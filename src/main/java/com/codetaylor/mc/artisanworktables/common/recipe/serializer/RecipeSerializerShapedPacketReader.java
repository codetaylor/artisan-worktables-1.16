package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeSerializerShapedPacketReader
    implements IRecipeSerializerPacketReader<ArtisanRecipeShaped> {

  @Nullable
  @Override
  public ArtisanRecipeShaped read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {

    // TODO
    return null;
  }
}
