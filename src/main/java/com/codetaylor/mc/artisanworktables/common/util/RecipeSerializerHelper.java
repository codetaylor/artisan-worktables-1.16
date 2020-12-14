package com.codetaylor.mc.artisanworktables.common.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public final class RecipeSerializerHelper {

  public static FluidStack deserializeFluid(@Nonnull JsonObject json, int fallbackAmount) {

    ResourceLocation resourceLocation = new ResourceLocation(JSONUtils.getString(json, "fluid"));
    Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourceLocation);

    if (fluid == null || fluid == Fluids.EMPTY) {
      throw new JsonParseException("Unknown fluid: " + resourceLocation);
    }

    int amount = JSONUtils.getInt(json, "amount", fallbackAmount);

    return new FluidStack(fluid, amount);
  }

  public static Map<String, Ingredient> deserializeKey(JsonObject json) {

    Map<String, Ingredient> map = Maps.newHashMap();

    for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
      if (entry.getKey().length() != 1) {
        throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
      }

      if (" ".equals(entry.getKey())) {
        throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
      }

      map.put(entry.getKey(), Ingredient.deserialize(entry.getValue()));
    }

    map.put(" ", Ingredient.EMPTY);
    return map;
  }

  public static String[] shrink(String... toShrink) {

    int i = Integer.MAX_VALUE;
    int j = 0;
    int k = 0;
    int l = 0;

    for (int i1 = 0; i1 < toShrink.length; ++i1) {
      String s = toShrink[i1];
      i = Math.min(i, RecipeSerializerHelper.firstNonSpace(s));
      int j1 = RecipeSerializerHelper.lastNonSpace(s);
      j = Math.max(j, j1);

      if (j1 < 0) {
        if (k == i1) {
          ++k;
        }

        ++l;

      } else {
        l = 0;
      }
    }

    if (toShrink.length == l) {
      return new String[0];

    } else {
      String[] result = new String[toShrink.length - l - k];

      for (int k1 = 0; k1 < result.length; ++k1) {
        result[k1] = toShrink[k1 + k].substring(i, j + 1);
      }

      return result;
    }
  }

  public static int firstNonSpace(String str) {

    int i = 0;

    while (i < str.length() && str.charAt(i) == ' ') {
      ++i;
    }

    return i;
  }

  public static int lastNonSpace(String str) {

    int i = str.length() - 1;

    while (i >= 0 && str.charAt(i) == ' ') {
      --i;
    }

    return i;
  }

  public static String[] patternFromJson(JsonArray jsonArr, int maxWidth, int maxHeight) {

    String[] result = new String[jsonArr.size()];

    if (result.length > maxHeight) {
      throw new JsonSyntaxException("Invalid pattern: too many rows, " + maxHeight + " is maximum");

    } else if (result.length == 0) {
      throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");

    } else {

      for (int i = 0; i < result.length; ++i) {
        String s = JSONUtils.getString(jsonArr.get(i), "pattern[" + i + "]");

        if (s.length() > maxWidth) {
          throw new JsonSyntaxException("Invalid pattern: too many columns, " + maxWidth + " is maximum");
        }

        if (i > 0 && result[0].length() != s.length()) {
          throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
        }

        result[i] = s;
      }

      return result;
    }
  }

  public static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {

    NonNullList<Ingredient> result = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
    Set<String> set = Sets.newHashSet(keys.keySet());
    set.remove(" ");

    for (int i = 0; i < pattern.length; ++i) {

      for (int j = 0; j < pattern[i].length(); ++j) {
        String s = pattern[i].substring(j, j + 1);
        Ingredient ingredient = keys.get(s);

        if (ingredient == null) {
          throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
        }

        set.remove(s);
        result.set(j + patternWidth * i, ingredient);
      }
    }

    if (!set.isEmpty()) {
      throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);

    } else {
      return result;
    }
  }

  public static ItemStack deserializeItem(JsonObject object) {

    if (object.has("data")) {
      throw new JsonParseException("Disallowed data tag found");

    } else {
      return CraftingHelper.getItemStack(object, true);
    }
  }

  private RecipeSerializerHelper() {
    //
  }
}
