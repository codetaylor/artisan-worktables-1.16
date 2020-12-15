package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShapeless;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

public class RecipeSerializerShapelessPacketWriter
    extends RecipeSerializerPacketWriter<ArtisanRecipeShapeless> {

  @Override
  public void write(@Nonnull PacketBuffer buffer, @Nonnull ArtisanRecipeShapeless recipe) {

    super.write(buffer, recipe);
  }
}