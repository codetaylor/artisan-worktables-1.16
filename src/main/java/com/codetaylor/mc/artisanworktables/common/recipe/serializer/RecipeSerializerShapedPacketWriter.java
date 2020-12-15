package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;
import java.util.List;

public class RecipeSerializerShapedPacketWriter
    implements IRecipeSerializerPacketWriter<ArtisanRecipeShaped> {

  @Override
  public void write(@Nonnull PacketBuffer buffer, @Nonnull ArtisanRecipeShaped recipe) {

    // Common between shaped and shapeless
    buffer.writeString(recipe.getGroup());
    this.writeTools(buffer, recipe.getTools());
    buffer.writeItemStack(recipe.getRecipeOutput());
    this.writeIngredientList(buffer, recipe.getIngredients());
    this.writeIngredientList(buffer, recipe.getSecondaryIngredients());
    buffer.writeBoolean(recipe.consumeSecondaryIngredients());
    recipe.getFluidIngredient().writeToPacket(buffer);
    this.writeExtraOutputs(buffer, recipe.getExtraOutputs());
    buffer.writeInt(recipe.getMinimumTier());
    buffer.writeInt(recipe.getMaximumTier());
    buffer.writeInt(recipe.getExperienceRequired());
    buffer.writeInt(recipe.getLevelRequired());
    buffer.writeBoolean(recipe.consumeExperience());

    // Shaped only
    buffer.writeBoolean(recipe.isMirrored());
    buffer.writeInt(recipe.getWidth());
    buffer.writeInt(recipe.getHeight());
  }

  private void writeExtraOutputs(PacketBuffer buffer, ArtisanRecipe.ExtraOutputChancePair[] extraOutputs) {

    buffer.writeInt(extraOutputs.length);

    for (ArtisanRecipe.ExtraOutputChancePair extraOutput : extraOutputs) {
      buffer.writeItemStack(extraOutput.getOutput());
      buffer.writeFloat(extraOutput.getChance());
    }
  }

  private void writeIngredientList(PacketBuffer buffer, List<Ingredient> ingredients) {

    buffer.writeInt(ingredients.size());

    for (Ingredient ingredient : ingredients) {
      ingredient.write(buffer);
    }
  }

  private void writeTools(PacketBuffer buffer, ToolEntry[] tools) {

    buffer.writeInt(tools.length);

    for (ToolEntry tool : tools) {
      tool.getTool().write(buffer);
      buffer.writeInt(tool.getDamage());
    }
  }
}