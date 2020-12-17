package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeBuilder;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import com.codetaylor.mc.artisanworktables.common.util.RecipeSerializerHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public abstract class RecipeSerializerJsonReader<R extends ArtisanRecipe>
    implements IRecipeSerializerJsonReader<R> {

  protected final int maxWidth;
  protected final int maxHeight;

  public RecipeSerializerJsonReader(int maxWidth, int maxHeight) {

    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
  }

  protected void read(ArtisanRecipeBuilder builder, ResourceLocation recipeId, JsonObject json) {

    String group = JSONUtils.getString(json, "group", "");

    ItemStack result = RecipeSerializerHelper.deserializeItem(JSONUtils.getJsonObject(json, "result"));
    NonNullList<ToolEntry> tools = this.deserializeTools(json);
    NonNullList<Ingredient> secondaryIngredients = this.deserializeSecondaryIngredients(json);
    boolean consumeSecondaryIngredients = JSONUtils.getBoolean(json, "consumeSecondaryIngredients", true);
    FluidStack fluidIngredient = this.deserializeFluidIngredient(json);
    NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutput = this.deserializeExtraOutput(json);

    int minimumTier = JSONUtils.getInt(json, "minimumTier", 0);
    int maximumTier = JSONUtils.getInt(json, "maximumTier", 2);
    int experienceRequired = JSONUtils.getInt(json, "experienceRequired", 0);
    int levelRequired = JSONUtils.getInt(json, "levelRequired", 0);
    boolean consumeExperience = JSONUtils.getBoolean(json, "consumeExperience", true);

    builder
        .setRecipeId(recipeId)
        .setGroup(group)
        .setResult(result)
        .setTools(tools)
        .setSecondaryIngredients(secondaryIngredients)
        .setConsumeSecondaryIngredients(consumeSecondaryIngredients)
        .setFluidIngredient(fluidIngredient)
        .setExtraOutputs(extraOutput)
        .setMinimumTier(minimumTier)
        .setMaximumTier(maximumTier)
        .setExperienceRequired(experienceRequired)
        .setLevelRequired(levelRequired)
        .setConsumeExperience(consumeExperience);
  }

  protected NonNullList<ArtisanRecipe.ExtraOutputChancePair> deserializeExtraOutput(JsonObject json) {

    NonNullList<ArtisanRecipe.ExtraOutputChancePair> result = NonNullList.create();

    if (json.has("extraOutput")) {
      JsonArray jsonArray = JSONUtils.getJsonArray(json, "extraOutput");

      for (JsonElement jsonElement : jsonArray) {
        JsonObject itemObject = jsonElement.getAsJsonObject();
        ItemStack itemStack = RecipeSerializerHelper.deserializeItem(itemObject);
        float chance = JSONUtils.getFloat(itemObject, "chance", 1);
        result.add(new ArtisanRecipe.ExtraOutputChancePair(itemStack, chance));
      }
    }

    return result;
  }

  protected FluidStack deserializeFluidIngredient(@Nonnull JsonObject json) {

    FluidStack fluidIngredient;
    if (json.has("fluidIngredient")) {
      fluidIngredient = RecipeSerializerHelper.deserializeFluid(JSONUtils.getJsonObject(json, "fluidIngredient"), 1000);

    } else {
      fluidIngredient = FluidStack.EMPTY;
    }
    return fluidIngredient;
  }

  protected NonNullList<Ingredient> deserializeSecondaryIngredients(JsonObject json) {

    NonNullList<Ingredient> result = NonNullList.create();

    if (json.has("secondaryIngredients")) {
      JsonArray jsonArray = JSONUtils.getJsonArray(json, "secondaryIngredients");

      for (JsonElement jsonElement : jsonArray) {
        JsonObject ingredientObject = jsonElement.getAsJsonObject();
        Ingredient ingredient = Ingredient.deserialize(ingredientObject);
        result.add(ingredient);
      }
    }

    if (result.size() > 9) {
      throw new JsonParseException("Secondary ingredient count cannot be greater than 9, was " + result.size());
    }

    return result;
  }

  protected NonNullList<ToolEntry> deserializeTools(@Nonnull JsonObject json) {

    NonNullList<ToolEntry> result = NonNullList.create();

    JsonArray toolArray = JSONUtils.getJsonArray(json, "tools", new JsonArray());

    if (toolArray != null) {

      for (JsonElement jsonElement : toolArray) {
        JsonObject toolObject = jsonElement.getAsJsonObject();
        Ingredient tool = Ingredient.deserialize(toolObject);
        int damage = JSONUtils.getInt(toolObject, "damage", 1);
        result.add(new ToolEntry(tool, damage));
      }

      if (result.size() > 3) {
        throw new JsonParseException("Tool count cannot be greater than 3, was " + result.size());
      }
    }

    return result;
  }
}
