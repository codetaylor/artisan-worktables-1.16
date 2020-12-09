package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import com.codetaylor.mc.artisanworktables.api.pattern.IItemDesignPattern;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class PatternSlot
    extends PredicateEnabledSlot {

  private final Runnable slotChangeListener;

  public PatternSlot(
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

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    Item item = stack.getItem();

    return this.isEnabled()
        && (item instanceof IItemDesignPattern)
        && (!((IItemDesignPattern) item).hasRecipe(stack));
  }

  @Override
  public void onSlotChanged() {

    super.onSlotChanged();
    this.slotChangeListener.run();
  }
}
