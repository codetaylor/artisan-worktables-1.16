package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ArtisanRecipeShapeless
    extends ArtisanRecipe {

  public ArtisanRecipeShapeless(
      ResourceLocation recipeId,
      String group,
      NonNullList<ToolEntry> tools,
      ItemStack result,
      NonNullList<Ingredient> ingredients,
      NonNullList<Ingredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
      FluidStack fluidIngredient,
      NonNullList<ExtraOutputChancePair> extraOutputs,
      int minimumTier,
      int maximumTier,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience
  ) {

    super(recipeId, group, tools, result, ingredients, secondaryIngredients, consumeSecondaryIngredients, fluidIngredient, extraOutputs, minimumTier, maximumTier, experienceRequired, levelRequired, consumeExperience);
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {

    return ArtisanWorktablesMod.RecipeSerializers.SHAPELESS;
  }

  @Nonnull
  @Override
  public IRecipeType<?> getType() {

    return ArtisanWorktablesMod.RecipeTypes.SHAPELESS;
  }

  @Override
  public boolean matches(@Nonnull ArtisanInventory inventory, @Nonnull World world) {

    ICraftingMatrixStackHandler craftingMatrix = inventory.getCraftingMatrix();
    FluidStack fluidStack = inventory.getFluidStack();

    int count = 0;
    List<ItemStack> itemList = new ArrayList<>();
    List<Ingredient> ingredients = this.getIngredients();
    FluidStack fluidIngredient = this.getFluidIngredient();

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
}
