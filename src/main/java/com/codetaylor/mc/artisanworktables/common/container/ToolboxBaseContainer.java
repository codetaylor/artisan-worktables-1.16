package com.codetaylor.mc.artisanworktables.common.container;

import com.codetaylor.mc.artisanworktables.common.block.ToolboxBlock;
import com.codetaylor.mc.artisanworktables.common.tile.ToolboxTileEntity;
import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class ToolboxBaseContainer
    extends ContainerBase {

  private static final int NUM_ROWS = 3;

  private ToolboxTileEntity tile;
  private ToolboxBlock block;

  public ToolboxBaseContainer(ContainerType<? extends ToolboxBaseContainer> containerType, int id, World world, BlockPos blockPos, PlayerInventory playerInventory, PlayerEntity player) {

    super(containerType, id, playerInventory);

    this.tile = Objects.requireNonNull((ToolboxTileEntity) world.getTileEntity(blockPos));
    this.block = Objects.requireNonNull((ToolboxBlock) world.getBlockState(blockPos).getBlock());

    for (int j = 0; j < NUM_ROWS; ++j) {
      for (int k = 0; k < 9; ++k) {
        this.containerSlotAdd(new SlotItemHandler(this.tile.getItemStackHandler(), k + j * 9, 8 + k * 18, 17 + j * 18));
      }
    }

    this.containerPlayerInventoryAdd();
    this.containerPlayerHotbarAdd();
  }

  public ToolboxBlock getBlock() {

    return this.block;
  }

  @Override
  public boolean canInteractWith(@Nonnull PlayerEntity player) {

    return this.tile.canPlayerUse(player);
  }

  @Nonnull
  @Override
  public ItemStack transferStackInSlot(PlayerEntity player, int index) {

    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (index < NUM_ROWS * 9) {
        if (!this.mergeItemStack(itemstack1, NUM_ROWS * 9, this.inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 0, NUM_ROWS * 9, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }
    }

    return itemstack;
  }
}
