package com.codetaylor.mc.artisanworktables;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = ArtisanWorktablesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArtisanWorktablesModCommonConfig {

  public static ForgeConfigSpec CONFIG_SPEC;
  public static ConfigCommon CONFIG;

  static {
    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    CONFIG = new ConfigCommon(builder);
    CONFIG_SPEC = builder.build();
  }

  @SubscribeEvent
  public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {

    if (configEvent.getConfig().getSpec() == ArtisanWorktablesModCommonConfig.CONFIG_SPEC) {
      ArtisanWorktablesModCommonConfig.bake();
    }
  }

  public static boolean enableDuplicateRecipeNameWarnings;
  public static boolean restrictCraftMinimumDurability;
  public static int fluidCapacityWorktable;
  public static int fluidCapacityWorkstation;
  public static int fluidCapacityWorkshop;

  public static void bake() {

    enableDuplicateRecipeNameWarnings = CONFIG.enableDuplicateRecipeNameWarnings.get();
    restrictCraftMinimumDurability = CONFIG.restrictCraftMinimumDurability.get();
    fluidCapacityWorktable = CONFIG.fluidCapacityWorktable.get();
    fluidCapacityWorkstation = CONFIG.fluidCapacityWorkstation.get();
    fluidCapacityWorkshop = CONFIG.fluidCapacityWorkshop.get();
  }

  public static class ConfigCommon {

    public final ForgeConfigSpec.BooleanValue enableDuplicateRecipeNameWarnings;
    public final ForgeConfigSpec.BooleanValue restrictCraftMinimumDurability;
    public final ForgeConfigSpec.IntValue fluidCapacityWorktable;
    public final ForgeConfigSpec.IntValue fluidCapacityWorkstation;
    public final ForgeConfigSpec.IntValue fluidCapacityWorkshop;

    public ConfigCommon(ForgeConfigSpec.Builder builder) {

      this.enableDuplicateRecipeNameWarnings = builder
          .comment(
              "Set to true to enable log warnings for duplicate recipe names.",
              "If you're using a lot of your own recipe names, you can enable this",
              "from time to time to check that you haven't accidentally used the",
              "same name twice.",
              "Default: " + false
          )
          .define("enableDuplicateRecipeNameWarnings", false);

      this.restrictCraftMinimumDurability = builder
          .comment(
              "If set to true, crafting tools must have sufficient durability remaining to perform the craft.",
              "If set to false, this restriction is ignored.",
              "Default: " + true
          )
          .define("restrictCraftMinimumDurability", true);

      this.fluidCapacityWorktable = builder
          .comment(
              "Worktable fluid capacity in millibuckets.",
              "Default: " + 4000
          )
          .defineInRange("fluidCapacityWorktable", 4000, 0, Integer.MAX_VALUE);

      this.fluidCapacityWorkstation = builder
          .comment(
              "Workstation fluid capacity in millibuckets.",
              "Default: " + 8000
          )
          .defineInRange("fluidCapacityWorkstation", 8000, 0, Integer.MAX_VALUE);

      this.fluidCapacityWorkshop = builder
          .comment(
              "Workshop fluid capacity in millibuckets.",
              "Default: " + 16000
          )
          .defineInRange("fluidCapacityWorkshop", 16000, 0, Integer.MAX_VALUE);
    }
  }
}
