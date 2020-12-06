package com.codetaylor.mc.artisanworktables.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class WorkshopBlock
    extends Block {

  private static final VoxelShape VOXEL_SHAPE = VoxelShapes.or(
      Block.makeCuboidShape(0, 8, 0, 16, 16, 16), // top
      Block.makeCuboidShape(1, 7, 1, 15, 8, 15), // inset
      Block.makeCuboidShape(0, 0, 0, 16, 7, 16) // bottom
  );

  public WorkshopBlock(Material material, ToolType toolType, SoundType soundType, float hardness, float resistance) {

    super(Properties.create(material)
        .harvestTool(toolType)
        .harvestLevel(0)
        .sound(soundType)
        .hardnessAndResistance(hardness, resistance)
        .notSolid()
    );
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {

    return VOXEL_SHAPE;
  }
}
