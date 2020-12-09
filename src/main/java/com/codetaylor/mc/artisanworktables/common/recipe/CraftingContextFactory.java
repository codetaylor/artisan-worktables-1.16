package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.api.EnumTier;
import com.codetaylor.mc.artisanworktables.api.EnumType;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.Optional;

public class CraftingContextFactory {

  public static ICraftingContext createContext(
      final BaseTileEntity tile,
      final PlayerEntity player,
      @Nullable final IItemHandlerModifiable secondaryIngredientHandler
  ) {

    return new ICraftingContext() {

      @Override
      public World getWorld() {

        return tile.getWorld();
      }

      @Override
      public Optional<PlayerEntity> getPlayer() {

        return Optional.of(player);
      }

      @Override
      public ICraftingMatrixStackHandler getCraftingMatrixStackHandler() {

        return tile.getCraftingMatrixHandler();
      }

      @Override
      public IItemHandlerModifiable getToolHandler() {

        return tile.getToolHandler();
      }

      @Override
      public IItemHandler getSecondaryOutputHandler() {

        return tile.getSecondaryOutputHandler();
      }

      @Nullable
      @Override
      public IItemHandlerModifiable getSecondaryIngredientHandler() {

        return secondaryIngredientHandler;
      }

      @Override
      public IFluidHandler getFluidHandler() {

        return tile.getTank();
      }

      @Nullable
      @Override
      public IItemHandler getToolReplacementHandler() {

        // TODO
//        TileEntityToolbox adjacentToolbox = tile.getAdjacentToolbox();
//
//        if (!(adjacentToolbox instanceof TileEntityMechanicalToolbox)) {
//          return null;
//        }
//
//        return adjacentToolbox.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        return null;
      }

      @Override
      public EnumType getType() {

        return tile.getTableType();
      }

      @Override
      public EnumTier getTier() {

        return tile.getTableTier();
      }

      @Override
      public BlockPos getPosition() {

        return tile.getPos();
      }
    };

  }

  private CraftingContextFactory() {
    //
  }

}
