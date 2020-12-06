package com.codetaylor.mc.artisanworktables.datagen;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ArtisanWorktablesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GatherDataEventHandler {

  @SubscribeEvent
  public static void on(GatherDataEvent event) {

    DataGenerator dataGenerator = event.getGenerator();
    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

    dataGenerator.addProvider(new BlockStates(dataGenerator, existingFileHelper));
  }

  private GatherDataEventHandler() {
    //
  }
}
