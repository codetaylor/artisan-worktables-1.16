package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeBuilder;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeSerializerShapedPacketReader
    extends RecipeSerializerPacketReader<ArtisanRecipeShaped> {

  private final EnumType type;

  public RecipeSerializerShapedPacketReader(EnumType type) {

    this.type = type;
  }

  @Nullable
  @Override
  public ArtisanRecipeShaped read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {

    ArtisanRecipeBuilder builder = new ArtisanRecipeBuilder();

    this.read(builder, recipeId, buffer);

    boolean mirrored = buffer.readBoolean();
    int width = buffer.readInt();
    int height = buffer.readInt();

    try {
      return builder
          .setMirrored(mirrored)
          .setWidth(width)
          .setHeight(height)
          .buildShaped(this.type);

    } catch (Exception e) {
      throw new RuntimeException("Error building recipe: " + recipeId, e);
    }
  }
}