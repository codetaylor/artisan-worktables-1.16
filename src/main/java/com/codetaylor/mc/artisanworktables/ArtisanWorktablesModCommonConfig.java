package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.api.Reference;
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

  public static void bake() {

    Reference.Config.restrictCraftMinimumDurability = CONFIG.restrictCraftMinimumDurability.get();
  }

  public static class ConfigClient {

    public final ForgeConfigSpec.BooleanValue restrictCraftMinimumDurability;

    public ConfigClient(ForgeConfigSpec.Builder builder) {

      this.restrictCraftMinimumDurability = builder
          .comment(
              "If set to true, crafting tools must have sufficient durability remaining to perform the craft.",
              "If set to false, this restriction is ignored.",
              "Default: " + true
          )
          .define("restrictCraftMinimumDurability", true);

    }
  }
}
