package com.codetaylor.mc.artisanworktables.common.recipe;

import net.minecraft.item.crafting.Ingredient;

import java.util.Collection;

public interface ISecondaryIngredientMatcher {

  ISecondaryIngredientMatcher FALSE = requiredIngredients -> false;

  boolean matches(Collection<Ingredient> requiredIngredients);
}
