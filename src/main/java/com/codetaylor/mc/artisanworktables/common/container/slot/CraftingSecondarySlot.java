package com.codetaylor.mc.artisanworktables.common.container.slot;

import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CraftingSecondarySlot
    extends SlotItemHandler
    implements ICreativeSlotClick {

  private final BaseContainer container;
  private final BaseTileEntity tile;
  private final Runnable slotChangeListener;

  public CraftingSecondarySlot(
      BaseContainer container,
      BaseTileEntity tile,
      Runnable slotChangeListener,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.container = container;
    this.tile = tile;
    this.slotChangeListener = slotChangeListener;
  }

  @Override
  public void onSlotChanged() {

    super.onSlotChanged();

    this.slotChangeListener.run();
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return !this.tile.isCreative()
        && super.isItemValid(stack);
  }

  @Override
  public boolean canTakeStack(PlayerEntity player) {

    return !this.tile.isCreative()
        && super.canTakeStack(player);
  }

  @Override
  public ItemStack creativeSlotClick(int slotId, int dragType, ClickType clickType, PlayerEntity player) {

    ItemStack stack = this.getStack();

    if (!TagSlotClickDelegate.slotClick(this.container.inventorySlots, slotId, stack, this.tile.tagMap, clickType, player.world.isRemote, this.tile.isTagLinked())) {
      this.putStack(player.inventory.getItemStack().copy());
      this.tile.tagMap.remove(slotId);
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean allowTag() {

    return true;
  }
}