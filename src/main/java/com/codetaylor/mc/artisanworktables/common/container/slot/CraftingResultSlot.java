package com.codetaylor.mc.artisanworktables.common.container.slot;

import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CraftingResultSlot
    extends SlotItemHandler
    implements ICreativeSlotClick {

  private final Runnable slotChangeListener;
  private final BaseTileEntity tile;

  public CraftingResultSlot(
      Runnable slotChangeListener,
      BaseTileEntity tile,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.tile = tile;
    this.slotChangeListener = slotChangeListener;
  }

  @Nonnull
  @Override
  public ItemStack onTake(@Nonnull PlayerEntity player, @Nonnull ItemStack stack) {

    this.tile.onTakeResult(player);
    this.slotChangeListener.run();
    return stack;
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return this.tile.isCreative();
  }

  @Override
  public boolean canTakeStack(PlayerEntity player) {

    return !this.tile.isCreative()
        && super.canTakeStack(player);
  }

  @Override
  public ItemStack creativeSlotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {

    this.putStack(player.inventory.getItemStack().copy());
    return ItemStack.EMPTY;
  }
}
