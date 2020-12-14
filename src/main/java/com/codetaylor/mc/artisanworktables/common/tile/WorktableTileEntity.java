package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.container.WorktableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

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

    // TODO
    return 2000;
  }

  @Override
  protected int getCraftingMatrixWidth() {

    return 3;
  }

  @Override
  protected int getCraftingMatrixHeight() {

    return 3;
  }

  @Nullable
  @ParametersAreNonnullByDefault
  @Override
  public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {

    return new WorktableContainer(id, this.world, this.pos, playerInventory, playerEntity);
  }
}
