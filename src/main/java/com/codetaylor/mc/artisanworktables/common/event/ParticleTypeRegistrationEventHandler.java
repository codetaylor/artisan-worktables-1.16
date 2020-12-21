package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.common.util.Key;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ParticleTypeRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegistryEvent.Register<ParticleType<?>> event) {

    IForgeRegistry<ParticleType<?>> registry = event.getRegistry();
    registry.register(new BasicParticleType(false).setRegistryName(Key.from("mage")));
  }

}