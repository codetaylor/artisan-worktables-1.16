package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class ArtisanRecipeShaped
    extends ArtisanRecipe {

  protected final boolean mirrored;
  protected final int width;
  protected final int height;

  public ArtisanRecipeShaped(
      ResourceLocation recipeId,
      String group,
      ToolEntry[] tools,
      ItemStack result,
      NonNullList<Ingredient> ingredients,
      List<Ingredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
      FluidStack fluidIngredient,
      ExtraOutputChancePair[] extraOutputs,
      boolean mirrored,
      int minimumTier,
      int maximumTier,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience,
      int width,
      int height
  ) {

    super(recipeId, group, tools, result, ingredients, secondaryIngredients, consumeSecondaryIngredients, fluidIngredient, extraOutputs, minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience);
    this.mirrored = mirrored;
    this.width = width;
    this.height = height;
  }

  public boolean isMirrored() {

    return this.mirrored;
  }

  public int getWidth() {

    return this.width;
  }

  public int getHeight() {

    return this.height;
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {

    return ArtisanWorktablesMod.RecipeSerializers.SHAPED;
  }

  @Nonnull
  @Override
  public IRecipeType<?> getType() {

    return ArtisanWorktablesMod.RecipeTypes.SHAPED;
  }

  @Override
  public boolean matches(@Nonnull ArtisanInventory inventory, @Nonnull World world) {

    ICraftingMatrixStackHandler craftingMatrix = inventory.getCraftingMatrix();
    FluidStack fluidStack = inventory.getFluidStack();

    if (this.fluidIngredient != FluidStack.EMPTY) {

      if (!fluidStack.containsFluid(this.fluidIngredient)) {
        return false;
      }
    }

    for (int x = 0; x <= craftingMatrix.getWidth() - this.width; ++x) {

      for (int y = 0; y <= craftingMatrix.getHeight() - this.height; ++y) {

        if (this.checkMatch(this.ingredients, craftingMatrix, x, y, this.width, this.height, false)) {
          return true;
        }

        if (this.mirrored && this.checkMatch(this.ingredients, craftingMatrix, x, y, this.width, this.height, true)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean checkMatch(
      List<Ingredient> ingredients,
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
        Ingredient ingredient = Ingredient.EMPTY;

        if (subX >= 0 && subY >= 0 && subX < width && subY < height) {

          if (mirror) {
            ingredient = ingredients.get(width - subX - 1 + subY * width);

          } else {
            ingredient = ingredients.get(subX + subY * width);
          }
        }

        if (!ingredient.test(craftingMatrix.getStackInSlot(x + y * craftingMatrix.getWidth()))) {
          return false;
        }
      }
    }

    return true;
  }
}
