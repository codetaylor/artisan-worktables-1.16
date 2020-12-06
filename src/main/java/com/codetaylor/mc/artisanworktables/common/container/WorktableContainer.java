package com.codetaylor.mc.artisanworktables.common.container;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.tile.WorktableTileEntity;
import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorktableContainer
    extends ContainerBase {

  public static final String NAME = "worktable";

  private final World world;
  private final BlockPos blockPos;
  private final PlayerEntity player;
  private final WorktableTileEntity tile;

  public WorktableContainer(int id, World world, BlockPos blockPos, PlayerInventory playerInventory, PlayerEntity player) {

    super(ArtisanWorktablesMod.ContainerTypes.WORKTABLE_CONTAINER_TYPE, id, playerInventory);
    this.world = world;
    this.blockPos = blockPos;
    this.player = player;

    TileEntity tileEntity = this.world.getTileEntity(this.blockPos);

    if (tileEntity instanceof WorktableTileEntity) {
      this.tile = (WorktableTileEntity) tileEntity;

    } else {
      this.tile = null;
    }
  }

  public WorktableTileEntity getTile() {

    return this.tile;
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {

    return true;
  }
}
