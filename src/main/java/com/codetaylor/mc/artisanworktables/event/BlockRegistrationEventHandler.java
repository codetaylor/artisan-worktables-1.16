package com.codetaylor.mc.artisanworktables.event;

import com.codetaylor.mc.artisanworktables.block.BasicWorktableBlock;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

public class BlockRegistrationEventHandler {

  private final List<Block> registeredWorktables;

  public BlockRegistrationEventHandler(List<Block> registeredWorktables) {

    this.registeredWorktables = registeredWorktables;
  }

  @SubscribeEvent
  public void on(RegistryEvent.Register<Block> event) {

    this.registerWorktables(
        event.getRegistry(),
        new BasicWorktableBlock()
    );
  }

  private void registerWorktables(IForgeRegistry<Block> registry, Block... blocks) {

    for (Block block : blocks) {
      registry.register(block);
      this.registeredWorktables.add(block);
    }
  }
}
