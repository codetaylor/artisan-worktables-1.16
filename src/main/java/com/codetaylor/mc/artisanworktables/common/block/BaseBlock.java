package com.codetaylor.mc.artisanworktables.common.block;

import com.codetaylor.mc.artisanworktables.api.EnumType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraftforge.common.ToolType;

import java.util.List;

public abstract class BaseBlock
    extends Block {

  private final EnumType type;

  protected BaseBlock(EnumType type, Material material, ToolType toolType, SoundType soundType, float hardness, float resistance) {

    super(Properties.create(material)
        .harvestTool(toolType)
        .harvestLevel(0)
        .sound(soundType)
        .hardnessAndResistance(hardness, resistance)
        .notSolid()
    );

    this.type = type;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {

    return true;
  }

  public EnumType getType() {

    return this.type;
  }
}
