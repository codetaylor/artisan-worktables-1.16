package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

public class RecipeSerializerShapedPacketWriter
    extends RecipeSerializerPacketWriter<ArtisanRecipeShaped> {

  @Override
  public void write(@Nonnull PacketBuffer buffer, @Nonnull ArtisanRecipeShaped recipe) {

    super.write(buffer, recipe);

    buffer.writeBoolean(recipe.isMirrored());
    buffer.writeInt(recipe.getWidth());
    buffer.writeInt(recipe.getHeight());
  }
}