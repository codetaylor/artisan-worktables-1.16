package com.codetaylor.mc.artisanworktables.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ArtisanRecipe
    implements IRecipe<ArtisanInventory> {

  protected final ResourceLocation recipeId;
  protected final String group;
  protected final ToolEntry[] tools;
  protected final ItemStack result;
  protected final NonNullList<Ingredient> ingredients;
  protected final List<Ingredient> secondaryIngredients;
  protected final boolean consumeSecondaryIngredients;
  protected final FluidStack fluidIngredient;
  protected final ExtraOutputChancePair[] extraOutputs;
  protected final int minimumTier;
  protected final int maximumTier;
  protected final int experienceRequired;
  protected final int levelRequired;
  protected final boolean consumeExperience;

  public ArtisanRecipe(
      ResourceLocation recipeId,
      String group,
      ToolEntry[] tools,
      ItemStack result,
      NonNullList<Ingredient> ingredients,
      List<Ingredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
      FluidStack fluidIngredient,
      ExtraOutputChancePair[] extraOutputs,
      int minimumTier,
      int maximumTier,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience
  ) {

    this.recipeId = recipeId;
    this.group = group;
    this.tools = tools;
    this.result = result;
    this.ingredients = ingredients;
    this.secondaryIngredients = secondaryIngredients;
    this.consumeSecondaryIngredients = consumeSecondaryIngredients;
    this.fluidIngredient = fluidIngredient;
    this.extraOutputs = extraOutputs;
    this.minimumTier = minimumTier;
    this.maximumTier = maximumTier;
    this.experienceRequired = experienceRequired;
    this.levelRequired = levelRequired;
    this.consumeExperience = consumeExperience;

    // TODO: calculate min and max tier
    // use width and height for shaped
    // use ingredient list size for shapeless
    // use tool count for both
    // presence of secondary ingredients
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NonNullList<Ingredient> getIngredients() {

    return this.ingredients;
  }

  public FluidStack getFluidIngredient() {

    return this.fluidIngredient;
  }

  // ---------------------------------------------------------------------------
  // Implementation
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public String getGroup() {

    return this.group;
  }

  @Override
  public boolean matches(@Nonnull ArtisanInventory inventory, @Nonnull World world) {

    return false;
  }

  @Nonnull
  @Override
  public ItemStack getCraftingResult(@Nonnull ArtisanInventory inventory) {

    return null;
  }

  @Override
  public boolean canFit(int width, int height) {

    return false;
  }

  @Nonnull
  @Override
  public ItemStack getRecipeOutput() {

    return null;
  }

  @Nonnull
  @Override
  public ResourceLocation getId() {

    return null;
  }

  public static class ExtraOutputChancePair {

    private final ItemStack output;
    private final float chance;

    public ExtraOutputChancePair(ItemStack output, float chance) {

      this.output = output;
      this.chance = chance;
    }

    public ItemStack getOutput() {

      return this.output.copy();
    }

    public float getChance() {

      return this.chance;
    }
  }
}
