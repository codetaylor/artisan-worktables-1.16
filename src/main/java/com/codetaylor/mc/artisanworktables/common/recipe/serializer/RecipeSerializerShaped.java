package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeSerializerShaped
    extends ForgeRegistryEntry<IRecipeSerializer<?>>
    implements IRecipeSerializer<ArtisanRecipeShaped> {

  private final IRecipeSerializerJsonReader<ArtisanRecipeShaped> recipeSerializerJsonReader;
  private final IRecipeSerializerPacketReader<ArtisanRecipeShaped> recipeSerializerPacketReader;
  private final IRecipeSerializerPacketWriter<ArtisanRecipeShaped> recipeSerializerPacketWriter;

  public RecipeSerializerShaped(
      IRecipeSerializerJsonReader<ArtisanRecipeShaped> recipeSerializerJsonReader,
      IRecipeSerializerPacketReader<ArtisanRecipeShaped> recipeSerializerPacketReader,
      IRecipeSerializerPacketWriter<ArtisanRecipeShaped> recipeSerializerPacketWriter
  ) {

    this.recipeSerializerJsonReader = recipeSerializerJsonReader;
    this.recipeSerializerPacketReader = recipeSerializerPacketReader;
    this.recipeSerializerPacketWriter = recipeSerializerPacketWriter;
  }

  @Nonnull
  @Override
  public ArtisanRecipeShaped read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {

    return this.recipeSerializerJsonReader.read(recipeId, json);
  }

  @Nullable
  @Override
  public ArtisanRecipeShaped read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {

    return this.recipeSerializerPacketReader.read(recipeId, buffer);
  }

  @Override
  public void write(@Nonnull PacketBuffer buffer, @Nonnull ArtisanRecipeShaped recipe) {

    this.recipeSerializerPacketWriter.write(buffer, recipe);
  }
}