package com.codetaylor.mc.artisanworktables.common.block;

import com.codetaylor.mc.artisanworktables.client.screen.element.GuiElementTabs;
import com.codetaylor.mc.artisanworktables.common.container.ToolboxContainerProvider;
import com.codetaylor.mc.artisanworktables.common.tile.ToolboxTileEntity;
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
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class ToolboxBaseBlock
    extends Block {

  public static final VoxelShape VOXEL_SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 14, 15);

  public ToolboxBaseBlock(float hardness, float resistance) {

    super(Properties.create(Material.WOOD)
        .harvestTool(ToolType.AXE)
        .harvestLevel(0)
        .sound(SoundType.WOOD)
        .hardnessAndResistance(hardness, resistance)
        .notSolid()
    );
  }

  @ParametersAreNonnullByDefault
  @Nonnull
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {

    return VOXEL_SHAPE;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {

    return true;
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {

    if (world.isRemote) {
      GuiElementTabs.RECALCULATE_TAB_OFFSETS = true;

    } else {

      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof ToolboxTileEntity) {

        ToolboxContainerProvider containerProvider = new ToolboxContainerProvider(world, pos);
        NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());

      } else {
        throw new IllegalStateException("Invalid tile entity found!");
      }
    }

    return ActionResultType.SUCCESS;
  }
}
