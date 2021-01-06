package com.codetaylor.mc.artisanworktables.common;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.codetaylor.mc.artisanworktables.IProxy;
import com.codetaylor.mc.artisanworktables.common.event.*;
import com.codetaylor.mc.artisanworktables.common.network.*;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.athenaeum.network.api.NetworkAPI;
import com.codetaylor.mc.athenaeum.network.spi.packet.IPacketService;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.ITileDataService;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.SCPacketTileData;
import com.codetaylor.mc.athenaeum.util.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CommonProxy
    implements IProxy {

  protected final List<Block> registeredWorktables;
  protected final Map<EnumTier, List<Block>> registeredWorktablesByTier;
  protected final EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShaped;
  protected final EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShapeless;

  protected IPacketService packetService;
  protected ITileDataService tileDataService;

  public CommonProxy() {

    this.registeredWorktables = new ArrayList<>();
    this.registeredWorktablesByTier = new EnumMap<>(EnumTier.class);
    this.registeredSerializersShaped = new EnumMap<>(EnumType.class);
    this.registeredSerializersShapeless = new EnumMap<>(EnumType.class);
  }

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

    this.packetService.registerMessage(SCPacketTileData.class, SCPacketTileData.class);
    this.packetService.registerMessage(CSPacketWorktableClear.class, CSPacketWorktableClear.class);
    this.packetService.registerMessage(CSPacketWorktableCreativeToggle.class, CSPacketWorktableCreativeToggle.class);
    this.packetService.registerMessage(CSPacketWorktableLockedModeToggle.class, CSPacketWorktableLockedModeToggle.class);
    this.packetService.registerMessage(CSPacketWorktableTab.class, CSPacketWorktableTab.class);
    this.packetService.registerMessage(CSPacketWorktableTankDestroyFluid.class, CSPacketWorktableTankDestroyFluid.class);
    this.packetService.registerMessage(SCPacketWorktableContainerJoinedBlockBreak.class, SCPacketWorktableContainerJoinedBlockBreak.class);
    this.packetService.registerMessage(SCPacketWorktableFluidUpdate.class, SCPacketWorktableFluidUpdate.class);
  }

  @Override
  public void registerModEventHandlers(IEventBus eventBus) {

    eventBus.register(new BlockRegistrationEventHandler(this.registeredWorktables, this.registeredWorktablesByTier));
    eventBus.register(new ItemRegistrationEventHandler(this.registeredWorktables));
    eventBus.register(new TileEntityRegistrationEventHandler(this.registeredWorktablesByTier));
    eventBus.register(new ContainerTypeRegistrationEventHandler());
    eventBus.register(new RecipeSerializerRegistrationEventHandler(this.registeredSerializersShaped, this.registeredSerializersShapeless));
    eventBus.register(new ParticleTypeRegistrationEventHandler());
  }

  @Override
  public void registerForgeEventHandlers(IEventBus eventBus) {

    eventBus.register(new TagsUpdatedEventHandler());
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

  @Override
  public IPacketService getPacketService() {

    return this.packetService;
  }

  @Override
  public boolean isIntegratedServerRunning() {

    return false;
  }

  @Override
  public EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShaped() {

    return this.registeredSerializersShaped;
  }

  @Override
  public EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShapeless() {

    return this.registeredSerializersShapeless;
  }

  @Nullable
  @Override
  public RecipeManager getRecipeManager() {

    MinecraftServer minecraftServer = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);

    if (minecraftServer != null) {
      return minecraftServer.getRecipeManager();
    }

    return null;
  }
}
