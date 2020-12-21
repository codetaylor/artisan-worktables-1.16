package com.codetaylor.mc.artisanworktables.common.block;

import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.tile.WorkstationTileEntity;
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

public class WorkstationBlock
    extends BaseBlock {

  public static final VoxelShape VOXEL_SHAPE = VoxelShapes.or(
      Block.makeCuboidShape(0, 8, 0, 16, 16, 16), // top
      Block.makeCuboidShape(1, 7, 1, 15, 8, 15), // inset
      Block.makeCuboidShape(3, 4, 3, 13, 7, 13), // leg
      Block.makeCuboidShape(1, 0, 1, 15, 4, 15) // bottom
  );

  public WorkstationBlock(EnumType type, Material material, ToolType toolType, SoundType soundType, float hardness, float resistance) {

    super(type, material, toolType, soundType, hardness, resistance);
  }

  @Override
  protected EnumTier getTier() {

    return EnumTier.WORKSTATION;
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

    return new WorkstationTileEntity(this.getType());
  }
}
