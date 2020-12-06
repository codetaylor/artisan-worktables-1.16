package com.codetaylor.mc.artisanworktables.block;

import com.codetaylor.mc.artisanworktables.tile.WorktableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class WorktableBlock
    extends WorkBlock {

  private static final VoxelShape VOXEL_SHAPE = VoxelShapes.or(
      Block.makeCuboidShape(0, 7, 0, 16, 15, 16), // top
      Block.makeCuboidShape(1, 6, 1, 15, 7, 15), // inset
      Block.makeCuboidShape(2, 2, 2, 4, 6, 4), // leg a
      Block.makeCuboidShape(2, 2, 12, 4, 6, 14), // leg b
      Block.makeCuboidShape(12, 2, 2, 14, 6, 4), // leg c
      Block.makeCuboidShape(12, 2, 12, 14, 6, 14), // leg d
      Block.makeCuboidShape(1, 0, 1, 5, 2, 5), // foot a
      Block.makeCuboidShape(1, 0, 11, 5, 2, 15), // foot b
      Block.makeCuboidShape(11, 0, 1, 15, 2, 5), // foot c
      Block.makeCuboidShape(11, 0, 11, 15, 2, 15), // foot d
      Block.makeCuboidShape(3, 3, 4, 4, 4, 12), // brace a
      Block.makeCuboidShape(12, 3, 4, 13, 4, 12), // brace b
      Block.makeCuboidShape(4, 3, 3, 12, 4, 4), // brace c
      Block.makeCuboidShape(4, 3, 12, 12, 4, 13) // brace d
  );

  public WorktableBlock(Material material, ToolType toolType, SoundType soundType, float hardness, float resistance) {

    super(material, toolType, soundType, hardness, resistance);
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {

    return VOXEL_SHAPE;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {

    return new WorktableTileEntity();
  }
}
