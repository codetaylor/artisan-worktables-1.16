package com.codetaylor.mc.artisanworktables.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.athenaeum.inventory.spi.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.ITileData;
import com.codetaylor.mc.athenaeum.network.spi.tile.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.TileDataItemStackHandler;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class WorktableTileEntity
    extends TileEntityDataBase {

  public static final String NAME = "worktable";

  private final CraftingStackHandler craftingStackHandler;
  private final TileDataItemStackHandler<CraftingStackHandler> craftingStackHandlerData;

  public WorktableTileEntity() {

    super(
        ArtisanWorktablesMod.TileEntityTypes.WORKTABLE_TILE_ENTITY_TYPE,
        ArtisanWorktablesMod.tileDataService
    );

    this.craftingStackHandler = new CraftingStackHandler(3 * 3);
    this.craftingStackHandlerData = new TileDataItemStackHandler<>(this.craftingStackHandler);

    this.registerTileDataForNetwork(new ITileData[]{
        this.craftingStackHandlerData
    });
  }

  // ---------------------------------------------------------------------------
  // Serialization
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Override
  public void read(BlockState state, CompoundNBT nbt) {

    super.read(state, nbt);
    this.craftingStackHandler.deserializeNBT(nbt.getCompound("craftingStackHandler"));
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public CompoundNBT write(CompoundNBT compound) {

    super.write(compound);
    compound.put("craftingStackHandler", this.craftingStackHandler.serializeNBT());
    return compound;
  }

  // ---------------------------------------------------------------------------
  // Data
  // ---------------------------------------------------------------------------

  private static class CraftingStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public CraftingStackHandler(int size) {

      super(size);
    }
  }
}
