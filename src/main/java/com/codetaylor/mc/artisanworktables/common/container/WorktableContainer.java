package com.codetaylor.mc.artisanworktables.common.container;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorktableContainer
    extends BaseContainer {

  public static final String NAME = "worktable";

  public WorktableContainer(int id, World world, BlockPos blockPos, PlayerInventory playerInventory, PlayerEntity player) {

    super(ArtisanWorktablesMod.ContainerTypes.WORKTABLE, id, world, blockPos, playerInventory, player);
  }
}
