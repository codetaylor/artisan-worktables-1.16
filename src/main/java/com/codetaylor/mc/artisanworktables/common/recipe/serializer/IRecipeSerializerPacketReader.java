package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRecipeSerializerPacketReader<T extends IRecipe<?>> {

  @Nullable
  T read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer);
}
