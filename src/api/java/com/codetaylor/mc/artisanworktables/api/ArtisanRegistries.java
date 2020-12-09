package com.codetaylor.mc.artisanworktables.api;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementBuilderSupplier;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementContextSupplier;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class ArtisanRegistries {

  public static final Map<ResourceLocation, RequirementContextSupplier> REQUIREMENT_CONTEXT_SUPPLIER_REGISTRY;
  public static final Map<ResourceLocation, RequirementBuilderSupplier<?, ?>> REQUIREMENT_BUILDER_SUPPLIER_REGISTRY;
  public static final Map<ResourceLocation, RecipeRegistry> RECIPE_REGISTRY;

  static {
    REQUIREMENT_CONTEXT_SUPPLIER_REGISTRY = new HashMap<>();
    REQUIREMENT_BUILDER_SUPPLIER_REGISTRY = new HashMap<>();
    RECIPE_REGISTRY = new HashMap<>();
  }

  private ArtisanRegistries() {
    //
  }
}