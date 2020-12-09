package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.api.EnumTier;
import com.codetaylor.mc.artisanworktables.api.EnumType;
import com.codetaylor.mc.artisanworktables.common.container.WorkstationContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class WorkstationTileEntity
    extends BaseTileEntity {

  public static final String NAME = "workstation";

  public WorkstationTileEntity() {
    // serialization
    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSTATION_TILE_ENTITY_TYPE,
        ArtisanWorktablesMod.getProxy().getTileDataService()
    );
  }

  public WorkstationTileEntity(EnumType type) {

    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKSTATION_TILE_ENTITY_TYPE,
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

  @Nullable
  @ParametersAreNonnullByDefault
  @Override
  public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {

    return new WorkstationContainer(id, this.world, this.pos, playerInventory, playerEntity);
  }
}
