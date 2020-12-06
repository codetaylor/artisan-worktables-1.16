package com.codetaylor.mc.artisanworktables.common;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.codetaylor.mc.artisanworktables.IProxy;
import com.codetaylor.mc.artisanworktables.api.EnumTier;
import com.codetaylor.mc.artisanworktables.common.event.BlockRegistrationEventHandler;
import com.codetaylor.mc.artisanworktables.common.event.ContainerTypeRegistrationEventHandler;
import com.codetaylor.mc.artisanworktables.common.event.ItemRegistrationEventHandler;
import com.codetaylor.mc.artisanworktables.common.event.TileEntityRegistrationEventHandler;
import com.codetaylor.mc.athenaeum.network.api.NetworkAPI;
import com.codetaylor.mc.athenaeum.network.spi.packet.IPacketService;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.ITileDataService;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.SCPacketTileData;
import com.codetaylor.mc.athenaeum.util.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CommonProxy
    implements IProxy {

  protected final List<Block> registeredWorktables = new ArrayList<>();
  protected final Map<EnumTier, List<Block>> registeredWorktablesByTier = new EnumMap<>(EnumTier.class);

  protected IPacketService packetService;
  protected ITileDataService tileDataService;

  @Override
  public void initialize() {

    String modId = ArtisanWorktablesMod.MOD_ID;
    Path configPath = FMLPaths.CONFIGDIR.get();
    Path modConfigPath = configPath.resolve(modId);

    try {
      Files.createDirectories(modConfigPath);

    } catch (IOException e) {
      ArtisanWorktablesMod.LOGGER.error("Error creating folder: " + modConfigPath, e);
    }

    ModLoadingContext modLoadingContext = ModLoadingContext.get();
    String configFilenameCommon = modId + "-common.toml";
    modLoadingContext.registerConfig(ModConfig.Type.COMMON, ArtisanWorktablesModCommonConfig.CONFIG_SPEC, modId + "/" + configFilenameCommon);
    ConfigHelper.loadConfig(ArtisanWorktablesModCommonConfig.CONFIG_SPEC, modConfigPath.resolve(configFilenameCommon));

    this.packetService = NetworkAPI.createPacketService(modId, modId, ArtisanWorktablesMod.PACKET_SERVICE_PROTOCOL_VERSION);
    this.tileDataService = NetworkAPI.createTileDataService(modId, modId, this.packetService);

    this.packetService.registerMessage(
        SCPacketTileData.class,
        SCPacketTileData.class
    );
  }

  @Override
  public void registerModEventHandlers(IEventBus eventBus) {

    eventBus.register(new BlockRegistrationEventHandler(this.registeredWorktables, this.registeredWorktablesByTier));
    eventBus.register(new ItemRegistrationEventHandler(this.registeredWorktables));
    eventBus.register(new TileEntityRegistrationEventHandler(this.registeredWorktablesByTier));
    eventBus.register(new ContainerTypeRegistrationEventHandler());
  }

  @Override
  public void registerForgeEventHandlers(IEventBus eventBus) {
    //
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  @Override
  public List<Block> getRegisteredWorktables() {

    return Collections.unmodifiableList(this.registeredWorktables);
  }

  @Override
  public ITileDataService getTileDataService() {

    return this.tileDataService;
  }
}
