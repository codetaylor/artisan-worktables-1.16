package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;

public class WorkstationTileEntity
    extends BaseTileEntity {

  public static final String NAME = "workstation";

  public WorkstationTileEntity() {
    // serialization
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSTATION,
        ArtisanWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorkstationTileEntity(EnumType type) {

    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSTATION,
        ArtisanWorktablesMod.getProxy().getTileDataService(),
        type
    );
  }

  // ---------------------------------------------------------------------------
  // Implementation
  // ---------------------------------------------------------------------------

  @Override
  public EnumTier getTableTier() {

    return EnumTier.WORKSTATION;
  }

  @Override
  protected int getMaxToolCount() {

    return 2;
  }

  @Override
  protected int getFluidTankCapacity() {

    // TODO
    return 4000;
  }

  @Override
  protected int getCraftingMatrixWidth() {

    return 3;
  }

  @Override
  protected int getCraftingMatrixHeight() {

    return 3;
  }
}
