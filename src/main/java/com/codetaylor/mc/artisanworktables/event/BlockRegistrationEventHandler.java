package com.codetaylor.mc.artisanworktables.event;

import com.codetaylor.mc.artisanworktables.api.EnumTier;
import com.codetaylor.mc.artisanworktables.api.EnumType;
import com.codetaylor.mc.artisanworktables.block.WorkstationBlock;
import com.codetaylor.mc.artisanworktables.block.WorktableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
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

    IForgeRegistry<Block> registry = event.getRegistry();

    this.register(registry, Material.CARPET, ToolType.PICKAXE, SoundType.CLOTH, EnumType.TAILOR.getName());
    this.register(registry, Material.WOOD, ToolType.AXE, SoundType.WOOD, EnumType.CARPENTER.getName());
    this.register(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.MASON.getName());
    this.register(registry, Material.IRON, ToolType.PICKAXE, SoundType.ANVIL, EnumType.BLACKSMITH.getName());
    this.register(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.JEWELER.getName());
    this.register(registry, Material.WOOD, ToolType.AXE, SoundType.WOOD, EnumType.BASIC.getName());
    this.register(registry, Material.IRON, ToolType.PICKAXE, SoundType.ANVIL, EnumType.ENGINEER.getName());
    this.register(registry, Material.CARPET, ToolType.PICKAXE, SoundType.ANCIENT_DEBRIS, EnumType.MAGE.getName());
    this.register(registry, Material.WOOD, ToolType.AXE, SoundType.WOOD, EnumType.SCRIBE.getName());
    this.register(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.CHEMIST.getName());
    this.register(registry, Material.EARTH, ToolType.SHOVEL, SoundType.GROUND, EnumType.FARMER.getName());
    this.register(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.CHEF.getName());
    this.register(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.DESIGNER.getName());
    this.register(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.TANNER.getName());
    this.register(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.POTTER.getName());
  }

  private void register(IForgeRegistry<Block> registry, Material material, ToolType toolType, SoundType soundType, String registryName) {

    this.register(
        registry,
        EnumTier.WORKTABLE.getName() + "_" + registryName,
        new WorktableBlock(material, toolType, soundType, 2.0f, 3.0f)
    );
    this.register(
        registry,
        EnumTier.WORKSTATION.getName() + "_" + registryName,
        new WorkstationBlock(material, toolType, soundType, 3.0f, 4.0f)
    );
  }

  private void register(IForgeRegistry<Block> registry, String registryName, Block block) {

    block.setRegistryName(registryName);
    registry.register(block);
    this.registeredWorktables.add(block);
  }
}
