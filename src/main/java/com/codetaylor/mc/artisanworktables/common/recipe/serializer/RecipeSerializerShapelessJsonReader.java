package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeBuilder;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShapeless;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RecipeSerializerShapelessJsonReader
    extends RecipeSerializerJsonReader<ArtisanRecipeShapeless> {

  public RecipeSerializerShapelessJsonReader(int maxWidth, int maxHeight) {

    super(maxWidth, maxHeight);
  }

  @Override
  @Nonnull
  public ArtisanRecipeShapeless read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {

    ArtisanRecipeBuilder builder = new ArtisanRecipeBuilder();
    this.read(builder, recipeId, json);

    NonNullList<Ingredient> ingredients = this.readIngredients(JSONUtils.getJsonArray(json, "ingredients"));

    if (ingredients.isEmpty()) {
      throw new JsonParseException("No ingredients for shapeless recipe");

    } else if (ingredients.size() > this.maxWidth * this.maxHeight) {
      throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (this.maxWidth * this.maxHeight));
    }

    try {
      return builder
          .setIngredients(ingredients)
          .buildShapeless();

    } catch (Exception e) {
      throw new JsonParseException("Error creating recipe: " + recipeId, e);
    }
  }

  private NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {

    NonNullList<Ingredient> result = NonNullList.create();

    for (int i = 0; i < ingredientArray.size(); ++i) {
      Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));

      if (!ingredient.hasNoMatchingItems()) {
        result.add(ingredient);
      }
    }

    return result;
  }
}
