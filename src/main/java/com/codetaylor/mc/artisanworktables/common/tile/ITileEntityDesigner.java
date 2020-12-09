package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public interface ITileEntityDesigner {

  ItemStackHandler getPatternStackHandler();

  TileEntity getTileEntity();

  boolean canPlayerUse(PlayerEntity player);

  Texture getTexturePatternSide();
}
