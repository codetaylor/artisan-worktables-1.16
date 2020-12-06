package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.api.Reference;
import com.codetaylor.mc.artisanworktables.event.BlockRegistrationEventHandler;
import com.codetaylor.mc.artisanworktables.event.ItemRegistrationEventHandler;
import com.codetaylor.mc.athenaeum.util.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mod(ArtisanWorktablesMod.MOD_ID)
public class ArtisanWorktablesMod {

  public static final String MOD_ID = Reference.MOD_ID;
  public static final Logger LOGGER = LogManager.getLogger();

  public static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get();
  public static final Path MOD_CONFIG_PATH = CONFIG_PATH.resolve(MOD_ID);

  public static final List<Block> REGISTERED_WORKTABLES = new ArrayList<>();

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
    modEventBus.register(new BlockRegistrationEventHandler(REGISTERED_WORKTABLES));
    modEventBus.register(new ItemRegistrationEventHandler(REGISTERED_WORKTABLES));
  }
}