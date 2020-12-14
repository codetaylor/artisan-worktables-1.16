package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.common.util.RecipeSerializerHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeSerializerShaped
    extends ForgeRegistryEntry<IRecipeSerializer<?>>
    implements IRecipeSerializer<ArtisanRecipe> {

  private static final int MAX_WIDTH = 5;
  private static final int MAX_HEIGHT = 5;

  @Nonnull
  @Override
  public ArtisanRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {

    String group = JSONUtils.getString(json, "group", "");
    Map<String, Ingredient> keys = RecipeSerializerHelper.deserializeKey(JSONUtils.getJsonObject(json, "key"));

    String[] pattern = RecipeSerializerHelper.shrink(RecipeSerializerHelper.patternFromJson(JSONUtils.getJsonArray(json, "pattern"), MAX_WIDTH, MAX_HEIGHT));
    int width = pattern[0].length();
    int height = pattern.length;
    NonNullList<Ingredient> ingredients = RecipeSerializerHelper.deserializeIngredients(pattern, keys, width, height);
    ItemStack result = RecipeSerializerHelper.deserializeItem(JSONUtils.getJsonObject(json, "result"));
    ToolEntry[] tools = this.deserializeTools(json);
    List<Ingredient> secondaryIngredients = this.deserializeSecondaryIngredients(json);
    boolean consumeSecondaryIngredients = JSONUtils.getBoolean(json, "consumeSecondaryIngredients", true);
    FluidStack fluidIngredient = this.deserializeFluidIngredient(json);
    ArtisanRecipe.ExtraOutputChancePair[] extraOutput = this.deserializeExtraOutput(json);

    boolean mirrored = JSONUtils.getBoolean(json, "mirrored", true);
    int minimumTier = JSONUtils.getInt(json, "minimumTier", 0);
    int maximumTier = JSONUtils.getInt(json, "maximumTier", 2);
    int experienceRequired = JSONUtils.getInt(json, "experienceRequired", 0);
    int levelRequired = JSONUtils.getInt(json, "levelRequired", 0);
    boolean consumeExperience = JSONUtils.getBoolean(json, "consumeExperience", true);

    return new ArtisanRecipeShaped(
        recipeId,
        group,
        tools,
        result,
        ingredients,
        secondaryIngredients,
        consumeSecondaryIngredients,
        fluidIngredient,
        extraOutput,
        mirrored,
        minimumTier,
        maximumTier,
        experienceRequired,
        levelRequired,
        consumeExperience,
        width,
        height
    );
  }

  private ArtisanRecipe.ExtraOutputChancePair[] deserializeExtraOutput(JsonObject json) {

    List<ArtisanRecipe.ExtraOutputChancePair> result = new ArrayList<>();

    if (json.has("extraOutput")) {
      JsonArray jsonArray = JSONUtils.getJsonArray(json, "extraOutput");

      for (JsonElement jsonElement : jsonArray) {
        JsonObject itemObject = jsonElement.getAsJsonObject();
        ItemStack itemStack = RecipeSerializerHelper.deserializeItem(itemObject);
        float chance = JSONUtils.getFloat(itemObject, "chance", 1);
        result.add(new ArtisanRecipe.ExtraOutputChancePair(itemStack, chance));
      }
    }

    return result.toArray(new ArtisanRecipe.ExtraOutputChancePair[0]);
  }

  private FluidStack deserializeFluidIngredient(@Nonnull JsonObject json) {

    FluidStack fluidIngredient;
    if (json.has("fluidIngredient")) {
      fluidIngredient = RecipeSerializerHelper.deserializeFluid(JSONUtils.getJsonObject(json, "fluidIngredient", new JsonObject()), 1000);

    } else {
      fluidIngredient = FluidStack.EMPTY;
    }
    return fluidIngredient;
  }

  private List<Ingredient> deserializeSecondaryIngredients(JsonObject json) {

    List<Ingredient> result = new ArrayList<>(3);

    if (json.has("secondaryIngredients")) {
      JsonArray jsonArray = JSONUtils.getJsonArray(json, "secondaryIngredients");

      for (JsonElement jsonElement : jsonArray) {
        JsonObject ingredientObject = jsonElement.getAsJsonObject();
        Ingredient ingredient = Ingredient.deserialize(ingredientObject);
        result.add(ingredient);
      }
    }

    if (result.size() > 9) {
      throw new JsonParseException("Secondary ingredient count cannot be greater than 9, was " + result.size());
    }

    return result;
  }

  private ToolEntry[] deserializeTools(@Nonnull JsonObject json) {

    List<ToolEntry> result = new ArrayList<>(3);
    {
      JsonArray toolArray = JSONUtils.getJsonArray(json, "tools", new JsonArray());

      if (toolArray != null) {

        for (JsonElement jsonElement : toolArray) {
          JsonObject toolObject = jsonElement.getAsJsonObject();
          Ingredient tool = Ingredient.deserialize(toolObject);
          int damage = JSONUtils.getInt(toolObject, "damage", 1);
          result.add(new ToolEntry(tool, damage));
        }

        if (result.size() > 3) {
          throw new JsonParseException("Tool count cannot be greater than 3, was " + result.size());
        }
      }
    }
    return result.toArray(new ToolEntry[0]);
  }

  @Nullable
  @Override
  public ArtisanRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {

    return null;
  }

  @Override
  public void write(@Nonnull PacketBuffer buffer, @Nonnull ArtisanRecipe recipe) {

  }

}
