package com.codetaylor.mc.artisanworktables.common.container.slot;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class ResultSlot
    extends SlotItemHandler {

  public ResultSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {

    super(itemHandler, index, xPosition, yPosition);
  }
}
