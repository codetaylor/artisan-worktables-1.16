package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.codetaylor.mc.artisanworktables.common.network.SCPacketIncompatible;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerLoggedInEventHandler {

  private final List<String> incompatibleModList;

  public PlayerLoggedInEventHandler(List<String> incompatibleModList) {

    this.incompatibleModList = incompatibleModList;
  }

  @SubscribeEvent
  public void on(PlayerEvent.PlayerLoggedInEvent event) {

    if (ArtisanWorktablesModCommonConfig.hideIncompatibilityMessage) {
      return;
    }

    List<String> result = new ArrayList<>();

    for (String modId : this.incompatibleModList) {

      if (ModList.get().isLoaded(modId)) {
        result.add(modId);
      }
    }

    if (!result.isEmpty()) {
      ArtisanWorktablesMod.getProxy().getPacketService().sendToPlayer((ServerPlayerEntity) event.getPlayer(), new SCPacketIncompatible(this.incompatibleModList));
    }
  }
}