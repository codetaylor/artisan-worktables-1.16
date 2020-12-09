package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.athenaeum.inventory.spi.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.ITileDataItemStackHandler;

public class SecondaryOutputStackHandler
    extends ObservableStackHandler
    implements ITileDataItemStackHandler {

  public SecondaryOutputStackHandler(int size) {

    super(size);
  }
}
