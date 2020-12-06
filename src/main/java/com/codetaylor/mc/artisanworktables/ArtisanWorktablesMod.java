package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.api.EnumTier;
import com.codetaylor.mc.artisanworktables.api.Reference;
import com.codetaylor.mc.artisanworktables.event.BlockRegistrationEventHandler;
import com.codetaylor.mc.artisanworktables.event.ItemRegistrationEventHandler;
import com.codetaylor.mc.artisanworktables.event.TileEntityRegistrationEventHandler;
import com.codetaylor.mc.artisanworktables.tile.WorkshopTileEntity;
import com.codetaylor.mc.artisanworktables.tile.WorkstationTileEntity;
import com.codetaylor.mc.artisanworktables.tile.WorktableTileEntity;
import com.codetaylor.mc.athenaeum.network.api.NetworkAPI;
import com.codetaylor.mc.athenaeum.network.spi.packet.IPacketService;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.ITileDataService;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.SCPacketTileData;
import com.codetaylor.mc.athenaeum.util.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Mod(ArtisanWorktablesMod.MOD_ID)
public class ArtisanWorktablesMod {

  public static final String MOD_ID = Reference.MOD_ID;
  public static final Logger LOGGER = LogManager.getLogger();

  public static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get();
  public static final Path MOD_CONFIG_PATH = CONFIG_PATH.resolve(MOD_ID);

  public static final List<Block> REGISTERED_WORKTABLES = new ArrayList<>();
  public static final Map<EnumTier, List<Block>> REGISTERED_WORKTABLES_BY_TIER = new EnumMap<>(EnumTier.class);

  public static IPacketService packetService;
  public static ITileDataService tileDataService;

  private static final String PACKET_SERVICE_PROTOCOL_VERSION = "1";

  public ArtisanWorktablesMod() {

    try {
      Files.createDirectories(MOD_CONFIG_PATH);

    } catch (IOException e) {
      LOGGER.error("Error creating folder: " + MOD_CONFIG_PATH, e);
    }

    ModLoadingContext modLoadingContext = ModLoadingContext.get();

    String configFilenameCommon = MOD_ID + "-common.toml";
    modLoadingContext.registerConfig(ModConfig.Type.COMMON, ArtisanWorktablesModCommonConfig.CONFIG_SPEC, MOD_ID + "/" + configFilenameCommon);
    ConfigHelper.loadConfig(ArtisanWorktablesModCommonConfig.CONFIG_SPEC, MOD_CONFIG_PATH.resolve(configFilenameCommon));

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.register(new BlockRegistrationEventHandler(REGISTERED_WORKTABLES, REGISTERED_WORKTABLES_BY_TIER));
    modEventBus.register(new ItemRegistrationEventHandler(REGISTERED_WORKTABLES));
    modEventBus.register(new TileEntityRegistrationEventHandler(REGISTERED_WORKTABLES_BY_TIER));

    ArtisanWorktablesMod.packetService = NetworkAPI.createPacketService(MOD_ID, MOD_ID, PACKET_SERVICE_PROTOCOL_VERSION);
    ArtisanWorktablesMod.tileDataService = NetworkAPI.createTileDataService(MOD_ID, MOD_ID, ArtisanWorktablesMod.packetService);

    ArtisanWorktablesMod.packetService.registerMessage(
        SCPacketTileData.class,
        SCPacketTileData.class
    );
  }

  @ObjectHolder(MOD_ID)
  public static class TileEntityTypes {

    @ObjectHolder(WorktableTileEntity.NAME)
    public static final TileEntityType<WorktableTileEntity> WORKTABLE_TILE_ENTITY_TYPE;

    @ObjectHolder(WorkstationTileEntity.NAME)
    public static final TileEntityType<WorkstationTileEntity> WORKSTATION_TILE_ENTITY_TYPE;

    @ObjectHolder(WorkshopTileEntity.NAME)
    public static final TileEntityType<WorkshopTileEntity> WORKSHOP_TILE_ENTITY_TYPE;

    static {
      WORKTABLE_TILE_ENTITY_TYPE = null;
      WORKSTATION_TILE_ENTITY_TYPE = null;
      WORKSHOP_TILE_ENTITY_TYPE = null;
    }
  }
}