package com.codetaylor.mc.artisanworktables.client.event;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.client.screen.WorkshopScreen;
import com.codetaylor.mc.artisanworktables.client.screen.WorkstationScreen;
import com.codetaylor.mc.artisanworktables.client.screen.WorktableScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetupEventHandler {

  @SubscribeEvent
  public void on(FMLClientSetupEvent event) {

    ScreenManager.registerFactory(ArtisanWorktablesMod.ContainerTypes.WORKTABLE, WorktableScreen::new);
    ScreenManager.registerFactory(ArtisanWorktablesMod.ContainerTypes.WORKSTATION, WorkstationScreen::new);
    ScreenManager.registerFactory(ArtisanWorktablesMod.ContainerTypes.WORKSHOP, WorkshopScreen::new);
  }

}
