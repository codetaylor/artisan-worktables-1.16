package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.common.container.*;
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
    }).setRegistryName(WorktableContainer.NAME));

    registry.register(IForgeContainerType.create((id, playerInventory, data) -> {
      BlockPos blockPos = data.readBlockPos();
      PlayerEntity player = playerInventory.player;
      World world = player.world;
      return new WorkstationContainer(id, world, blockPos, playerInventory, player);
    }).setRegistryName(WorkstationContainer.NAME));

    registry.register(IForgeContainerType.create((id, playerInventory, data) -> {
      BlockPos blockPos = data.readBlockPos();
      PlayerEntity player = playerInventory.player;
      World world = player.world;
      return new WorkshopContainer(id, world, blockPos, playerInventory, player);
    }).setRegistryName(WorkshopContainer.NAME));

    registry.register(IForgeContainerType.create((id, playerInventory, data) -> {
      BlockPos blockPos = data.readBlockPos();
      PlayerEntity player = playerInventory.player;
      World world = player.world;
      return new ToolboxContainer(id, world, blockPos, playerInventory, player);
    }).setRegistryName(ToolboxContainer.NAME));

    registry.register(IForgeContainerType.create((id, playerInventory, data) -> {
      BlockPos blockPos = data.readBlockPos();
      PlayerEntity player = playerInventory.player;
      World world = player.world;
      return new ToolboxMechanicalContainer(id, world, blockPos, playerInventory, player);
    }).setRegistryName(ToolboxMechanicalContainer.NAME));
  }
}
