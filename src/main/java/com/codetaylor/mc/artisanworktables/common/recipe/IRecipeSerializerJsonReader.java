package com.codetaylor.mc.artisanworktables.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IRecipeSerializerJsonReader {

  @Nonnull
  ArtisanRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json);
}
