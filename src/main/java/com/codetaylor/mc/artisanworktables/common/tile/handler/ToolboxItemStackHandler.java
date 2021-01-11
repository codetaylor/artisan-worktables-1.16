package com.codetaylor.mc.artisanworktables.common.tile.handler;

import com.codetaylor.mc.athenaeum.inventory.spi.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.ITileDataItemStackHandler;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ToolboxItemStackHandler
    extends ObservableStackHandler
    implements ITileDataItemStackHandler {

  private final Predicate<ItemStack> predicate;

  public ToolboxItemStackHandler(Predicate<ItemStack> predicate, int size) {

    super(size);
    this.predicate = predicate;
  }

  @Override
  public void setStackInSlot(int slot, @Nonnull ItemStack stack) {

    if (this.predicate.test(stack)) {
      super.setStackInSlot(slot, stack);
    }
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

    if (this.predicate.test(stack)) {
      return super.insertItem(slot, stack, simulate);
    }

    return stack;
  }

  @Override
  public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

    return this.predicate.test(stack) && super.isItemValid(slot, stack);
  }
}
