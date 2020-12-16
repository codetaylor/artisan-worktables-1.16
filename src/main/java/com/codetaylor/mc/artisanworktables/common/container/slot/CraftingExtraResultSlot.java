package com.codetaylor.mc.artisanworktables.common.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class CraftingExtraResultSlot
    extends ResultSlot {

  public CraftingExtraResultSlot(
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return false;
  }
}
