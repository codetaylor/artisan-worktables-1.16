package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.container.WorkshopContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class WorkshopTileEntity
    extends BaseTileEntity {

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

    // TODO
    return 8000;
  }

  @Override
  protected int getCraftingMatrixWidth() {

    return 5;
  }

  @Override
  protected int getCraftingMatrixHeight() {

    return 5;
  }

  @Nullable
  @ParametersAreNonnullByDefault
  @Override
  public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {

    return new WorkshopContainer(id, this.world, this.pos, playerInventory, playerEntity);
  }
}
