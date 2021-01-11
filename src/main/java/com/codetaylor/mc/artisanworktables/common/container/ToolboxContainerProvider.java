package com.codetaylor.mc.artisanworktables.common.container;

import com.codetaylor.mc.artisanworktables.common.block.ToolboxBaseBlock;
import com.codetaylor.mc.artisanworktables.common.block.ToolboxBlock;
import com.codetaylor.mc.artisanworktables.common.block.ToolboxMechanicalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ToolboxContainerProvider
    implements INamedContainerProvider {

  private final ToolboxBaseBlock block;
  private final World world;
  private final BlockPos pos;

  public ToolboxContainerProvider(World world, BlockPos pos) {

    this.block = (ToolboxBaseBlock) world.getBlockState(pos).getBlock();
    this.world = world;
    this.pos = pos;
  }

  @Nonnull
  @Override
  public ITextComponent getDisplayName() {

    return new TranslationTextComponent("block.artisanworktables." + this.block.getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {

    if (this.block instanceof ToolboxBlock) {
      return new ToolboxContainer(id, this.world, this.pos, playerInventory, playerEntity);

    } else if (this.block instanceof ToolboxMechanicalBlock) {
      return new ToolboxMechanicalContainer(id, this.world, this.pos, playerInventory, playerEntity);

    } else {
      throw new IllegalArgumentException("Unknown block type: " + this.block.getRegistryName());
    }
  }
}