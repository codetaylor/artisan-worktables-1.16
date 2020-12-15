package com.codetaylor.mc.artisanworktables.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeSerializerShaped
    extends ForgeRegistryEntry<IRecipeSerializer<?>>
    implements IRecipeSerializer<ArtisanRecipe> {

  private final IRecipeSerializerJson recipeSerializerJson;

  public RecipeSerializerShaped(IRecipeSerializerJson recipeSerializerJson) {

    this.recipeSerializerJson = recipeSerializerJson;
  }

  @Nonnull
  @Override
  public ArtisanRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {

    return this.recipeSerializerJson.read(recipeId, json);
  }

  @Nullable
  @Override
  public ArtisanRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {

    return null;
  }

  @Override
  public void write(@Nonnull PacketBuffer buffer, @Nonnull ArtisanRecipe recipe) {

  }

}
