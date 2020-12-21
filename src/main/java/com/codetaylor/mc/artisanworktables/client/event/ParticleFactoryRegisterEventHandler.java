package com.codetaylor.mc.artisanworktables.client.event;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.particle.MageParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ParticleFactoryRegisterEventHandler {

  @SubscribeEvent
  public void on(ParticleFactoryRegisterEvent event) {

    ParticleManager particleManager = Minecraft.getInstance().particles;
    particleManager.registerFactory(ArtisanWorktablesMod.ParticleTypes.MAGE, MageParticle.Factory::new);
  }
}