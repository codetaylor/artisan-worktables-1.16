package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class RecipeSerializerPacketWriter<R extends ArtisanRecipe>
    implements IRecipeSerializerPacketWriter<R> {

  @Override
  public void write(@Nonnull PacketBuffer buffer, @Nonnull R recipe) {

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
  }

  protected void writeExtraOutputs(PacketBuffer buffer, NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs) {

    buffer.writeInt(extraOutputs.size());

    for (ArtisanRecipe.ExtraOutputChancePair extraOutput : extraOutputs) {
      buffer.writeItemStack(extraOutput.getOutput());
      buffer.writeFloat(extraOutput.getChance());
    }
  }

  protected void writeIngredientList(PacketBuffer buffer, List<Ingredient> ingredients) {

    buffer.writeInt(ingredients.size());

    for (Ingredient ingredient : ingredients) {
      ingredient.write(buffer);
    }
  }

  protected void writeTools(PacketBuffer buffer, NonNullList<ToolEntry> tools) {

    buffer.writeInt(tools.size());

    for (ToolEntry tool : tools) {
      tool.getTool().write(buffer);
      buffer.writeInt(tool.getDamage());
    }
  }
}
