package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.api.EnumType;
import net.minecraft.tileentity.TileEntity;

public class WorkstationTileEntity
    extends TileEntity {

  public static final String NAME = "workstation";

  public WorkstationTileEntity() {
    // serialization
    super(ArtisanWorktablesMod.TileEntityTypes.WORKSTATION_TILE_ENTITY_TYPE);
  }

  public WorkstationTileEntity(EnumType type) {

    this();
  }
}
