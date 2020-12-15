package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

public interface IRecipeSerializerPacketWriter<T extends IRecipe<?>> {

  void write(@Nonnull PacketBuffer buffer, @Nonnull T recipe);
}
