package com.codetaylor.mc.artisanworktables.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Collection;
import java.util.List;

public class SecondaryIngredientMatcher
    implements ISecondaryIngredientMatcher {

  private final int[] availableAmounts;
  private final List<ItemStack> inputs;

  public SecondaryIngredientMatcher(List<ItemStack> inputs) {

    this.inputs = inputs;
    this.availableAmounts = new int[this.inputs.size()];
  }

  @Override
  public boolean matches(Collection<Ingredient> requiredIngredients) {

    for (int i = 0; i < this.inputs.size(); i++) {
      ItemStack iItemStack = this.inputs.get(i);
      this.availableAmounts[i] = (iItemStack != null) ? iItemStack.getCount() : 0;
    }

    for (Ingredient recipeInput : requiredIngredients) {
      int amountRequired = 1; //recipeInput.getAmount();

      // Set the amount to 1 to avoid quantity discrepancies when matching
      //IIngredient toMatch = recipeInput.amount(1);

      for (int i = 0; i < this.inputs.size(); i++) {
        ItemStack input = this.inputs.get(i);

        if (input == null) {
          continue;
        }

        if (recipeInput.test(input)) {

          if (this.availableAmounts[i] >= amountRequired) {
            // more ingredients are available in this stack than are required
            // adjust and break
            this.availableAmounts[i] -= amountRequired;
            amountRequired = 0;
            break;

          } else {
            // there aren't enough ingredients available to satisfy the entire requirement
            // adjust and keep looking
            amountRequired -= this.availableAmounts[i];
            this.availableAmounts[i] = 0;
          }
        }
      }

      if (amountRequired > 0) {
        // the requirements were not met
        return false;
      }
    }

    return true;
  }
}
