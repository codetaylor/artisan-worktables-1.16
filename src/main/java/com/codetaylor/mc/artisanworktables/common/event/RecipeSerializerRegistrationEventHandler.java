package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.common.recipe.serializer.*;
import com.codetaylor.mc.artisanworktables.common.reference.Reference;
import com.codetaylor.mc.artisanworktables.common.util.Key;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class RecipeSerializerRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegistryEvent.Register<IRecipeSerializer<?>> event) {

    IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

    registry.register(new RecipeSerializer<>(
        new RecipeSerializerShapedJsonReader(
            Reference.MAX_RECIPE_WIDTH,
            Reference.MAX_RECIPE_HEIGHT
        ),
        new RecipeSerializerShapedPacketReader(),
        new RecipeSerializerShapedPacketWriter()
    ).setRegistryName(Key.from("shaped")));

    registry.register(new RecipeSerializer<>(
        new RecipeSerializerShapelessJsonReader(
            Reference.MAX_RECIPE_WIDTH,
            Reference.MAX_RECIPE_HEIGHT
        ),
        new RecipeSerializerShapelessPacketReader(),
        new RecipeSerializerShapelessPacketWriter()
    ).setRegistryName(Key.from("shapeless")));
  }
}