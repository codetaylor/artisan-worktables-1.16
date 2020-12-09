package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface IRecipeMatrixMatcher {

  IRecipeMatrixMatcher SHAPED = new IRecipeMatrixMatcher() {

    @Override
    public boolean matches(
        IArtisanRecipe recipe,
        ICraftingMatrixStackHandler craftingMatrix,
        @Nullable FluidStack fluidStack
    ) {

      int width = recipe.getWidth();
      int height = recipe.getHeight();
      boolean mirrored = recipe.isMirrored();
      List<IArtisanIngredient> ingredients = recipe.getIngredientList();
      FluidStack fluidIngredient = recipe.getFluidIngredient();

      if (fluidIngredient != null) {

        if (fluidStack == null || !fluidStack.containsFluid(fluidIngredient)) {
          return false;
        }
      }

      for (int x = 0; x <= craftingMatrix.getWidth() - width; ++x) {

        for (int y = 0; y <= craftingMatrix.getHeight() - height; ++y) {

          if (this.checkMatch(ingredients, craftingMatrix, x, y, width, height, false)) {
            return true;
          }

          if (mirrored && this.checkMatch(ingredients, craftingMatrix, x, y, width, height, true)) {
            return true;
          }
        }
      }

      return false;
    }

    private boolean checkMatch(
        List<IArtisanIngredient> ingredients,
        ICraftingMatrixStackHandler craftingMatrix,
        int startX,
        int startY,
        int width,
        int height,
        boolean mirror
    ) {

      for (int x = 0; x < craftingMatrix.getWidth(); ++x) {

        for (int y = 0; y < craftingMatrix.getHeight(); ++y) {

          int subX = x - startX;
          int subY = y - startY;
          IArtisanIngredient ingredient = ArtisanIngredient.EMPTY;

          if (subX >= 0 && subY >= 0 && subX < width && subY < height) {

            if (mirror) {
              ingredient = ingredients.get(width - subX - 1 + subY * width);

            } else {
              ingredient = ingredients.get(subX + subY * width);
            }
          }

          if (!ingredient.matches(craftingMatrix.getStackInSlot(x + y * craftingMatrix.getWidth()))) {
            return false;
          }
        }
      }

      return true;
    }
  };

  IRecipeMatrixMatcher SHAPELESS = (recipe, craftingMatrix, fluidStack) -> {

    int count = 0;
    List<ItemStack> itemList = new ArrayList<>();
    List<IArtisanIngredient> ingredients = recipe.getIngredientList();
    FluidStack fluidIngredient = recipe.getFluidIngredient();

    if (fluidIngredient != null) {

      if (fluidStack == null || !fluidStack.containsFluid(fluidIngredient)) {
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

    List<Ingredient> ingredientList = new ArrayList<>(ingredients.size());

    for (IArtisanIngredient ingredient : ingredients) {
      ingredientList.add(ingredient.toIngredient());
    }

    return RecipeMatcher.findMatches(itemList, ingredientList) != null;
  };

  boolean matches(
      IArtisanRecipe recipe,
      ICraftingMatrixStackHandler craftingMatrix,
      @Nullable FluidStack fluidStack
  );
}
