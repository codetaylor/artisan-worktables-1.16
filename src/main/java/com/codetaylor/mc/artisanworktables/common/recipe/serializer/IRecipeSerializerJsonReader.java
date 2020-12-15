package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IRecipeSerializerJsonReader<T extends IRecipe<?>> {

  @Nonnull
  T read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json);
}
