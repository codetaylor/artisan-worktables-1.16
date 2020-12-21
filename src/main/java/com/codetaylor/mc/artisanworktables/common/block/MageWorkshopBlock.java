package com.codetaylor.mc.artisanworktables.common.block;

import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.tile.WorkshopTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class MageWorkshopBlock
    extends MageBaseBlock {

  public MageWorkshopBlock(Material material, ToolType toolType, SoundType soundType, float hardness, float resistance) {

    super(EnumType.MAGE, material, toolType, soundType, hardness, resistance);
  }

  @Override
  protected EnumTier getTier() {

    return EnumTier.WORKSHOP;
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {

    return WorkshopBlock.VOXEL_SHAPE;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {

    return new WorkshopTileEntity(this.getType());
  }

}
