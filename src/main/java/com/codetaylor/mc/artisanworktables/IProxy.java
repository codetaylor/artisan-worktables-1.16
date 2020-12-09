package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.athenaeum.network.spi.packet.IPacketService;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.ITileDataService;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;

public interface IProxy {

  void initialize();

  void registerModEventHandlers(IEventBus eventBus);

  void registerForgeEventHandlers(IEventBus eventBus);

  List<Block> getRegisteredWorktables();

  ITileDataService getTileDataService();

  IPacketService getPacketService();

  boolean isIntegratedServerRunning();
}
