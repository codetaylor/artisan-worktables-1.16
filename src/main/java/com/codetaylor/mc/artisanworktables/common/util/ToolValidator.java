package com.codetaylor.mc.artisanworktables.common.util;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShapeless;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ToolValidator {

  private static final ThreadLocal<Object2BooleanMap<ResourceLocation>> CACHE;

  static {
    CACHE = ThreadLocal.withInitial(Object2BooleanOpenHashMap::new);
  }

  public static boolean isValidTool(ItemStack tool, RecipeManager recipeManager) {

    ResourceLocation resourceLocation = tool.getItem().getRegistryName();
    Object2BooleanMap<ResourceLocation> cache = CACHE.get();

    if (cache.containsKey(resourceLocation)) {
      return cache.getBoolean(resourceLocation);
    }

    List<ArtisanRecipeShaped> shapedList = recipeManager.getRecipesForType(ArtisanWorktablesMod.RecipeTypes.SHAPED);
    boolean result = false;

    if (ToolValidator.checkList(tool, shapedList)) {
      result = true;

    } else {
      List<ArtisanRecipeShapeless> shapelessList = recipeManager.getRecipesForType(ArtisanWorktablesMod.RecipeTypes.SHAPELESS);

      if (ToolValidator.checkList(tool, shapelessList)) {
        result = true;
      }
    }

    cache.put(resourceLocation, result);
    return result;
  }

  private static boolean checkList(ItemStack tool, List<? extends ArtisanRecipe> recipeList) {

    for (ArtisanRecipe artisanRecipeShaped : recipeList) {
      NonNullList<ToolEntry> tools = artisanRecipeShaped.getTools();

      for (ToolEntry toolEntry : tools) {
        boolean valid = toolEntry.getTool().test(tool);

        if (valid) {
          return true;
        }
      }
    }

    return false;
  }

}
