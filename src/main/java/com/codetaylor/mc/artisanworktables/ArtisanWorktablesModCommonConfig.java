package com.codetaylor.mc.artisanworktables;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = ArtisanWorktablesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArtisanWorktablesModCommonConfig {

  public static ForgeConfigSpec CONFIG_SPEC;
  public static ConfigClient CONFIG;

  static {
    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    CONFIG = new ConfigClient(builder);
    CONFIG_SPEC = builder.build();
  }

  @SubscribeEvent
  public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {

    if (configEvent.getConfig().getSpec() == ArtisanWorktablesModCommonConfig.CONFIG_SPEC) {
      ArtisanWorktablesModCommonConfig.bake();
    }
  }

  public static boolean enableTileDataServiceMonitor;
  public static int tileDataServiceMonitorUpdateIntervalTicks;
  public static int tileDataServiceMonitorTrackedIndex;

  public static void bake() {

    enableTileDataServiceMonitor = CONFIG.enableTileDataServiceMonitor.get();
    tileDataServiceMonitorUpdateIntervalTicks = CONFIG.tileDataServiceMonitorUpdateIntervalTicks.get();
    tileDataServiceMonitorTrackedIndex = CONFIG.tileDataServiceMonitorTrackedIndex.get();
  }

  public static class ConfigClient {

    public final ForgeConfigSpec.BooleanValue enableTileDataServiceMonitor;
    public final ForgeConfigSpec.IntValue tileDataServiceMonitorUpdateIntervalTicks;
    public final ForgeConfigSpec.IntValue tileDataServiceMonitorTrackedIndex;

    public ConfigClient(ForgeConfigSpec.Builder builder) {

      this.enableTileDataServiceMonitor = builder
          .comment(
              "Enables / disables the tile data service monitor.",
              "Default: " + false
          )
          .define("enableTileDataServiceMonitor", false);

      this.tileDataServiceMonitorUpdateIntervalTicks = builder
          .comment(
              "Sets the tile data service monitor update interval in ticks.",
              "Default: " + 20
          )
          .defineInRange("tileDataServiceMonitorUpdateIntervalTicks", 20, 20, Integer.MAX_VALUE);

      this.tileDataServiceMonitorTrackedIndex = builder
          .comment(
              "Sets the tile data service monitor's tracked index.",
              "Default: " + 10
          )
          .defineInRange("tileDataServiceMonitorTrackedIndex", 10, 0, Integer.MAX_VALUE);
    }
  }
}
