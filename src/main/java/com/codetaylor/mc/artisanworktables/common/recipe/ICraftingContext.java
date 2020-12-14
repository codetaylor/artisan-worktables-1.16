package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public interface ICraftingContext {

  World getWorld();

  Optional<PlayerEntity> getPlayer();

  ICraftingMatrixStackHandler getCraftingMatrixStackHandler();

  IItemHandlerModifiable getToolHandler();

  IItemHandler getSecondaryOutputHandler();

  @Nullable
  IItemHandlerModifiable getSecondaryIngredientHandler();

  IFluidHandler getFluidHandler();

  @Nullable
  IItemHandler getToolReplacementHandler();

  EnumType getType();

  EnumTier getTier();

  BlockPos getPosition();

}
