package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.api.EnumType;
import net.minecraft.tileentity.TileEntity;

public class WorkshopTileEntity
    extends TileEntity {

  public static final String NAME = "workshop";

  public WorkshopTileEntity() {
    // serialization
    super(ArtisanWorktablesMod.TileEntityTypes.WORKSHOP_TILE_ENTITY_TYPE);
  }

  public WorkshopTileEntity(EnumType type) {

    this();
  }
}
