package com.codetaylor.mc.artisanworktables.common.container;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolboxMechanicalContainer
    extends ToolboxBaseContainer {

  public static final String NAME = "mechanical_toolbox";

  public ToolboxMechanicalContainer(int id, World world, BlockPos blockPos, PlayerInventory playerInventory, PlayerEntity player) {

    super(ArtisanWorktablesMod.ContainerTypes.MECHANICAL_TOOLBOX, id, world, blockPos, playerInventory, player);
  }
}
