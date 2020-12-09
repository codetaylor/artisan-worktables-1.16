package com.codetaylor.mc.artisanworktables.common.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class PatternResultSlot
    extends PredicateEnabledSlot {

  private final Runnable slotChangeListener;

  public PatternResultSlot(
      Predicate predicate,
      Runnable slotChangeListener,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(predicate, itemHandler, index, xPosition, yPosition);
    this.slotChangeListener = slotChangeListener;
  }

  @Nonnull
  @Override
  public ItemStack onTake(@Nonnull PlayerEntity player, @Nonnull ItemStack stack) {

    this.slotChangeListener.run();
    return stack;
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return false;
  }
}
