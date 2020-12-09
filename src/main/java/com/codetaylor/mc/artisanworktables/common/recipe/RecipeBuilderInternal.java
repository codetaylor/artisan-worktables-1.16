package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.recipe.IRecipeFactory;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.IRecipeBuilderCopyStrategyInternal;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.RecipeBuilderCopyStrategyByName;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.RecipeBuilderCopyStrategyByOutput;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.RecipeBuilderCopyStrategyByRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class RecipeBuilderInternal
    implements IRecipeBuilder,
    IRecipeBuilderAction {

  public static RecipeBuilderInternal get(String table, IRecipeFactory recipeFactory) throws RecipeBuilderException {

    table = table.toLowerCase();

    if (!ArtisanAPI.isWorktableNameValid(table) && !"all".equals(table)) {
      throw new RecipeBuilderException("Unknown table type: " + table + ". Valid table types are: " + String.join(
          ",",
          ArtisanAPI.getWorktableNames()
      ));
    }

    return new RecipeBuilderInternal(table, ModuleWorktables.RECIPE_ADDITION_QUEUE, recipeFactory);
  }

  public static class Copy {

    public static IRecipeBuilderCopyStrategy byName(String recipeName) throws RecipeBuilderException {

      return new RecipeBuilderCopyStrategyByName(recipeName);
    }

    public static IRecipeBuilderCopyStrategy byRecipe(IRecipe recipe) throws RecipeBuilderException {

      return new RecipeBuilderCopyStrategyByRecipe(recipe);
    }

    public static IRecipeBuilderCopyStrategy byOutput(IArtisanIngredient[] outputs) throws RecipeBuilderException {

      return new RecipeBuilderCopyStrategyByOutput(outputs);
    }
  }

  private static final int MAX_TOOL_COUNT = 3;

  private String tableName;
  private final IRecipeAdditionQueue recipeAdditionQueue;
  private IRecipeFactory recipeFactory;

  // --------------------------------------------------------------------------
  // - Recipe

  private String name;
  private int width;
  private int height;
  private boolean mirrored;
  private List<IArtisanIngredient> ingredients;
  private List<IArtisanIngredient> secondaryIngredients;
  private boolean consumeSecondaryIngredients;
  private FluidStack fluidIngredient;
  private List<ToolIngredientEntry> tools;
  private List<OutputWeightPair> outputWeightPairList;
  private ExtraOutputChancePair[] extraOutputs;
  private int minimumTier;
  private int maximumTier;
  private int experienceRequired;
  private int levelRequired;
  private boolean consumeExperience;
  private Map<ResourceLocation, IRequirement> requirementMap;
  private boolean hidden;

  // --------------------------------------------------------------------------
  // - Copy

  private IRecipeBuilderCopyStrategyInternal recipeCopyStrategy;

  // --------------------------------------------------------------------------
  // - Validation

  private boolean invalid;
  private boolean inputSet;
  private boolean outputSet;
  private boolean validateRun;

  // --------------------------------------------------------------------------
  // - Constructor

  public RecipeBuilderInternal(
      String tableName,
      IRecipeAdditionQueue recipeAdditionQueue,
      IRecipeFactory recipeFactory
  ) {

    this.tableName = tableName;
    this.recipeAdditionQueue = recipeAdditionQueue;
    this.recipeFactory = recipeFactory;

    this.ingredients = null;
    this.secondaryIngredients = Collections.emptyList();
    this.consumeSecondaryIngredients = true;
    this.fluidIngredient = null;
    this.outputWeightPairList = new ArrayList<>();
    this.extraOutputs = new ExtraOutputChancePair[3];
    Arrays.fill(this.extraOutputs, new ExtraOutputChancePair(ArtisanItemStack.EMPTY, 0));
    this.tools = new ArrayList<>();
    this.minimumTier = 0;
    this.maximumTier = Integer.MAX_VALUE;
    this.experienceRequired = 0;
    this.levelRequired = 0;
    this.consumeExperience = true;

    this.requirementMap = new HashMap<>();
  }

  // --------------------------------------------------------------------------
  // - Validation

  private void isNotEmpty(IArtisanIngredient ingredient, String message) throws RecipeBuilderException {

    if (ingredient.isEmpty()) {
      this.setInvalid(message);
    }
  }

  private void isNotEmpty(IArtisanItemStack itemStack, String message) throws RecipeBuilderException {

    if (itemStack.isEmpty()) {
      this.setInvalid(message);
    }
  }

  private void isNonnull(Object o, String message) throws RecipeBuilderException {

    if (o == null) {
      this.setInvalid(message);
    }
  }

  private void isNull(Object o, String message) throws RecipeBuilderException {

    if (o != null) {
      this.setInvalid(message);
    }
  }

  private void isTrue(boolean b, String message) throws RecipeBuilderException {

    if (!b) {
      this.setInvalid(message);
    }
  }

  private void isFalse(boolean b, String message) throws RecipeBuilderException {

    if (b) {
      this.setInvalid(message);
    }
  }

  private void isNotZeroLength(Object[] o, String message) throws RecipeBuilderException {

    if (o.length == 0) {
      this.setInvalid(message);
    }
  }

  private void setInvalid(String message) throws RecipeBuilderException {

    this.invalid = true;
    throw new RecipeBuilderException(message);
  }

  // --------------------------------------------------------------------------
  // - Recipe

  @Override
  public IRecipeBuilder setName(String name) throws RecipeBuilderException {

    this.isNonnull(name, "Recipe name can't be null");
    this.isTrue(name.length() > 0, "Recipe name can't be zero length");

    this.name = name;
    return this;
  }

  @Override
  public IRecipeBuilder setIngredients(IArtisanIngredient[][] ingredients) throws RecipeBuilderException {

    this.isNonnull(ingredients, "Ingredient matrix can't be null");
    this.isFalse(this.inputSet, "Recipe input already set");
    this.isNull(this.ingredients, "Ingredients already set, can't be set twice");

    this.ingredients = new ArrayList<>();
    this.width = 0;

    for (IArtisanIngredient[] row : ingredients) {

      if (row.length > this.width) {
        this.width = row.length;
      }
    }

    boolean isValid = false;

    for (IArtisanIngredient[] row : ingredients) {

      this.isNonnull(row, "Ingredient row can't be null");
      this.isNotZeroLength(row, "Ingredient row can't be zero length");

      List<IArtisanIngredient> cols = NonNullList.withSize(this.width, ArtisanIngredient.EMPTY);

      for (int i = 0; i < row.length; i++) {
        cols.set(i, row[i]);

        if (!isValid && !row[i].isEmpty()) {
          isValid = true;
        }
      }
      this.ingredients.addAll(cols);
    }

    if (!isValid) {
      this.setInvalid("Shaped ingredient matrix must contain at minimum one non-empty entry");
    }

    this.height = ingredients.length;
    this.inputSet = true;
    return this;
  }

  @Override
  public IRecipeBuilder setIngredients(IArtisanIngredient[] ingredients) throws RecipeBuilderException {

    this.isNonnull(ingredients, "Ingredient array can't be null");
    this.isFalse(this.inputSet, "Recipe input already set");
    this.isNull(this.ingredients, "Ingredients already set, can't be set twice");

    for (int i = 0; i < ingredients.length; i++) {
      this.isNotEmpty(ingredients[i], "Shapeless ingredient array can't contain empty elements, found empty element at index: " + i);
    }

    this.ingredients = new ArrayList<>();
    Collections.addAll(this.ingredients, ingredients);
    this.inputSet = true;
    return this;
  }

  @Override
  public IRecipeBuilder setFluidIngredient(FluidStack fluidIngredient) throws RecipeBuilderException {

    this.isNull(this.fluidIngredient, "Fluid ingredient already set, can't be set twice");

    this.fluidIngredient = fluidIngredient;
    return this;
  }

  @Override
  public IRecipeBuilder setSecondaryIngredients(IArtisanIngredient[] secondaryIngredients) throws RecipeBuilderException {

    this.isNonnull(secondaryIngredients, "Secondary ingredients array can't be null");
    this.isNotZeroLength(secondaryIngredients, "Secondary ingredients array can't be zero length");
    this.isTrue(this.secondaryIngredients.isEmpty(), "Secondary ingredients already set, can't be set twice");
    this.isTrue(secondaryIngredients.length <= 9, "Exceeded max allowed 9 secondary ingredients");

    for (int i = 0; i < secondaryIngredients.length; i++) {
      this.isNotEmpty(secondaryIngredients[i], "Secondary ingredient array element can't be empty at index: " + i);
    }

    this.secondaryIngredients = new ArrayList<>();
    Collections.addAll(this.secondaryIngredients, secondaryIngredients);
    return this;
  }

  @Override
  public IRecipeBuilder setConsumeSecondaryIngredients() {

    return this.setConsumeSecondaryIngredients(true);
  }

  @Override
  public IRecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients) {

    this.consumeSecondaryIngredients = consumeSecondaryIngredients;
    return this;
  }

  @Override
  public IRecipeBuilder setMirrored() {

    return this.setMirrored(true);
  }

  @Override
  public IRecipeBuilder setMirrored(boolean mirrored) {

    this.mirrored = mirrored;
    return this;
  }

  @Override
  public IRecipeBuilder addTool(IArtisanIngredient tool, int toolDamage) throws RecipeBuilderException {

    this.isNonnull(tool, "Tool can't be null");
    this.isTrue(this.tools.size() < MAX_TOOL_COUNT, "Exceeded maximum tool count");
    this.isNotEmpty(tool, "Tool ingredient can't be empty");

    this.tools.add(new ToolIngredientEntry(tool, toolDamage));
    return this;
  }

  @Override
  public IRecipeBuilder addOutput(IArtisanItemStack output, int weight) throws RecipeBuilderException {

    this.isNonnull(output, "Output can't be null");
    this.isNotEmpty(output, "Output can't be empty");

    if (weight < 1) {
      weight = 1;
    }

    this.outputWeightPairList.add(new OutputWeightPair(output, weight));
    this.outputSet = true;
    return this;
  }

  @Override
  public IRecipeBuilder setExtraOutput(
      int index,
      IArtisanItemStack output,
      float chance
  ) throws RecipeBuilderException {

    this.isNonnull(output, "Output can't be null");
    this.isNotEmpty(output, "Output can't be empty");

    this.isTrue(index >= 0, "Extra output index out of bounds");
    this.isTrue(index < this.extraOutputs.length, "Extra output index out of bounds");

    chance = MathHelper.clamp(chance, 0, 1);
    this.extraOutputs[index] = new ExtraOutputChancePair(output, chance);
    return this;
  }

  @Override
  public IRecipeBuilder setMinimumTier(int tier) throws RecipeBuilderException {

    try {
      EnumTier.fromId(tier);

    } catch (Exception e) {
      this.setInvalid("Invalid tier: " + tier);
    }

    if (tier > this.maximumTier) {
      this.setInvalid("Minimum tier can't be larger than maximum tier: " + this.maximumTier + " < " + tier);
    }

    this.minimumTier = tier;
    return this;
  }

  @Override
  public IRecipeBuilder setMaximumTier(int tier) throws RecipeBuilderException {

    try {
      EnumTier.fromId(tier);

    } catch (Exception e) {
      this.setInvalid("Invalid tier: " + tier);
    }

    if (tier < this.minimumTier) {
      this.setInvalid("Maximum tier can't be smaller than minimum tier: " + this.minimumTier + ">" + tier);
    }

    this.maximumTier = tier;
    return this;
  }

  @Override
  public IRecipeBuilder setExperienceRequired(int cost) throws RecipeBuilderException {

    this.isTrue(cost > 0, "Experience required must be greater than zero");

    this.levelRequired = 0;
    this.experienceRequired = cost;
    return this;
  }

  @Override
  public IRecipeBuilder setLevelRequired(int cost) throws RecipeBuilderException {

    this.isTrue(cost > 0, "Level required must be greater than zero");

    this.experienceRequired = 0;
    this.levelRequired = cost;
    return this;
  }

  @Override
  public IRecipeBuilder setConsumeExperience() {

    return this.setConsumeExperience(true);
  }

  @Override
  public IRecipeBuilder setConsumeExperience(boolean consumeExperience) {

    this.consumeExperience = consumeExperience;
    return this;
  }

  @Override
  public IRecipeBuilder setCopy(IRecipeBuilderCopyStrategy copyStrategy) {

    this.recipeCopyStrategy = (IRecipeBuilderCopyStrategyInternal) copyStrategy;
    return this;
  }

  @Override
  public IRecipeBuilder addRequirement(IRequirementBuilder matchRequirementBuilder) {

    ResourceLocation location = matchRequirementBuilder.getResourceLocation();

    if (Loader.isModLoaded(matchRequirementBuilder.getRequirementId().toLowerCase())) {
      IRequirement requirement = matchRequirementBuilder.create();
      this.requirementMap.put(location, requirement);
    }

    return this;
  }

  public void validate() throws RecipeBuilderException {

    if (this.invalid) {
      throw new RecipeBuilderException("Invalid recipe");
    }

    if (this.recipeCopyStrategy != null) {

      if (!this.recipeCopyStrategy.isValid()) {
        this.setInvalid("Invalid recipe copy strategy");

      } else if (!this.inputSet && this.recipeCopyStrategy.isExcludeInput()) {
        this.setInvalid("Recipe missing input");

      } else if (!this.outputSet && this.recipeCopyStrategy.isExcludeOutput()) {
        this.setInvalid("Recipe missing output");
      }

    } else {

      // Recipe must have a minimum of one output
      this.isTrue(this.outputSet, "No outputs defined for recipe");

      // Recipe must have ingredients
      this.isTrue(this.inputSet, "No ingredients defined for recipe");
    }

    // Must be able to calculate recipe tier
    if (!"all".equals(this.tableName)) {
      EnumTier tier = RecipeTierCalculator.calculateTier(
          this.tableName,
          this.width,
          this.height,
          this.tools.size(),
          this.secondaryIngredients.size(),
          this.fluidIngredient
      );

      if (tier == null) {
        this.setInvalid("Unable to calculate recipe tier");

      } else {
        this.minimumTier = Math.max(this.minimumTier, tier.getId());
      }
    }

    this.validateRun = true;
  }

  @Override
  public IRecipeBuilder setHidden() {

    return this.setHidden(true);
  }

  @Override
  public IRecipeBuilder setHidden(boolean hidden) {

    this.hidden = hidden;
    return this;
  }

  public IRecipeBuilderCopyStrategyInternal getRecipeCopyStrategy() {

    return this.recipeCopyStrategy;
  }

  public String getTableName() {

    return this.tableName;
  }

  public IRecipeAdditionQueue getRecipeAdditionQueue() {

    return this.recipeAdditionQueue;
  }

  public IRecipeFactory getRecipeFactory() {

    return this.recipeFactory;
  }

  public RecipeBuilderInternal copy() {

    RecipeBuilderInternal copy = new RecipeBuilderInternal(this.tableName, this.recipeAdditionQueue, this.recipeFactory);

    copy.outputSet = this.outputSet;
    copy.inputSet = this.inputSet;
    copy.invalid = this.invalid;

    copy.width = this.width;
    copy.height = this.height;
    copy.mirrored = this.mirrored;

    if (this.ingredients != null) {
      copy.ingredients = new ArrayList<>(this.ingredients);
    }
    copy.secondaryIngredients = new ArrayList<>(this.secondaryIngredients);
    copy.consumeSecondaryIngredients = this.consumeSecondaryIngredients;
    copy.fluidIngredient = this.fluidIngredient;
    copy.tools = new ArrayList<>(this.tools);
    copy.outputWeightPairList = new ArrayList<>(this.outputWeightPairList);
    copy.extraOutputs = new ExtraOutputChancePair[this.extraOutputs.length];
    System.arraycopy(this.extraOutputs, 0, copy.extraOutputs, 0, this.extraOutputs.length);
    copy.minimumTier = this.minimumTier;
    copy.maximumTier = this.maximumTier;
    copy.experienceRequired = this.experienceRequired;
    copy.levelRequired = this.levelRequired;
    copy.consumeExperience = this.consumeExperience;
    copy.requirementMap = new HashMap<>(this.requirementMap);
    copy.hidden = this.hidden;

    return copy;
  }

  @Override
  public IRecipeBuilder create() throws RecipeBuilderException {

    if (!this.validateRun) {
      this.validate();
    }

    if (this.recipeCopyStrategy != null) {
      this.recipeAdditionQueue.offerWithCopy(this);

    } else {
      this.recipeAdditionQueue.offer(this);
    }

    return new RecipeBuilderInternal(this.tableName, this.recipeAdditionQueue, this.recipeFactory);
  }

  @Override
  public void apply(ILogger logger) throws RecipeBuilderException {

    // Build the recipe object and add it to the registry.

    // Decide which style recipe matcher to use.

    IRecipeMatrixMatcher recipeMatcher;

    if (this.width > 0 && this.height > 0) {
      recipeMatcher = IRecipeMatrixMatcher.SHAPED;

    } else {
      recipeMatcher = IRecipeMatrixMatcher.SHAPELESS;
    }

    // Prepare the tool entry array.

    ToolEntry[] tools = new ToolEntry[this.tools.size()];

    for (int i = 0; i < this.tools.size(); i++) {
      ToolIngredientEntry entry = this.tools.get(i);
      tools[i] = new ToolEntry(entry.getTool(), entry.getDamage());
    }

    // Ensure that the secondary ingredient collection is not null.

    if (this.secondaryIngredients == null) {
      this.secondaryIngredients = Collections.emptyList();
    }

    if ("all".equals(this.tableName)) {

      for (String worktableName : ArtisanAPI.getWorktableNames()) {

        // Must be able to calculate recipe tier
        EnumTier tier = RecipeTierCalculator.calculateTier(
            worktableName,
            this.width,
            this.height,
            this.tools.size(),
            this.secondaryIngredients.size(),
            this.fluidIngredient
        );

        if (tier == null) {
          this.setInvalid("Unable to calculate recipe tier");

        } else {
          this.minimumTier = Math.max(this.minimumTier, tier.getId());
        }

        this.addRecipe(logger, recipeMatcher, tools, worktableName);
      }

    } else {
      this.addRecipe(logger, recipeMatcher, tools, this.tableName);
    }
  }

  private void addRecipe(ILogger logger, IRecipeMatrixMatcher recipeMatcher, ToolEntry[] tools, String tableName) throws RecipeBuilderException {

    // Calculate the recipe name and ensure it is unique.

    RecipeRegistry registry = ArtisanAPI.getWorktableRecipeRegistry(tableName);

    if (this.name == null) {
      this.name = this.calculateName(registry, logger);

    } else {
      this.name = this.prefixTableName(this.name);

      // Ensure the recipe has a unique name.
      int i = 0;

      while (registry.hasRecipe(this.name)) {
        logger.logWarning("Duplicate recipe name found: " + this.name);

        if (i > 1000) {
          // sanity
          break;
        }

        i += 1;
        this.name = this.prefixTableName(this.name + "_" + i);
      }
    }

    if (registry.hasRecipe(this.name)) {
      throw new RecipeBuilderException("Duplicate recipe name: " + this.name);
    }

    registry.addRecipe(this.recipeFactory.create(
        this.name,
        new HashMap<>(this.requirementMap),
        this.outputWeightPairList,
        tools,
        this.ingredients,
        this.secondaryIngredients,
        this.consumeSecondaryIngredients,
        this.fluidIngredient,
        this.experienceRequired,
        this.levelRequired,
        this.consumeExperience,
        this.extraOutputs,
        recipeMatcher,
        this.mirrored,
        this.width,
        this.height,
        this.minimumTier,
        this.maximumTier,
        this.hidden
    ));
  }

  private String calculateName(RecipeRegistry registry, ILogger logger) {

    HashCodeBuilder builder = new HashCodeBuilder(17, 37);

    // Requirements
    for (ResourceLocation resourceLocation : this.requirementMap.keySet()) {
      builder.append(resourceLocation.hashCode());
    }

    // Output
    for (OutputWeightPair pair : this.outputWeightPairList) {
      builder.append(HashCodeUtil.get(pair));
    }

    // Tools
    for (ToolIngredientEntry entry : this.tools) {
      builder.append(HashCodeUtil.get(entry));
    }

    // Ingredients
    for (IArtisanIngredient ingredient : this.ingredients) {
      builder.append(HashCodeUtil.get(ingredient));
    }

    // Secondary Ingredients
    for (IArtisanIngredient ingredient : this.secondaryIngredients) {
      builder.append(HashCodeUtil.get(ingredient));
    }

    builder.append(this.consumeSecondaryIngredients)
        .append(HashCodeUtil.get(this.fluidIngredient))
        .append(this.experienceRequired)
        .append(this.levelRequired)
        .append(this.consumeExperience);

    // Extra Chance Outputs
    for (ExtraOutputChancePair pair : this.extraOutputs) {
      builder.append(HashCodeUtil.get(pair));
    }

    builder.append(this.mirrored)
        .append(this.width)
        .append(this.height)
        .append(this.minimumTier)
        .append(this.maximumTier);

    int hash = builder.build();
    String recipeName = this.prefixTableName(String.valueOf(hash));

    // check for duplicate recipe name
    while (registry.hasRecipe(recipeName)) {

      if (ModuleWorktablesConfig.ENABLE_DUPLICATE_RECIPE_NAME_WARNINGS) {
        logger.logWarning("Duplicate recipe name found: " + recipeName);
      }

      hash += 1;
      recipeName = this.prefixTableName(String.valueOf(hash));
    }

    return recipeName;
  }

  private String prefixTableName(String name) {

    return this.tableName + ":" + name;
  }
}
