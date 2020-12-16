package com.codetaylor.mc.artisanworktables.common.container.slot;

import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CraftingResultSlot
    extends SlotItemHandler {

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

    // TODO
    //this.tile.onTakeResult(player);
    this.slotChangeListener.run();
    return stack;
  }
}
