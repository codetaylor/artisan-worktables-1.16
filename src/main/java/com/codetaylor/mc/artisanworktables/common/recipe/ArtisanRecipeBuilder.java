package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

public class ArtisanRecipeBuilder {

  // Common
  private ResourceLocation recipeId;
  private String group;
  private NonNullList<ToolEntry> tools;
  private ItemStack result;
  private NonNullList<Ingredient> ingredients;
  private NonNullList<Ingredient> secondaryIngredients;
  private boolean consumeSecondaryIngredients;
  private FluidStack fluidIngredient;
  private NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs;
  private int minimumTier;
  private int maximumTier;
  private int experienceRequired;
  private int levelRequired;
  private boolean consumeExperience;

  // Shaped only
  private boolean mirrored;
  private int width;
  private int height;

  public ArtisanRecipeBuilder() {

    this.group = "";
    this.tools = NonNullList.create();
    this.result = ItemStack.EMPTY;
    this.ingredients = NonNullList.create();
    this.secondaryIngredients = NonNullList.create();
    this.consumeSecondaryIngredients = true;
    this.fluidIngredient = FluidStack.EMPTY;
    this.extraOutputs = NonNullList.create();
    this.minimumTier = 0;
    this.maximumTier = 2;
    this.experienceRequired = 0;
    this.levelRequired = 0;
    this.consumeExperience = true;

    // Shaped only
    this.mirrored = true;
    this.width = 0;
    this.height = 0;
  }

  // ---------------------------------------------------------------------------
  // Setters
  // ---------------------------------------------------------------------------

  public ArtisanRecipeBuilder setRecipeId(ResourceLocation recipeId) {

    this.recipeId = recipeId;
    return this;
  }

  public ArtisanRecipeBuilder setGroup(String group) {

    this.group = group;
    return this;
  }

  public ArtisanRecipeBuilder setTools(NonNullList<ToolEntry> tools) {

    this.tools = tools;
    return this;
  }

  public ArtisanRecipeBuilder addTool(Ingredient tool, int damage) {

    this.tools.add(new ToolEntry(tool, damage));
    return this;
  }

  public ArtisanRecipeBuilder setResult(ItemStack itemStack) {

    this.result = itemStack.copy();
    return this;
  }

  public ArtisanRecipeBuilder setIngredients(NonNullList<Ingredient> ingredients) {

    this.ingredients = ingredients;
    return this;
  }

  public ArtisanRecipeBuilder setSecondaryIngredients(NonNullList<Ingredient> secondaryIngredients) {

    this.secondaryIngredients = secondaryIngredients;
    return this;
  }

  public ArtisanRecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients) {

    this.consumeSecondaryIngredients = consumeSecondaryIngredients;
    return this;
  }

  public ArtisanRecipeBuilder setFluidIngredient(FluidStack fluidIngredient) {

    this.fluidIngredient = fluidIngredient;
    return this;
  }

  public ArtisanRecipeBuilder setExtraOutputs(NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs) {

    this.extraOutputs = extraOutputs;
    return this;
  }

  public ArtisanRecipeBuilder addExtraOutput(ItemStack itemStack, float chance) {

    this.extraOutputs.add(new ArtisanRecipe.ExtraOutputChancePair(itemStack, chance));
    return this;
  }

  public ArtisanRecipeBuilder setMinimumTier(int minimumTier) {

    this.minimumTier = minimumTier;
    return this;
  }

  public ArtisanRecipeBuilder setMaximumTier(int maximumTier) {

    this.maximumTier = maximumTier;
    return this;
  }

  public ArtisanRecipeBuilder setExperienceRequired(int experienceRequired) {

    this.experienceRequired = experienceRequired;
    return this;
  }

  public ArtisanRecipeBuilder setLevelRequired(int levelRequired) {

    this.levelRequired = levelRequired;
    return this;
  }

  public ArtisanRecipeBuilder setConsumeExperience(boolean consumeExperience) {

    this.consumeExperience = consumeExperience;
    return this;
  }

  public ArtisanRecipeBuilder setMirrored(boolean mirrored) {

    this.mirrored = mirrored;
    return this;
  }

  public ArtisanRecipeBuilder setWidth(int width) {

    this.width = width;
    return this;
  }

  public ArtisanRecipeBuilder setHeight(int height) {

    this.height = height;
    return this;
  }

  // ---------------------------------------------------------------------------
  // Validation
  // ---------------------------------------------------------------------------

  private void validateShapeless() throws Exception {

    if (this.recipeId == null) {
      throw new Exception("Recipe missing recipe id");
    }

    if (this.result == ItemStack.EMPTY) {
      throw new Exception(String.format("Recipe missing result item: %s", this.recipeId));
    }

    if (this.tools.size() > 3) {
      throw new Exception(String.format("Recipe can't have more than %d tools: %s", 3, this.recipeId));
    }

    if (this.ingredients.isEmpty()) {
      throw new Exception(String.format("Recipe missing ingredients: %s", this.recipeId));
    }

    if (this.ingredients.size() > Reference.MAX_RECIPE_WIDTH * Reference.MAX_RECIPE_HEIGHT) {
      throw new Exception(String.format("Recipe can't have more than %d ingredients: %s", Reference.MAX_RECIPE_WIDTH * Reference.MAX_RECIPE_HEIGHT, this.recipeId));
    }

    if (this.secondaryIngredients.size() > 9) {
      throw new Exception(String.format("Recipe can't have more than %d secondary ingredients: %s", 9, this.recipeId));
    }

    EnumTier tier = RecipeTierCalculator.calculateTier(
        this.width,
        this.height,
        this.tools.size(),
        this.secondaryIngredients.size(),
        this.fluidIngredient
    );

    if (tier == null) {
      throw new Exception(String.format("Can't calculate minimum tier for recipe: %s", this.recipeId));
    }

    this.minimumTier = MathHelper.clamp(this.minimumTier, tier.getId(), 2);
    this.maximumTier = MathHelper.clamp(this.maximumTier, this.minimumTier, 2);
    this.experienceRequired = MathHelper.clamp(this.experienceRequired, 0, Integer.MAX_VALUE);
    this.levelRequired = MathHelper.clamp(this.levelRequired, 0, Integer.MAX_VALUE);
  }

  private void validateShaped() throws Exception {

    this.validateShapeless();

    if (this.width == 0 || this.width > Reference.MAX_RECIPE_WIDTH) {
      throw new Exception(String.format("Recipe width must be between 1 and %d", Reference.MAX_RECIPE_WIDTH));
    }

    if (this.height == 0 || this.height > Reference.MAX_RECIPE_HEIGHT) {
      throw new Exception(String.format("Recipe height must be between 1 and %d", Reference.MAX_RECIPE_HEIGHT));
    }
  }

  // ---------------------------------------------------------------------------
  // Instantiation
  // ---------------------------------------------------------------------------

  public ArtisanRecipeShapeless buildShapeless() throws Exception {

    this.validateShapeless();

    return new ArtisanRecipeShapeless(
        this.recipeId,
        this.group,
        this.tools,
        this.result,
        this.ingredients,
        this.secondaryIngredients,
        this.consumeSecondaryIngredients,
        this.fluidIngredient,
        this.extraOutputs,
        this.minimumTier,
        this.maximumTier,
        this.experienceRequired,
        this.levelRequired,
        this.consumeExperience
    );
  }

  public ArtisanRecipeShaped buildShaped() throws Exception {

    this.validateShaped();

    return new ArtisanRecipeShaped(
        this.recipeId,
        this.group,
        this.tools,
        this.result,
        this.ingredients,
        this.secondaryIngredients,
        this.consumeSecondaryIngredients,
        this.fluidIngredient,
        this.extraOutputs,
        this.mirrored,
        this.minimumTier,
        this.maximumTier,
        this.experienceRequired,
        this.levelRequired,
        this.consumeExperience,
        this.width,
        this.height
    );
  }
}
