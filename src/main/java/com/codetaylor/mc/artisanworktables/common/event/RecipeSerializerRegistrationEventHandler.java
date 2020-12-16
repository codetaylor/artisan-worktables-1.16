package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShapeless;
import com.codetaylor.mc.artisanworktables.common.recipe.serializer.*;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.reference.Reference;
import com.codetaylor.mc.artisanworktables.common.util.Key;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.EnumMap;

public class RecipeSerializerRegistrationEventHandler {

  private final EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShaped;
  private final EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShapeless;

  public RecipeSerializerRegistrationEventHandler(
      EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShaped,
      EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> registeredSerializersShapeless
  ) {

    this.registeredSerializersShaped = registeredSerializersShaped;
    this.registeredSerializersShapeless = registeredSerializersShapeless;
  }

  @SubscribeEvent
  public void on(RegistryEvent.Register<IRecipeSerializer<?>> event) {

    IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

    for (EnumType type : EnumType.values()) {
      String name = type.getName();
      {
        RecipeSerializer<ArtisanRecipeShaped> serializer = new RecipeSerializer<>(
            new RecipeSerializerShapedJsonReader(type, Reference.MAX_RECIPE_WIDTH, Reference.MAX_RECIPE_HEIGHT),
            new RecipeSerializerShapedPacketReader(type),
            new RecipeSerializerShapedPacketWriter()
        );
        serializer.setRegistryName(Key.from(name + "_shaped"));
        this.registeredSerializersShaped.put(type, serializer);
        registry.register(serializer);
      }
      {
        RecipeSerializer<ArtisanRecipeShapeless> serializer = new RecipeSerializer<>(
            new RecipeSerializerShapelessJsonReader(type, Reference.MAX_RECIPE_WIDTH, Reference.MAX_RECIPE_HEIGHT),
            new RecipeSerializerShapelessPacketReader(type),
            new RecipeSerializerShapelessPacketWriter()
        );
        serializer.setRegistryName(Key.from(name + "_shapeless"));
        this.registeredSerializersShapeless.put(type, serializer);
        registry.register(serializer);
      }
    }
  }
}