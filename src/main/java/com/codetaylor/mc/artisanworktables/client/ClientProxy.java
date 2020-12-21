package com.codetaylor.mc.artisanworktables.client;

import com.codetaylor.mc.artisanworktables.client.event.ClientSetupEventHandler;
import com.codetaylor.mc.artisanworktables.client.event.ParticleFactoryRegisterEventHandler;
import com.codetaylor.mc.artisanworktables.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.IEventBus;

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
}
