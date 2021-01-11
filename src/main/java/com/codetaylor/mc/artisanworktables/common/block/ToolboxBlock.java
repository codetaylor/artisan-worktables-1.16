package com.codetaylor.mc.artisanworktables.common.block;

import com.codetaylor.mc.artisanworktables.common.tile.ToolboxTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ToolboxBlock
    extends ToolboxBaseBlock {

  public static final String NAME = "toolbox";

  public ToolboxBlock() {

    super(2, 3);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {

    return new ToolboxTileEntity();
  }
}
