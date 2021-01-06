package com.codetaylor.mc.artisanworktables.common.plugin.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.logger.ILogger;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import com.blamejared.crafttweaker.impl.managers.CTCraftingTableManager;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeBuilder;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.codetaylor.mc.artisanworktables.common.recipe.RecipeTypes;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.HashMap;
import java.util.Map;

@ZenRegister
@ZenCodeType.Name("mods.artisanworktables.Recipe")
public class ZenRecipe {

  private final ZenEnumType type;
  private final ArtisanRecipeBuilder builder;

  private boolean isShaped;

  public ZenRecipe(ZenEnumType type, ArtisanRecipeBuilder builder) {

    this.type = type;
    this.builder = builder;
  }

  @ZenCodeType.Method
  public static ZenRecipe type(ZenEnumType type) {

    return new ZenRecipe(type, new ArtisanRecipeBuilder());
  }

  @ZenCodeType.Method
  public ZenRecipe shaped(IIngredient[][] ingredients) {

    NonNullList<Ingredient> result = NonNullList.create();

    int maxWidth = 0;

    for (IIngredient[] iIngredientRow : ingredients) {
      maxWidth = Math.max(iIngredientRow.length, maxWidth);
      for (IIngredient iIngredient : iIngredientRow) {
        result.add(iIngredient.asVanillaIngredient());
      }
    }

    this.builder.setIngredients(result);
    this.builder.setWidth(maxWidth);
    this.builder.setHeight(ingredients.length);
    this.isShaped = true;
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe shapeless(IIngredient[] ingredients) {

    NonNullList<Ingredient> result = NonNullList.create();

    for (IIngredient iIngredient : ingredients) {
      result.add(iIngredient.asVanillaIngredient());
    }

    this.builder.setIngredients(result);
    this.builder.setWidth(0);
    this.builder.setHeight(0);
    this.isShaped = false;
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe tool(IIngredient tool, int damage) {

    this.builder.addTool(tool.asVanillaIngredient(), damage);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe fluid(IFluidStack fluid) {

    this.builder.setFluidIngredient(fluid.getInternal());
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe secondary(IIngredient[] ingredients) {

    return this.secondary(ingredients, true);
  }

  @ZenCodeType.Method
  public ZenRecipe secondary(IIngredient[] ingredients, boolean consume) {

    NonNullList<Ingredient> result = NonNullList.create();

    for (IIngredient iIngredient : ingredients) {
      result.add(iIngredient.asVanillaIngredient());
    }

    this.builder.setSecondaryIngredients(result);
    this.builder.setConsumeSecondaryIngredients(consume);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe mirrored(boolean mirrored) {

    this.builder.setMirrored(mirrored);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe restrict(ZenEnumTier minimum) {

    return this.restrict(minimum, ZenEnumTier.WORKSHOP);
  }

  @ZenCodeType.Method
  public ZenRecipe restrict(ZenEnumTier minimum, ZenEnumTier maximum) {

    this.builder.setMinimumTier(minimum.getTier().getId());
    this.builder.setMaximumTier(maximum.getTier().getId());
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe experience(int amount) {

    return this.experience(amount, true);
  }

  @ZenCodeType.Method
  public ZenRecipe experience(int amount, boolean consume) {

    this.builder.setLevelRequired(0);
    this.builder.setExperienceRequired(amount);
    this.builder.setConsumeExperience(consume);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe level(int amount) {

    return this.level(amount, true);
  }

  @ZenCodeType.Method
  public ZenRecipe level(int amount, boolean consume) {

    this.builder.setExperienceRequired(0);
    this.builder.setLevelRequired(amount);
    this.builder.setConsumeExperience(consume);
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe output(IItemStack output) {

    this.builder.setResult(output.getInternal());
    return this;
  }

  @ZenCodeType.Method
  public ZenRecipe extra(IItemStack extra, float chance) {

    this.builder.addExtraOutput(extra.getInternal(), chance);
    return this;
  }

  @ZenCodeType.Method
  public void register(String name) {

    CraftTweakerUtil.validateRecipeName(name);
    ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
    this.builder.setRecipeId(resourceLocation);

    try {

      if (this.isShaped) {

        CraftTweakerAPI.apply(new AddRecipeAction(this.builder.buildShaped(this.type.getType())));

      } else {
        CraftTweakerAPI.apply(new AddRecipeAction(this.builder.buildShapeless(this.type.getType())));
      }

    } catch (Exception e) {
      CraftTweakerAPI.logThrowing("Error registering recipe: " + resourceLocation, e);
    }
  }

  public static class AddRecipeAction
      implements IRuntimeAction {

    private final ArtisanRecipe recipe;

    public AddRecipeAction(ArtisanRecipe recipe) {

      this.recipe = recipe;
    }

    @Override
    public void apply() {

      EnumType tableType = this.recipe.getTableType();
      IRecipeType<ArtisanRecipe> type;

      if (this.recipe instanceof ArtisanRecipeShaped) {
        type = RecipeTypes.SHAPED_RECIPE_TYPES.get(tableType);

      } else {
        type = RecipeTypes.SHAPELESS_RECIPE_TYPES.get(tableType);
      }

      Map<ResourceLocation, IRecipe<?>> recipeMap = CTCraftingTableManager.recipeManager.recipes.computeIfAbsent(type, t -> new HashMap<>());
      recipeMap.put(this.recipe.getId(), this.recipe);
    }

    @Override
    public String describe() {

      return "Adding ArtisanWorktables " + this.recipe.getTableType().getName() + " recipe, with name: \"" + this.recipe.getId() + "\", that outputs: " + (new MCItemStackMutable(this.recipe.getRecipeOutput()).toString());
    }

    @Override
    public boolean validate(ILogger logger) {

      return true;
    }
  }

}
