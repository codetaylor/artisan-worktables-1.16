package com.codetaylor.mc.artisanworktables.event;

import com.codetaylor.mc.artisanworktables.api.EnumTier;
import com.codetaylor.mc.artisanworktables.api.EnumType;
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

    this.registerWorktables(registry, Material.CARPET, ToolType.PICKAXE, SoundType.CLOTH, EnumType.TAILOR.getName());
    this.registerWorktables(registry, Material.WOOD, ToolType.AXE, SoundType.WOOD, EnumType.CARPENTER.getName());
    this.registerWorktables(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.MASON.getName());
    this.registerWorktables(registry, Material.IRON, ToolType.PICKAXE, SoundType.ANVIL, EnumType.BLACKSMITH.getName());
    this.registerWorktables(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.JEWELER.getName());
    this.registerWorktables(registry, Material.WOOD, ToolType.AXE, SoundType.WOOD, EnumType.BASIC.getName());
    this.registerWorktables(registry, Material.IRON, ToolType.PICKAXE, SoundType.ANVIL, EnumType.ENGINEER.getName());
    this.registerWorktables(registry, Material.CARPET, ToolType.PICKAXE, SoundType.ANCIENT_DEBRIS, EnumType.MAGE.getName());
    this.registerWorktables(registry, Material.WOOD, ToolType.AXE, SoundType.WOOD, EnumType.SCRIBE.getName());
    this.registerWorktables(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.CHEMIST.getName());
    this.registerWorktables(registry, Material.EARTH, ToolType.SHOVEL, SoundType.GROUND, EnumType.FARMER.getName());
    this.registerWorktables(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.CHEF.getName());
    this.registerWorktables(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.DESIGNER.getName());
    this.registerWorktables(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.TANNER.getName());
    this.registerWorktables(registry, Material.ROCK, ToolType.PICKAXE, SoundType.STONE, EnumType.POTTER.getName());
  }

  private void registerWorktables(IForgeRegistry<Block> registry, Material material, ToolType toolType, SoundType soundType, String registryName) {

    this.registerWorktable(
        registry,
        EnumTier.WORKTABLE.getName() + "_" + registryName,
        new WorktableBlock(material, toolType, soundType, 2.0f, 3.0f)
    );
  }

  private void registerWorktable(IForgeRegistry<Block> registry, String registryName, Block block) {

    block.setRegistryName(registryName);
    registry.register(block);
    this.registeredWorktables.add(block);
  }
}
