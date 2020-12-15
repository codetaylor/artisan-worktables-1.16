package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.recipe.RecipeSerializerShaped;
import com.codetaylor.mc.artisanworktables.common.recipe.RecipeSerializerShapedJsonReader;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class RecipeSerializerRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegistryEvent.Register<IRecipeSerializer<? extends IRecipe<?>>> event) {

    IForgeRegistry<IRecipeSerializer<? extends IRecipe<?>>> registry = event.getRegistry();

    registry.register(new RecipeSerializerShaped(
        new RecipeSerializerShapedJsonReader(5, 5)
    ).setRegistryName(new ResourceLocation(ArtisanWorktablesMod.MOD_ID, "recipe_serializer_shaped")));
  }
}