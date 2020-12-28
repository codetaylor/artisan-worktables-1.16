package com.codetaylor.mc.artisanworktables.datagen;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = ArtisanWorktablesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GatherDataEventHandler {

  private static final Logger LOGGER = LogManager.getLogger(GatherDataEventHandler.class);

  @SubscribeEvent
  public static void on(GatherDataEvent event) {

    DataGenerator dataGenerator = event.getGenerator();
    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

    dataGenerator.addProvider(new BlockStates(dataGenerator, existingFileHelper));
    dataGenerator.addProvider(
        new LootTables(
            dataGenerator,
            new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(),
            LOGGER
        )
    );
  }

  private GatherDataEventHandler() {
    //
  }
}
