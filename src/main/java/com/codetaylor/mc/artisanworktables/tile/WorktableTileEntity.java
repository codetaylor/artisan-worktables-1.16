package com.codetaylor.mc.artisanworktables.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.tileentity.TileEntity;

public class WorktableTileEntity
    extends TileEntity {

  public static final String NAME = "worktable";

  public WorktableTileEntity() {

    super(ArtisanWorktablesMod.TileEntityTypes.WORKTABLE_TILE_ENTITY_TYPE);
  }
}
