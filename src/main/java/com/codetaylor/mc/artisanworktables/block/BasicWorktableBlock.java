package com.codetaylor.mc.artisanworktables.block;

import com.codetaylor.mc.artisanworktables.block.spi.WorktableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class BasicWorktableBlock
    extends WorktableBlock {

  public static final String NAME = "worktable_basic";

  public BasicWorktableBlock() {

    super(Properties
        .create(Material.WOOD, MaterialColor.WOOD)
        .hardnessAndResistance(2.0f, 3.0f)
        .harvestTool(ToolType.AXE)
        .harvestLevel(0)
        .notSolid()
        .sound(SoundType.WOOD));
    this.setRegistryName(NAME);
  }

}
