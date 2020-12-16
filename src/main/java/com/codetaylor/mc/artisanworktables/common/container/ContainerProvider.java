package com.codetaylor.mc.artisanworktables.common.container;

import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ContainerProvider
    implements INamedContainerProvider {

  private final EnumTier tier;
  private final EnumType type;
  private final World world;
  private final BlockPos pos;

  public ContainerProvider(EnumTier tier, EnumType type, World world, BlockPos pos) {

    this.tier = tier;
    this.type = type;
    this.world = world;
    this.pos = pos;
  }

  @Nonnull
  @Override
  public ITextComponent getDisplayName() {

    return new TranslationTextComponent("block.artisanworktables." + this.tier.getName() + "_" + this.type.getName());
  }

  @Override
  public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {

    switch (this.tier) {

      case WORKTABLE:
        return new WorktableContainer(id, this.world, this.pos, playerInventory, playerEntity);

      case WORKSTATION:
        return new WorkstationContainer(id, this.world, this.pos, playerInventory, playerEntity);

      case WORKSHOP:
        return new WorkshopContainer(id, this.world, this.pos, playerInventory, playerEntity);

      default:
        throw new IllegalArgumentException("Unknown table tier: " + this.tier.getName());
    }
  }
}