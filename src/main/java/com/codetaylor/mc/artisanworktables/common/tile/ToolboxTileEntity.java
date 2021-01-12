package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.codetaylor.mc.artisanworktables.common.tile.handler.ToolboxItemStackHandler;
import com.codetaylor.mc.artisanworktables.common.util.ToolValidationHelper;
import com.codetaylor.mc.athenaeum.network.spi.tile.ITileData;
import com.codetaylor.mc.athenaeum.network.spi.tile.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.TileDataItemStackHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ToolboxTileEntity
    extends TileEntityDataBase {

  public static final String NAME = "toolbox";

  private final ToolboxItemStackHandler itemStackHandler;
  private final LazyOptional<ItemStackHandler> itemCapability;

  public ToolboxTileEntity() {

    super(
        ArtisanWorktablesMod.TileEntityTypes.TOOLBOX,
        ArtisanWorktablesMod.getProxy().getTileDataService()
    );

    Predicate<ItemStack> predicate = itemStack -> itemStack.isEmpty()
        || this.allowNonToolItems()
        || ToolValidationHelper.isValidTool(itemStack, ArtisanWorktablesMod.getProxy().getRecipeManager());

    this.itemStackHandler = new ToolboxItemStackHandler(predicate, 27);
    this.itemStackHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());

    this.itemCapability = LazyOptional.of(() -> this.itemStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.itemStackHandler)
    });
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  public ToolboxItemStackHandler getItemStackHandler() {

    return this.itemStackHandler;
  }

  public boolean canPlayerUse(PlayerEntity player) {

    return this.world != null
        && this.world.getTileEntity(this.getPos()) == this
        && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
  }

  private boolean allowNonToolItems() {

    return ArtisanWorktablesModCommonConfig.allowNonToolItemsInToolboxes;
  }

  // ---------------------------------------------------------------------------
  // Capability
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return this.itemCapability.cast();
    }

    return super.getCapability(capability, side);
  }

  // ---------------------------------------------------------------------------
  // Serialization
  // ---------------------------------------------------------------------------

  @Override
  public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {

    super.read(state, nbt);
    this.itemStackHandler.deserializeNBT(nbt.getCompound("itemStackHandler"));

    if (nbt.contains("Items", Constants.NBT.TAG_LIST)) {
      NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
      ItemStackHelper.loadAllItems(nbt, itemStacks);

      for (int i = 0; i < 27; i++) {
        this.itemStackHandler.forceStackInSlot(i, itemStacks.get(i));
      }
    }
  }

  @Nonnull
  @Override
  public CompoundNBT write(@Nonnull CompoundNBT nbt) {

    super.write(nbt);
    nbt.put("itemStackHandler", this.itemStackHandler.serializeNBT());
    return nbt;
  }
}