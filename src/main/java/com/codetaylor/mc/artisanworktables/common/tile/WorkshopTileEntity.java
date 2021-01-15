package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;

public class WorkshopTileEntity
    extends SecondaryInputBaseTileEntity {

  public static final String NAME = "workshop";

  public WorkshopTileEntity() {
    // serialization
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSHOP,
        ArtisanWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorkshopTileEntity(EnumType type) {

    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSHOP,
        ArtisanWorktablesMod.getProxy().getTileDataService(),
        type
    );
  }

  // ---------------------------------------------------------------------------
  // Implementation
  // ---------------------------------------------------------------------------

  @Override
  public EnumTier getTableTier() {

    return EnumTier.WORKSHOP;
  }

  @Override
  protected int getMaxToolCount() {

    return 3;
  }

  @Override
  protected int getFluidTankCapacity() {

    return ArtisanWorktablesModCommonConfig.fluidCapacityWorkshop;
  }

  @Override
  protected int getCraftingMatrixWidth() {

    return 5;
  }

  @Override
  protected int getCraftingMatrixHeight() {

    return 5;
  }
}
