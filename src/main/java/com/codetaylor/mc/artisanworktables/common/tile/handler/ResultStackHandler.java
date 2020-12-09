package com.codetaylor.mc.artisanworktables.common.tile.handler;

import com.codetaylor.mc.athenaeum.inventory.spi.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.ITileDataItemStackHandler;

public class ResultStackHandler
    extends ObservableStackHandler
    implements ITileDataItemStackHandler {

  public ResultStackHandler(int size) {

    super(size);
  }
}
