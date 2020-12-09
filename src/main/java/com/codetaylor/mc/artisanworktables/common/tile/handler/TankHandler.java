package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.athenaeum.inventory.spi.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.network.spi.tile.ITileDataFluidTank;

public class TankHandler
    extends ObservableFluidTank
    implements ITileDataFluidTank {

  public TankHandler(int capacity) {

    super(capacity);
  }
}
