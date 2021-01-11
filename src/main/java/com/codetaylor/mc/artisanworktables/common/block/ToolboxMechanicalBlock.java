package com.codetaylor.mc.artisanworktables.common.block;

import com.codetaylor.mc.artisanworktables.common.tile.ToolboxTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ToolboxMechanicalBlock
    extends ToolboxBaseBlock {

  public static final String NAME = "mechanical_toolbox";

  public ToolboxMechanicalBlock() {

    super(4, 12);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {

    return new ToolboxTileEntity();
  }
}
