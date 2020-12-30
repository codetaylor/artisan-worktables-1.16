package com.codetaylor.mc.artisanworktables.common.plugin.jei;

import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;

import javax.annotation.Nonnull;
import java.util.List;

public class WorkshopCraftingGridHelper
    implements ICraftingGridHelper {

  private final int craftInputSlot;

  public WorkshopCraftingGridHelper(int craftInputSlot) {

    this.craftInputSlot = craftInputSlot;
  }

  @Override
  public <T> void setInputs(@Nonnull IGuiIngredientGroup<T> ingredientGroup, List<List<T>> inputs) {

    int width, height;

    if (inputs.size() > 16) {
      width = height = 5;

    } else if (inputs.size() > 9) {
      width = height = 4;

    } else if (inputs.size() > 4) {
      width = height = 3;

    } else if (inputs.size() > 1) {
      width = height = 2;

    } else {
      width = height = 1;
    }

    setInputs(ingredientGroup, inputs, width, height);
  }

  @Override
  public <T> void setInputs(@Nonnull IGuiIngredientGroup<T> ingredientGroup, List<List<T>> inputs, int width, int height) {

    for (int i = 0; i < inputs.size(); i++) {
      List<T> recipeItem = inputs.get(i);
      int index = getCraftingIndex(i, width);

      setInput(ingredientGroup, index, recipeItem);
    }
  }

  private <T> void setInput(IGuiIngredientGroup<T> guiIngredients, int inputIndex, List<T> input) {

    guiIngredients.set(this.craftInputSlot + inputIndex, input);
  }

  private int getCraftingIndex(int i, int width) {

    return (i / width) * 5 + (i % width);
  }
}