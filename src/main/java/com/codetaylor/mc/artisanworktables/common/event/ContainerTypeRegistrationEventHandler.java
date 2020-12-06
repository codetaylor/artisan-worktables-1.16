package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.api.EnumTier;
import com.codetaylor.mc.artisanworktables.common.container.WorktableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ContainerTypeRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegistryEvent.Register<ContainerType<?>> event) {

    IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

    registry.register(IForgeContainerType.create((id, playerInventory, data) -> {
      BlockPos blockPos = data.readBlockPos();
      PlayerEntity player = playerInventory.player;
      World world = player.world;
      return new WorktableContainer(id, world, blockPos, playerInventory, player);
    }).setRegistryName(EnumTier.WORKTABLE.getName()));
  }
}
