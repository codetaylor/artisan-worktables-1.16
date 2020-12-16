package com.codetaylor.mc.artisanworktables.common.container.slot;

import net.minecraftforge.items.IItemHandler;

public class ToolboxSideSlot
    extends PredicateEnabledSlot {

  protected final int originX;
  protected final int originY;

  public ToolboxSideSlot(
      Predicate predicate,
      IItemHandler itemHandler,
      int index,
      int originX,
      int originY
  ) {

    super(predicate, itemHandler, index, originX, originY);
    this.originX = originX;
    this.originY = originY;
  }
}
