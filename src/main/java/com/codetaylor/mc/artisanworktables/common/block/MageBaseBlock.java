package com.codetaylor.mc.artisanworktables.common.block;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.artisanworktables.common.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public abstract class MageBaseBlock
    extends BaseBlock {

  protected MageBaseBlock(EnumType type, Material material, ToolType toolType, SoundType soundType, float hardness, float resistance) {

    super(type, material, toolType, soundType, hardness, resistance);
  }

  @ParametersAreNonnullByDefault
  @OnlyIn(Dist.CLIENT)
  @Override
  public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof BaseTileEntity) {

      ItemStackHandler toolHandler = ((BaseTileEntity) tileEntity).getToolHandler();

      boolean hasTool = false;

      for (int i = 0; i < toolHandler.getSlots(); i++) {

        if (!toolHandler.getStackInSlot(i).isEmpty()) {
          hasTool = true;
          break;
        }
      }

      if (hasTool) {
        ParticleManager particleManager = Minecraft.getInstance().particles;

        particleManager.addParticle(
            ParticleTypes.PORTAL,
            pos.getX() + 0.5 + Util.RANDOM.nextFloat() * 0.5 - 0.25,
            pos.getY() + 0.5,
            pos.getZ() + 0.5 + Util.RANDOM.nextFloat() * 0.5 - 0.25,
            0,
            Util.RANDOM.nextFloat(),
            0
        );

        particleManager.addParticle(
            ArtisanWorktablesMod.ParticleTypes.MAGE,
            pos.getX() + 0.5 + Util.RANDOM.nextFloat() * 0.5 - 0.25,
            pos.getY() + 1.5,
            pos.getZ() + 0.5 + Util.RANDOM.nextFloat() * 0.5 - 0.25,
            0,
            Util.RANDOM.nextFloat() * 0.5,
            0
        );
      }
    }
  }
}