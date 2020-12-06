package com.codetaylor.mc.artisanworktables.common.block;

import com.codetaylor.mc.artisanworktables.api.EnumType;
import com.codetaylor.mc.artisanworktables.common.container.WorktableContainer;
import com.codetaylor.mc.artisanworktables.common.tile.WorktableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class WorktableBlock
    extends WorkBlock {

  private static final VoxelShape VOXEL_SHAPE = VoxelShapes.or(
      Block.makeCuboidShape(0, 7, 0, 16, 15, 16), // top
      Block.makeCuboidShape(1, 6, 1, 15, 7, 15), // inset
      Block.makeCuboidShape(2, 2, 2, 4, 6, 4), // leg a
      Block.makeCuboidShape(2, 2, 12, 4, 6, 14), // leg b
      Block.makeCuboidShape(12, 2, 2, 14, 6, 4), // leg c
      Block.makeCuboidShape(12, 2, 12, 14, 6, 14), // leg d
      Block.makeCuboidShape(1, 0, 1, 5, 2, 5), // foot a
      Block.makeCuboidShape(1, 0, 11, 5, 2, 15), // foot b
      Block.makeCuboidShape(11, 0, 1, 15, 2, 5), // foot c
      Block.makeCuboidShape(11, 0, 11, 15, 2, 15), // foot d
      Block.makeCuboidShape(3, 3, 4, 4, 4, 12), // brace a
      Block.makeCuboidShape(12, 3, 4, 13, 4, 12), // brace b
      Block.makeCuboidShape(4, 3, 3, 12, 4, 4), // brace c
      Block.makeCuboidShape(4, 3, 12, 12, 4, 13) // brace d
  );

  public WorktableBlock(EnumType type, Material material, ToolType toolType, SoundType soundType, float hardness, float resistance) {

    super(type, material, toolType, soundType, hardness, resistance);
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {

    return VOXEL_SHAPE;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {

    return new WorktableTileEntity(this.getType());
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {

    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(pos);

      if (tileEntity instanceof WorktableTileEntity) {
        INamedContainerProvider containerProvider = new INamedContainerProvider() {

          @Override
          public ITextComponent getDisplayName() {

            return new TranslationTextComponent("block.artisanworktables.worktable_" + WorktableBlock.this.getType().getName());
          }

          @Override
          public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {

            return new WorktableContainer(i, world, pos, playerInventory, playerEntity);
          }
        };
        NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());

      } else {
        throw new IllegalStateException("Our named container provider is missing!");
      }
    }
    return ActionResultType.SUCCESS;
  }
}
