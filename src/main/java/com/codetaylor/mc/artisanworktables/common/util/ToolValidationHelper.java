package com.codetaylor.mc.artisanworktables.common.util;

import com.codetaylor.mc.artisanworktables.api.IToolHandler;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanToolHandlers;
import com.codetaylor.mc.artisanworktables.common.recipe.RecipeTypes;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.EnumMap;
import java.util.List;

/**
 * Responsible for checking all AW recipes for the use of the given tool.
 * <p>
 * Caches results for performance.
 * <p>
 * Uses a synchronized block because it is called from both threads on the
 * same machine in a single player game.
 */
public final class ToolValidationHelper {

  private static final EnumMap<EnumType, Object2BooleanMap<ResourceLocation>> TYPE_CACHE;
  private static final Object2BooleanMap<ResourceLocation> ALL_CACHE;

  static {
    TYPE_CACHE = new EnumMap<>(EnumType.class);
    ALL_CACHE = new Object2BooleanOpenHashMap<>();
  }

  public static void clear() {

    synchronized (TYPE_CACHE) {
      TYPE_CACHE.clear();
      ALL_CACHE.clear();
    }
  }

  public static boolean isValidTool(ItemStack tool, RecipeManager recipeManager) {

    synchronized (ALL_CACHE) {

      ResourceLocation resourceLocation = tool.getItem().getRegistryName();

      if (ALL_CACHE.containsKey(resourceLocation)) {
        return ALL_CACHE.getBoolean(resourceLocation);
      }

      for (EnumType type : EnumType.values()) {

        if (ToolValidationHelper.isValidTool(type, tool, recipeManager)) {
          ALL_CACHE.put(resourceLocation, true);
          return true;
        }
      }

      ALL_CACHE.put(resourceLocation, false);
      return false;
    }
  }

  public static boolean isValidTool(EnumType type, ItemStack tool, RecipeManager recipeManager) {

    synchronized (TYPE_CACHE) {
      ResourceLocation resourceLocation = tool.getItem().getRegistryName();

      Object2BooleanMap<ResourceLocation> cache = TYPE_CACHE.computeIfAbsent(type, (t) -> new Object2BooleanOpenHashMap<>());

      if (cache.containsKey(resourceLocation)) {
        return cache.getBoolean(resourceLocation);
      }

      IToolHandler toolHandler = ArtisanToolHandlers.get(tool);

      boolean result = false;

      if (ToolValidationHelper.checkRecipeType(toolHandler, tool, recipeManager, RecipeTypes.SHAPED_RECIPE_TYPES.get(type))) {
        result = true;
      }

      if (!result) {

        if (ToolValidationHelper.checkRecipeType(toolHandler, tool, recipeManager, RecipeTypes.SHAPELESS_RECIPE_TYPES.get(type))) {
          result = true;
        }
      }

      cache.put(resourceLocation, result);
      return result;
    }
  }

  private static boolean checkRecipeType(IToolHandler toolHandler, ItemStack tool, RecipeManager recipeManager, IRecipeType<? extends ArtisanRecipe> recipeType) {

    return ToolValidationHelper.checkList(toolHandler, tool, recipeManager.getRecipesForType(recipeType));
  }

  private static boolean checkList(IToolHandler toolHandler, ItemStack tool, List<? extends ArtisanRecipe> recipeList) {

    for (ArtisanRecipe artisanRecipe : recipeList) {
      NonNullList<ToolEntry> tools = artisanRecipe.getTools();

      for (ToolEntry toolEntry : tools) {
        ItemStack[] toolStacks = toolEntry.getToolStacks();

        for (ItemStack recipeTool : toolStacks) {

          if (toolHandler.matches(tool, recipeTool)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  private ToolValidationHelper() {
    //
  }
}
