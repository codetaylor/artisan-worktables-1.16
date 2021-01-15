package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.ArtisanWorktablesModCommonConfig;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;

public class WorktableTileEntity
    extends BaseTileEntity {

  public static final String NAME = "worktable";

  public WorktableTileEntity() {
    // serialization
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKTABLE,
        ArtisanWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorktableTileEntity(EnumType type) {

    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKTABLE,
        ArtisanWorktablesMod.getProxy().getTileDataService(),
        type
    );
  }

  // ---------------------------------------------------------------------------
  // Implementation
  // ---------------------------------------------------------------------------

  @Override
  public EnumTier getTableTier() {

    return EnumTier.WORKTABLE;
  }

  @Override
  protected int getMaxToolCount() {

    return 1;
  }

  @Override
  protected int getFluidTankCapacity() {

    return ArtisanWorktablesModCommonConfig.fluidCapacityWorktable;
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
