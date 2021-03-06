package com.codetaylor.mc.artisanworktables.common.container;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.container.slot.*;
import com.codetaylor.mc.artisanworktables.common.network.SCPacketWorktableContainerJoinedBlockBreak;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.common.tile.*;
import com.codetaylor.mc.artisanworktables.common.util.ToolValidationHelper;
import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseContainer
    extends ContainerBase {

  private final CraftingResultSlot craftingResultSlot;
  private final World world;
  private final BaseTileEntity tile;
  private final ToolboxTileEntity toolbox;
  private final ItemStackHandler resultHandler;
  private FluidStack lastFluidStack;
  private final PlayerEntity player;
  private final List<ToolboxSideSlot> toolboxSideSlotList;

  public final int slotIndexResult;
  public final int slotIndexCraftingMatrixStart;
  public final int slotIndexCraftingMatrixEnd;
  public final int slotIndexInventoryStart;
  public final int slotIndexInventoryEnd;
  public final int slotIndexHotbarStart;
  public final int slotIndexHotbarEnd;
  public final int slotIndexToolsStart;
  public final int slotIndexToolsEnd;
  public final int slotIndexSecondaryOutputStart;
  public final int slotIndexSecondaryOutputEnd;
  public final int slotIndexToolboxStart;
  public final int slotIndexToolboxEnd;
  public final int slotIndexSecondaryInputStart;
  public final int slotIndexSecondaryInputEnd;

  public BaseContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {

    super(type, id, playerInventory);

    this.lastFluidStack = FluidStack.EMPTY;
    this.world = world;

    this.tile = (BaseTileEntity) world.getTileEntity(pos);
    assert this.tile != null;

    this.toolbox = this.getToolbox(this.tile);
    this.tile.addContainer(this);

    this.player = playerInventory.player;
    Runnable slotChangeListener = this.tile::setRequiresRecipeUpdate;

    // ------------------------------------------------------------------------
    // Result
    // ------------------------------------------------------------------------

    this.slotIndexResult = this.nextSlotIndex;
    this.resultHandler = this.tile.getResultHandler();
    this.craftingResultSlot = new CraftingResultSlot(
        this.tile,
        this::updateRecipeOutput,
        this.resultHandler,
        0,
        this.getResultPositionX(),
        this.getResultPositionY()
    );
    this.containerSlotAdd(this.craftingResultSlot);

    // ------------------------------------------------------------------------
    // Crafting Matrix
    // ------------------------------------------------------------------------

    ICraftingMatrixStackHandler craftingMatrixHandler = this.tile.getCraftingMatrixHandler();
    this.slotIndexCraftingMatrixStart = this.nextSlotIndex;

    for (int y = 0; y < craftingMatrixHandler.getHeight(); ++y) {
      for (int x = 0; x < craftingMatrixHandler.getWidth(); ++x) {
        this.containerSlotAdd(new CraftingIngredientSlot(
            slotChangeListener,
            craftingMatrixHandler,
            x + y * craftingMatrixHandler.getWidth(),
            20 + x * 18,
            17 + y * 18
        ));
      }
    }
    this.slotIndexCraftingMatrixEnd = this.nextSlotIndex - 1;

    // ------------------------------------------------------------------------
    // Player Inventory
    // ------------------------------------------------------------------------

    this.slotIndexInventoryStart = this.nextSlotIndex;
    this.containerPlayerInventoryAdd();
    this.slotIndexInventoryEnd = this.nextSlotIndex - 1;

    // ------------------------------------------------------------------------
    // Player HotBar
    // ------------------------------------------------------------------------

    this.slotIndexHotbarStart = this.nextSlotIndex;
    this.containerPlayerHotbarAdd();
    this.slotIndexHotbarEnd = this.nextSlotIndex - 1;

    // ------------------------------------------------------------------------
    // Tool Slots
    // ------------------------------------------------------------------------

    {
      this.slotIndexToolsStart = this.nextSlotIndex;
      ItemStackHandler toolHandler = this.tile.getToolHandler();

      for (int i = 0; i < toolHandler.getSlots(); i++) {
        this.containerSlotAdd(new CraftingToolSlot(
            slotChangeListener,
            itemStack -> ToolValidationHelper.isValidTool(this.tile.getTableType(), itemStack, world.getRecipeManager()),
            toolHandler,
            i,
            78 + this.getToolOffsetX(),
            35 + 22 * i + this.getToolOffsetY()
        ));
      }
      this.slotIndexToolsEnd = this.nextSlotIndex - 1;
    }

    // ------------------------------------------------------------------------
    // Secondary output
    this.slotIndexSecondaryOutputStart = this.nextSlotIndex;

    if (this.tile instanceof WorkshopTileEntity) {

      for (int i = 0; i < 3; i++) {
        this.containerSlotAdd(new CraftingExtraResultSlot(
            this.tile.getSecondaryOutputHandler(),
            i,
            116 + i * 18,
            17
        ));
      }

    } else {

      for (int i = 0; i < 3; i++) {
        this.containerSlotAdd(new CraftingExtraResultSlot(
            this.tile.getSecondaryOutputHandler(),
            i,
            152,
            17 + i * 18
        ));
      }
    }
    this.slotIndexSecondaryOutputEnd = this.nextSlotIndex - 1;

    // ------------------------------------------------------------------------
    // Secondary input
    // ------------------------------------------------------------------------

    if (this.tile instanceof SecondaryInputBaseTileEntity) {
      this.slotIndexSecondaryInputStart = this.nextSlotIndex;
      IItemHandler handler = this.tile.getSecondaryIngredientHandler();
      int slotCount = handler.getSlots();

      for (int i = 0; i < slotCount; i++) {
        this.containerSlotAdd(new CraftingSecondarySlot(
            slotChangeListener,
            handler,
            i,
            8 + i * 18,
            75 + this.getSecondaryInputOffsetY()
        ));
      }
      this.slotIndexSecondaryInputEnd = this.nextSlotIndex - 1;

    } else {
      this.slotIndexSecondaryInputStart = -1;
      this.slotIndexSecondaryInputEnd = -1;
    }

    // ------------------------------------------------------------------------
    // Side Toolbox
    // ------------------------------------------------------------------------

    this.slotIndexToolboxStart = this.nextSlotIndex;
    if (this.canPlayerUseToolbox()) {
      this.toolboxSideSlotList = new ArrayList<>(27);
      ItemStackHandler itemHandler = this.toolbox.getItemStackHandler();

      for (int x = 0; x < 3; x++) {

        for (int y = 0; y < 9; y++) {
          ToolboxSideSlot toolboxSideSlot = new ToolboxSideSlot(
              this::canPlayerUseToolbox,
              itemHandler,
              y + x * 9,
              x * -18 + this.containerToolboxOffsetGetX(),
              y * 18 + 8
          );
          this.toolboxSideSlotList.add(toolboxSideSlot);
          this.containerSlotAdd(toolboxSideSlot);
        }
      }

    } else {
      this.toolboxSideSlotList = Collections.emptyList();
    }
    this.slotIndexToolboxEnd = this.nextSlotIndex - 1;

    this.updateRecipeOutput();
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  public BaseTileEntity getTile() {

    return this.tile;
  }

  private ToolboxTileEntity getToolbox(BaseTileEntity tile) {

    return tile.getAdjacentToolbox();
  }

  private int containerToolboxOffsetGetX() {

    return -26;
  }

  private int getSecondaryInputOffsetY() {

    if (this.tile instanceof WorkshopTileEntity) {
      return 36;
    }

    return 0;
  }

  private int getToolOffsetX() {

    if (this.tile instanceof WorkshopTileEntity) {
      return 36;
    }

    return 0;
  }

  private int getToolOffsetY() {

    if (this.tile instanceof WorkstationTileEntity) {
      return -11;

    }
    if (this.tile instanceof WorkshopTileEntity) {
      return 5;
    }

    return 0;
  }

  private int getResultPositionY() {

    if (this.tile instanceof WorkshopTileEntity) {
      return 62;
    }

    return 35;
  }

  private int getResultPositionX() {

    if (this.tile instanceof WorkshopTileEntity) {
      return 143;
    }

    return 115;
  }

  // ---------------------------------------------------------------------------
  // Accessor Implementations
  // ---------------------------------------------------------------------------

  @Override
  protected int containerInventoryPositionGetY() {

    if (this.tile instanceof WorkstationTileEntity) {
      return 107;

    } else if (this.tile instanceof WorkshopTileEntity) {
      return 143;
    }

    return super.containerInventoryPositionGetY();
  }

  @Override
  protected int containerInventoryPositionGetX() {

    return super.containerInventoryPositionGetX();
  }

  @Override
  protected int containerHotbarPositionGetY() {

    if (this.tile instanceof WorkstationTileEntity) {
      return 165;

    } else if (this.tile instanceof WorkshopTileEntity) {
      return 201;
    }

    return super.containerHotbarPositionGetY();
  }

  @Override
  protected int containerHotbarPositionGetX() {

    return super.containerHotbarPositionGetX();
  }

  // ---------------------------------------------------------------------------
  // Actions
  // ---------------------------------------------------------------------------

  public void onJoinedBlockBreak(World world, BlockPos pos) {

    if (!world.isRemote) {
      ArtisanWorktablesMod.getProxy().getPacketService().sendToPlayer(
          (ServerPlayerEntity) this.player,
          new SCPacketWorktableContainerJoinedBlockBreak(pos)
      );
    }
  }

  /**
   * @return the adjacent toolbox, or null
   */
  @Nullable
  public ToolboxTileEntity getToolbox() {

    if (this.hasValidToolbox()) {

      return this.toolbox;
    }

    return null;
  }

  public boolean hasValidToolbox() {

    return this.toolbox != null && !this.toolbox.isRemoved();
  }

  public boolean canPlayerUseToolbox() {

    return this.hasValidToolbox() && this.toolbox.canPlayerUse(this.player);
  }

  public void updateRecipeOutput() {

    if (this.tile == null) {
      return;
    }

    ArtisanRecipe recipe = this.tile.getRecipe(this.player);

    if (recipe != null) {
      this.resultHandler.setStackInSlot(0, recipe.getRecipeOutput());

    } else {
      this.resultHandler.setStackInSlot(0, ItemStack.EMPTY);
    }
  }

  @Override
  public void onCraftMatrixChanged(@Nonnull IInventory inventory) {
    //
  }

  @Override
  public boolean canInteractWith(@Nonnull PlayerEntity player) {

    return this.tile.canPlayerUse(player);
  }

  private boolean swapItemStack(int originSlotIndex, int targetSlotIndex, boolean swapOnlyEmtpyTarget) {

    Slot originSlot = this.inventorySlots.get(originSlotIndex);
    Slot targetSlot = this.inventorySlots.get(targetSlotIndex);

    ItemStack originStack = originSlot.getStack();
    ItemStack targetStack = targetSlot.getStack();

    if (swapOnlyEmtpyTarget) {

      if (!targetStack.isEmpty()) {
        return false;
      }
    }

    if (originStack.isItemEqual(targetStack)) {
      return true;
    }

    if (!originStack.isEmpty()
        && targetSlot.isItemValid(originStack)) {

      if (targetStack.isEmpty()) {
        targetSlot.putStack(originStack);
        originSlot.putStack(ItemStack.EMPTY);

      } else {
        targetSlot.putStack(originStack);
        originSlot.putStack(targetStack);
      }

      return true;
    }

    return false;
  }

  // ---------------------------------------------------------------------------
  // Slots
  // ---------------------------------------------------------------------------

  @Override
  public boolean canMergeSlot(@Nonnull ItemStack stack, @Nonnull Slot slot) {

    return slot != this.craftingResultSlot && super.canMergeSlot(stack, slot);
  }

  private boolean isSlotSecondaryInput(int slotIndex) {

    return slotIndex >= this.slotIndexSecondaryInputStart && slotIndex <= this.slotIndexSecondaryInputEnd;
  }

  private boolean isSlotIndexResult(int slotIndex) {

    return slotIndex == this.slotIndexResult;
  }

  private boolean isSlotIndexInventory(int slotIndex) {

    return slotIndex >= this.slotIndexInventoryStart && slotIndex <= this.slotIndexInventoryEnd;
  }

  private boolean isSlotIndexHotbar(int slotIndex) {

    return slotIndex >= this.slotIndexHotbarStart && slotIndex <= this.slotIndexHotbarEnd;
  }

  private boolean isSlotIndexToolbox(int slotIndex) {

    return slotIndex >= this.slotIndexToolboxStart && slotIndex <= this.slotIndexToolboxEnd;
  }

  private boolean isSlotIndexTool(int slotIndex) {

    return slotIndex >= this.slotIndexToolsStart && slotIndex <= this.slotIndexToolsEnd;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean mergeInventory(ItemStack itemStack, boolean reverse) {

    return this.mergeItemStack(itemStack, this.slotIndexInventoryStart, this.slotIndexInventoryEnd + 1, reverse);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean mergeHotbar(ItemStack itemStack, boolean reverse) {

    return this.mergeItemStack(itemStack, this.slotIndexHotbarStart, this.slotIndexHotbarEnd + 1, reverse);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean mergeCraftingMatrix(ItemStack itemStack, boolean reverse) {

    return this.mergeItemStack(
        itemStack,
        this.slotIndexCraftingMatrixStart,
        this.slotIndexCraftingMatrixEnd + 1,
        reverse
    );
  }

  private boolean mergeToolbox(ItemStack itemStack, boolean reverse) {

    return this.mergeItemStack(itemStack, this.slotIndexToolboxStart, this.slotIndexToolboxEnd + 1, reverse);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean mergeSecondaryInput(ItemStack itemStack, boolean reverse) {

    if (this.slotIndexSecondaryInputStart == -1) {
      return false;
    }

    return this.mergeItemStack(
        itemStack,
        this.slotIndexSecondaryInputStart,
        this.slotIndexSecondaryInputEnd + 1,
        reverse
    );
  }

  private boolean swapTools(int slotIndex) {

    for (int i = this.slotIndexToolsStart; i <= this.slotIndexToolsEnd; i++) {

      // try to swap tool into empty slot first
      if (this.swapItemStack(slotIndex, i, true)) {
        return true; // Swapped tools
      }
    }

    for (int i = this.slotIndexToolsStart; i <= this.slotIndexToolsEnd; i++) {

      // swap tools into any valid slot
      if (this.swapItemStack(slotIndex, i, false)) {
        return true; // Swapped tools
      }
    }

    return false;
  }

  @Nonnull
  @Override
  public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickType, @Nonnull PlayerEntity player) {

    if (slotId == this.slotIndexResult
        || (slotId >= this.slotIndexSecondaryOutputStart && slotId <= this.slotIndexSecondaryOutputEnd)) {
      // prevent deleting half of the stack
      return super.slotClick(slotId, 0, clickType, player);
    }

    return super.slotClick(slotId, dragType, clickType, player);
  }

  @Nonnull
  @Override
  public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int slotIndex) {

    ItemStack itemStackCopy = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(slotIndex);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemStack = slot.getStack();
      itemStackCopy = itemStack.copy();

      if (this.isSlotIndexResult(slotIndex)) {
        // Result

        // This is executed on both the client and server for each craft. If the crafting
        // grid has multiple, complete recipes, this will be executed for each complete
        // recipe.

        ArtisanRecipe recipe = this.tile.getRecipe(player);

        if (recipe == null) {
          return ItemStack.EMPTY;
        }

        if (!this.mergeInventory(itemStack, false)
            && !this.mergeHotbar(itemStack, false)) {
          return ItemStack.EMPTY;
        }

        itemStack.getItem().onCreated(itemStack, this.world, player);
        slot.onSlotChange(itemStack, itemStackCopy);

      } else if (this.isSlotIndexInventory(slotIndex)) {
        // Inventory clicked, try to move to tool slot first, then blank pattern slot,
        // then crafting matrix, then secondary, then hotbar

        if (this.swapTools(slotIndex)) {
          return ItemStack.EMPTY; // swapped tools
        }

        if (!this.mergeCraftingMatrix(itemStack, false)
            && !this.mergeSecondaryInput(itemStack, false)
            && !this.mergeHotbar(itemStack, false)) {
          return ItemStack.EMPTY;
        }

      } else if (this.isSlotIndexHotbar(slotIndex)) {
        // HotBar clicked, try to move to tool slot first, then blank pattern slot,
        // then crafting matrix, then secondary, then inventory

        if (this.swapTools(slotIndex)) {
          return ItemStack.EMPTY; // swapped tools
        }

        if (!this.mergeCraftingMatrix(itemStack, false)
            && !this.mergeSecondaryInput(itemStack, false)
            && !this.mergeInventory(itemStack, false)) {
          return ItemStack.EMPTY;
        }

      } else if (this.isSlotIndexToolbox(slotIndex)) {
        // Toolbox clicked, try to move to tool slot first, then crafting matrix, then secondary, then inventory, then hotbar

        if (this.swapTools(slotIndex)) {
          return ItemStack.EMPTY; // swapped tools
        }

        if (!this.mergeCraftingMatrix(itemStack, false)
            && !this.mergeSecondaryInput(itemStack, false)
            && !this.mergeInventory(itemStack, false)
            && !this.mergeHotbar(itemStack, false)) {
          return ItemStack.EMPTY;
        }

      } else if (this.isSlotIndexTool(slotIndex)) {
        // Tool slot clicked, try to move to toolbox first, then inventory, then hotbar

        if (this.canPlayerUseToolbox()) {

          if (!this.mergeToolbox(itemStack, false)
              && !this.mergeInventory(itemStack, false)
              && !this.mergeHotbar(itemStack, false)) {
            return ItemStack.EMPTY;
          }

        } else if (!this.mergeInventory(itemStack, false)
            && !this.mergeHotbar(itemStack, false)) {
          return ItemStack.EMPTY;
        }

      } else if (!this.mergeInventory(itemStack, false)
          && !this.mergeHotbar(itemStack, false)) {
        // All others: crafting matrix, secondary output
        return ItemStack.EMPTY;
      }

      if (itemStack.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);

      } else {
        slot.onSlotChanged();
      }

      if (itemStack.getCount() == itemStackCopy.getCount()) {
        return ItemStack.EMPTY;
      }

      ItemStack itemStack2 = slot.onTake(player, itemStack);

      if (slotIndex == 0) {
        player.dropItem(itemStack2, false);
      }
    }

    return itemStackCopy;
  }

  // ---------------------------------------------------------------------------
  // Sync
  // ---------------------------------------------------------------------------

  @Override
  public void detectAndSendChanges() {

    super.detectAndSendChanges();

    //System.out.println("Player: " + this.player + ", Stack:" + this.craftingResultSlot.getStack());

    World world = this.tile.getWorld();

    if (this.tile == null
        || world == null
        || world.isRemote) {
      return;
    }

    // Send fluid changes to the client.

    FluidTank tank = this.tile.getTank();
    FluidStack fluidStack = tank.getFluid();

    if (this.lastFluidStack != FluidStack.EMPTY
        && fluidStack == FluidStack.EMPTY) {
      this.lastFluidStack = null;
      this.updateRecipeOutput();

    } else if (this.lastFluidStack == FluidStack.EMPTY
        && fluidStack != FluidStack.EMPTY) {
      this.lastFluidStack = fluidStack.copy();
      this.updateRecipeOutput();

    } else if (this.lastFluidStack != FluidStack.EMPTY) {

      if (!this.lastFluidStack.isFluidStackIdentical(fluidStack)) {
        this.lastFluidStack = fluidStack.copy();
        this.updateRecipeOutput();
      }
    }
  }

  @Override
  public void onContainerClosed(@Nonnull PlayerEntity player) {

    super.onContainerClosed(player);
    this.tile.removeContainer(this);
  }
}