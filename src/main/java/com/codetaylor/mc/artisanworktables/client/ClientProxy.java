package com.codetaylor.mc.artisanworktables.client;

import com.codetaylor.mc.artisanworktables.client.event.ClientSetupEventHandler;
import com.codetaylor.mc.artisanworktables.client.event.ParticleFactoryRegisterEventHandler;
import com.codetaylor.mc.artisanworktables.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nullable;

public class ClientProxy
    extends CommonProxy {

  @Override
  public void registerModEventHandlers(IEventBus eventBus) {

    super.registerModEventHandlers(eventBus);

    eventBus.register(new ClientSetupEventHandler());
    eventBus.register(new ParticleFactoryRegisterEventHandler());
  }

  @Override
  public boolean isIntegratedServerRunning() {

    return Minecraft.getInstance().isIntegratedServerRunning();
  }

  @Nullable
  @Override
  public RecipeManager getRecipeManager() {

    ClientPlayerEntity player = Minecraft.getInstance().player;

    if (player != null) {
      return player.world.getRecipeManager();
    }

    return null;
  }
}
