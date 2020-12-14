package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public interface IRecipeMatrixMatcher {

  IRecipeMatrixMatcher SHAPELESS = new IRecipeMatrixMatcher() {

    @Override
    public boolean matches(ArtisanRecipe recipe, ICraftingMatrixStackHandler craftingMatrix, FluidStack fluidStack) {

      int count = 0;
      List<ItemStack> itemList = new ArrayList<>();
      List<Ingredient> ingredients = recipe.getIngredients();
      FluidStack fluidIngredient = recipe.getFluidIngredient();

      if (fluidIngredient != FluidStack.EMPTY) {

        if (!fluidStack.containsFluid(fluidIngredient)) {
          return false;
        }
      }

      for (int i = 0; i < craftingMatrix.getSlots(); i++) {
        ItemStack itemStack = craftingMatrix.getStackInSlot(i);

        if (!itemStack.isEmpty()) {
          count += 1;
          itemList.add(itemStack);
        }
      }

      if (count != ingredients.size()) {
        return false;
      }

      return RecipeMatcher.findMatches(itemList, ingredients) != null;
    }

    @Override
    public IRecipeType<?> getType() {

      return ArtisanWorktablesMod.RecipeTypes.SHAPELESS;
    }
  };

  boolean matches(
      ArtisanRecipe recipe,
      ICraftingMatrixStackHandler craftingMatrix,
      FluidStack fluidStack
  );

  IRecipeType<?> getType();
}
