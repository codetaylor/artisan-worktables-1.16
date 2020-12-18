package com.codetaylor.mc.artisanworktables.common.block;

import com.codetaylor.mc.artisanworktables.client.screen.element.GuiElementTabs;
import com.codetaylor.mc.artisanworktables.common.container.ContainerProvider;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.util.FluidHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class BaseBlock
    extends Block {

  private final EnumType type;

  protected BaseBlock(EnumType type, Material material, ToolType toolType, SoundType soundType, float hardness, float resistance) {

    super(Properties.create(material)
        .harvestTool(toolType)
        .harvestLevel(0)
        .sound(soundType)
        .hardnessAndResistance(hardness, resistance)
        .notSolid()
    );

    this.type = type;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {

    return true;
  }

  public EnumType getType() {

    return this.type;
  }

  protected abstract EnumTier getTier();

  @Nonnull
  @ParametersAreNonnullByDefault
  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {

    if (world.isRemote) {
      GuiElementTabs.RECALCULATE_TAB_OFFSETS = true;

    } else {

      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof BaseTileEntity) {

        FluidTank tank = ((BaseTileEntity) tileEntity).getTank();

        if (FluidHelper.drainWaterFromBottle(player, tank)
            || FluidHelper.drainWaterIntoBottle(player, tank)
            || FluidUtil.interactWithFluidHandler(player, hand, tank)) {

          System.out.println(tank.getFluid().getDisplayName().getString() + " " + tank.getFluid().getAmount());
          return ActionResultType.SUCCESS;
        }

        ContainerProvider containerProvider = new ContainerProvider(this.getTier(), this.getType(), world, pos);
        NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());

      } else {
        throw new IllegalStateException("Invalid tile entity found!");
      }
    }

    return ActionResultType.SUCCESS;
  }
}
