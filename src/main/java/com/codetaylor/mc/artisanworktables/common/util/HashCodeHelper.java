package com.codetaylor.mc.artisanworktables.common.util;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolIngredientEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nullable;

public final class HashCodeHelper {

  public static int get(ItemStack itemStack) {

    HashCodeBuilder builder = new HashCodeBuilder()
        .append(itemStack.getCount())
        .append(itemStack.getItem().getRegistryName())
        .append(itemStack.getDamage());

    if (itemStack.getTag() != null) {
      builder.append(itemStack.getTag().hashCode());
    }

    return builder.build();
  }

  public static int get(Ingredient ingredient) {

    HashCodeBuilder builder = new HashCodeBuilder();
    ItemStack[] matchingStacks = ingredient.getMatchingStacks();

    for (ItemStack itemStack : matchingStacks) {
      builder.append(HashCodeHelper.get(itemStack));
    }

    return builder.build();
  }

  public static int get(@Nullable FluidStack fluidStack) {

    HashCodeBuilder builder = new HashCodeBuilder();

    if (fluidStack != null) {
      builder.append(fluidStack.getFluid().getClass().getName().hashCode());
      builder.append(fluidStack.getAmount());

      if (fluidStack.getTag() != null) {
        builder.append(fluidStack.getTag().hashCode());
      }
    }

    return builder.build();
  }

  public static int get(ToolEntry entry) {

    return new HashCodeBuilder()
        .append(HashCodeHelper.get(entry.getTool()))
        .append(entry.getDamage())
        .build();
  }

  public static int get(ArtisanRecipe.ExtraOutputChancePair pair) {

    return new HashCodeBuilder()
        .append(HashCodeHelper.get(pair.getOutput()))
        .append(pair.getChance())
        .build();
  }

  private HashCodeHelper() {
    //
  }
}