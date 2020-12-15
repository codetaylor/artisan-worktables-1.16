package com.codetaylor.mc.artisanworktables.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nullable;

public class HashCodeUtil {

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
      builder.append(HashCodeUtil.get(itemStack));
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

  public static int get(ToolIngredientEntry entry) {

    return new HashCodeBuilder()
        .append(HashCodeUtil.get(entry.getTool()))
        .append(entry.getDamage())
        .build();
  }

  public static int get(ArtisanRecipe.ExtraOutputChancePair pair) {

    return new HashCodeBuilder()
        .append(HashCodeUtil.get(pair.getOutput()))
        .append(pair.getChance())
        .build();
  }

  private HashCodeUtil() {
    //
  }
}