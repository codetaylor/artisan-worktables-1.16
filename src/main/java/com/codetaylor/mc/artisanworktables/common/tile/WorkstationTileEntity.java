package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.tileentity.TileEntity;

public class WorkstationTileEntity
    extends TileEntity {

  public static final String NAME = "workstation";

  public WorkstationTileEntity() {

    super(ArtisanWorktablesMod.TileEntityTypes.WORKSTATION_TILE_ENTITY_TYPE);
  }
}
