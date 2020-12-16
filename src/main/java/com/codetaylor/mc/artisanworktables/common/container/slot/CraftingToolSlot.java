package com.codetaylor.mc.artisanworktables.common.container.slot;

import com.codetaylor.mc.athenaeum.inventory.spi.PredicateSlotItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class CraftingToolSlot
    extends PredicateSlotItemHandler {

  private final Runnable slotChangeListener;

  public CraftingToolSlot(
      Runnable slotChangeListener,
      Predicate<ItemStack> predicate,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(predicate, itemHandler, index, xPosition, yPosition);
    this.slotChangeListener = slotChangeListener;
  }

  @Override
  public void onSlotChanged() {

    super.onSlotChanged();
    this.slotChangeListener.run();
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return super.isItemValid(stack);
  }

  @Override
  public boolean canTakeStack(PlayerEntity player) {

    return super.canTakeStack(player);
  }
}
