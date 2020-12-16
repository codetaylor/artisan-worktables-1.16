package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import net.minecraft.item.crafting.IRecipeType;

import java.util.EnumMap;

public final class RecipeTypes {

  public static final EnumMap<EnumType, IRecipeType<? extends ArtisanRecipeShaped>> SHAPED_RECIPE_TYPES;
  public static final EnumMap<EnumType, IRecipeType<? extends ArtisanRecipeShapeless>> SHAPELESS_RECIPE_TYPES;

  static {
    SHAPED_RECIPE_TYPES = new EnumMap<>(EnumType.class);
    SHAPELESS_RECIPE_TYPES = new EnumMap<>(EnumType.class);

    for (EnumType type : EnumType.values()) {
      SHAPED_RECIPE_TYPES.put(type, IRecipeType.register(ArtisanWorktablesMod.MOD_ID + ":" + type.getName() + "_shaped"));
      SHAPELESS_RECIPE_TYPES.put(type, IRecipeType.register(ArtisanWorktablesMod.MOD_ID + ":" + type.getName() + "_shapeless"));
    }
  }

  private RecipeTypes() {
    //
  }
}
