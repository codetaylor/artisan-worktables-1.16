package com.codetaylor.mc.artisanworktables.common.container.slot;

import net.minecraftforge.items.IItemHandler;

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
}
