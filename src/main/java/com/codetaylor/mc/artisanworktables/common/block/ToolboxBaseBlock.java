package com.codetaylor.mc.artisanworktables.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class ToolboxBaseBlock
    extends Block {

  public static final VoxelShape VOXEL_SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 14, 15);

  public ToolboxBaseBlock(float hardness, float resistance) {

    super(Properties.create(Material.WOOD)
        .harvestTool(ToolType.AXE)
        .harvestLevel(0)
        .sound(SoundType.WOOD)
        .hardnessAndResistance(hardness, resistance)
        .notSolid()
    );
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {

    return VOXEL_SHAPE;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {

    return true;
  }
}
