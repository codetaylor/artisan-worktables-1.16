package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeBuilder;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.codetaylor.mc.artisanworktables.common.util.RecipeSerializerHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class RecipeSerializerShapedJsonReader
    extends RecipeSerializerJsonReader<ArtisanRecipeShaped> {

  public RecipeSerializerShapedJsonReader(int maxWidth, int maxHeight) {

    super(maxWidth, maxHeight);
  }

  @Override
  @Nonnull
  public ArtisanRecipeShaped read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {

    ArtisanRecipeBuilder builder = new ArtisanRecipeBuilder();
    this.read(builder, recipeId, json);

    Map<String, Ingredient> keys = RecipeSerializerHelper.deserializeKey(JSONUtils.getJsonObject(json, "key"));

    String[] pattern = RecipeSerializerHelper.shrink(RecipeSerializerHelper.patternFromJson(JSONUtils.getJsonArray(json, "pattern"), this.maxWidth, this.maxHeight));
    int width = pattern[0].length();
    int height = pattern.length;
    NonNullList<Ingredient> ingredients = RecipeSerializerHelper.deserializeIngredients(pattern, keys, width, height);

    boolean mirrored = JSONUtils.getBoolean(json, "mirrored", true);

    try {
      return builder
          .setIngredients(ingredients)
          .setMirrored(mirrored)
          .setWidth(width)
          .setHeight(height)
          .buildShaped();

    } catch (Exception e) {
      throw new JsonParseException("Error creating recipe: " + recipeId, e);
    }
  }
}
