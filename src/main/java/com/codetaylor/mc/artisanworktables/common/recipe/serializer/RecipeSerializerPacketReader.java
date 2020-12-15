package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeBuilder;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public abstract class RecipeSerializerPacketReader<R extends ArtisanRecipe>
    implements IRecipeSerializerPacketReader<R> {

  protected void read(ArtisanRecipeBuilder builder, ResourceLocation recipeId, PacketBuffer buffer) {

    String group = buffer.readString();
    NonNullList<ToolEntry> tools = this.readTools(buffer);
    ItemStack result = buffer.readItemStack();
    NonNullList<Ingredient> ingredients = this.readIngredientList(buffer);
    NonNullList<Ingredient> secondaryIngredients = this.readIngredientList(buffer);
    boolean consumeSecondaryIngredients = buffer.readBoolean();
    FluidStack fluidIngredient = FluidStack.readFromPacket(buffer);
    NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutput = this.readExtraOutputs(buffer);
    int minimumTier = buffer.readInt();
    int maximumTier = buffer.readInt();
    int experienceRequired = buffer.readInt();
    int levelRequired = buffer.readInt();
    boolean consumeExperience = buffer.readBoolean();

    builder
        .setRecipeId(recipeId)
        .setGroup(group)
        .setResult(result)
        .setIngredients(ingredients)
        .setTools(tools)
        .setSecondaryIngredients(secondaryIngredients)
        .setConsumeSecondaryIngredients(consumeSecondaryIngredients)
        .setFluidIngredient(fluidIngredient)
        .setExtraOutputs(extraOutput)
        .setMinimumTier(minimumTier)
        .setMaximumTier(maximumTier)
        .setExperienceRequired(experienceRequired)
        .setLevelRequired(levelRequired)
        .setConsumeExperience(consumeExperience);
  }

  protected NonNullList<ArtisanRecipe.ExtraOutputChancePair> readExtraOutputs(PacketBuffer buffer) {

    int size = buffer.readInt();
    NonNullList<ArtisanRecipe.ExtraOutputChancePair> result = NonNullList.create();

    for (int i = 0; i < size; i++) {
      ItemStack itemStack = buffer.readItemStack();
      float chance = buffer.readFloat();
      result.add(new ArtisanRecipe.ExtraOutputChancePair(itemStack, chance));
    }

    return result;
  }

  protected NonNullList<Ingredient> readIngredientList(PacketBuffer buffer) {

    int size = buffer.readInt();
    NonNullList<Ingredient> result = NonNullList.create();

    for (int i = 0; i < size; i++) {
      Ingredient ingredient = Ingredient.read(buffer);
      result.add(ingredient);
    }

    return result;
  }

  protected NonNullList<ToolEntry> readTools(PacketBuffer buffer) {

    int size = buffer.readInt();
    NonNullList<ToolEntry> result = NonNullList.create();

    for (int i = 0; i < size; i++) {
      Ingredient ingredient = Ingredient.read(buffer);
      int damage = buffer.readInt();
      result.add(new ToolEntry(ingredient, damage));
    }

    return result;
  }
}
