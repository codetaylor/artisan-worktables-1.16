package com.codetaylor.mc.artisanworktables.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public abstract class ArtisanRecipe
    implements IRecipe<ArtisanInventory> {

  protected final ResourceLocation recipeId;
  protected final String group;
  protected final NonNullList<ToolEntry> tools;
  protected final ItemStack result;
  protected final NonNullList<Ingredient> ingredients;
  protected final NonNullList<Ingredient> secondaryIngredients;
  protected final boolean consumeSecondaryIngredients;
  protected final FluidStack fluidIngredient;
  protected final NonNullList<ExtraOutputChancePair> extraOutputs;
  protected final int minimumTier;
  protected final int maximumTier;
  protected final int experienceRequired;
  protected final int levelRequired;
  protected final boolean consumeExperience;

  /* package */ ArtisanRecipe(
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
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  public ResourceLocation getRecipeId() {

    return this.recipeId;
  }

  public NonNullList<ToolEntry> getTools() {

    return this.tools;
  }

  @Nonnull
  @Override
  public NonNullList<Ingredient> getIngredients() {

    return this.ingredients;
  }

  public NonNullList<Ingredient> getSecondaryIngredients() {

    return this.secondaryIngredients;
  }

  public boolean consumeSecondaryIngredients() {

    return this.consumeSecondaryIngredients;
  }

  public FluidStack getFluidIngredient() {

    return this.fluidIngredient;
  }

  public NonNullList<ExtraOutputChancePair> getExtraOutputs() {

    return this.extraOutputs;
  }

  public int getMinimumTier() {

    return this.minimumTier;
  }

  public int getMaximumTier() {

    return this.maximumTier;
  }

  public int getExperienceRequired() {

    return this.experienceRequired;
  }

  public int getLevelRequired() {

    return this.levelRequired;
  }

  public boolean consumeExperience() {

    return this.consumeExperience;
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
