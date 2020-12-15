package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeSerializer<R extends ArtisanRecipe>
    extends ForgeRegistryEntry<IRecipeSerializer<?>>
    implements IRecipeSerializer<R> {

  private final IRecipeSerializerJsonReader<R> recipeSerializerJsonReader;
  private final IRecipeSerializerPacketReader<R> recipeSerializerPacketReader;
  private final IRecipeSerializerPacketWriter<R> recipeSerializerPacketWriter;

  public RecipeSerializer(
      IRecipeSerializerJsonReader<R> recipeSerializerJsonReader,
      IRecipeSerializerPacketReader<R> recipeSerializerPacketReader,
      IRecipeSerializerPacketWriter<R> recipeSerializerPacketWriter
  ) {

    this.recipeSerializerJsonReader = recipeSerializerJsonReader;
    this.recipeSerializerPacketReader = recipeSerializerPacketReader;
    this.recipeSerializerPacketWriter = recipeSerializerPacketWriter;
  }

  @Nonnull
  @Override
  public R read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {

    return this.recipeSerializerJsonReader.read(recipeId, json);
  }

  @Nullable
  @Override
  public R read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {

    return this.recipeSerializerPacketReader.read(recipeId, buffer);
  }

  @Override
  public void write(@Nonnull PacketBuffer buffer, @Nonnull R recipe) {

    this.recipeSerializerPacketWriter.write(buffer, recipe);
  }
}