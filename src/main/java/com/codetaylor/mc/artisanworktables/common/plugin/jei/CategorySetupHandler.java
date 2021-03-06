package com.codetaylor.mc.artisanworktables.common.plugin.jei;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.google.common.collect.Lists;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CategorySetupHandler {

  private final EnumTier tier;
  private final ICraftingGridHelper craftingGridHelper;

  public CategorySetupHandler(EnumTier tier, ICraftingGridHelper craftingGridHelper) {

    this.tier = tier;
    this.craftingGridHelper = craftingGridHelper;
  }

  public void setup(ArtisanRecipe recipe, IRecipeLayout layout) {

    IGuiItemStackGroup stacks = layout.getItemStacks();
    IGuiFluidStackGroup fluidStacks = layout.getFluidStacks();

    int[] index = new int[1];

    this.setupOutput(index, recipe, stacks, this.tier);
    this.setupCraftingGrid(index, recipe, stacks, this.tier, this.craftingGridHelper);
    this.setupTools(index, stacks, recipe, this.tier);
    this.setupExtraOutputs(index, recipe, stacks, this.tier);
    this.setupFluid(index, recipe, fluidStacks, this.tier);
    this.setupSecondaryIngredients(index, recipe, stacks, this.tier);
    this.setupTransferButton(layout, this.tier);
  }

  private void setupTransferButton(IRecipeLayout layout, EnumTier tier) {

    switch (tier) {
      case WORKTABLE:
        layout.moveRecipeTransferButton(157, 67);
        break;
      case WORKSTATION:
        layout.moveRecipeTransferButton(157, 89);
        break;
      case WORKSHOP:
        layout.moveRecipeTransferButton(157, 115);
        break;
    }
  }

  private void setupSecondaryIngredients(int[] index, ArtisanRecipe recipe, IGuiItemStackGroup stacks, EnumTier tier) {

    if (tier == EnumTier.WORKSTATION || tier == EnumTier.WORKSHOP) {
      int yPos = (tier == EnumTier.WORKSTATION) ? 71 : 97;
      NonNullList<Ingredient> secondaryIngredients = recipe.getSecondaryIngredients();

      for (int i = 0; i < 9; i++) {
        // The input flag is set to false here to prevent JEI from trying to transfer
        // recipe items into these slots when the transfer button is clicked.

        if (i + 1 <= secondaryIngredients.size()) {
          stacks.init(index[0], false, 4 + (18 * i), yPos);
          stacks.set(index[0], Arrays.asList(secondaryIngredients.get(i).getMatchingStacks()));
          index[0] += 1;
        }
      }
    }
  }

  private void setupFluid(int[] index, ArtisanRecipe recipe, IGuiFluidStackGroup fluidStacks, EnumTier tier) {

    FluidStack fluidStack = recipe.getFluidIngredient();

    if (!fluidStack.isEmpty()) {

      if (tier == EnumTier.WORKTABLE || tier == EnumTier.WORKSTATION) {
        fluidStacks.init(index[0], true, 5, 14, 6, 52, fluidStack.getAmount() * 2, false, null);
        fluidStacks.set(index[0], fluidStack);

      } else if (tier == EnumTier.WORKSHOP) {
        fluidStacks.init(index[0], true, 5, 4, 6, 88, fluidStack.getAmount() * 2, false, null);
        fluidStacks.set(index[0], fluidStack);
      }

      index[0] += 1;
    }
  }

  private void setupExtraOutputs(int[] index, ArtisanRecipe recipe, IGuiItemStackGroup stacks, EnumTier tier) {

    NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs = recipe.getExtraOutputs();
    int size = Math.min(extraOutputs.size(), 3);

    if (tier == EnumTier.WORKTABLE || tier == EnumTier.WORKSTATION) {

      for (int i = 0; i < size; i++) {
        stacks.init(index[0], false, 148, 13 + 18 * i);
        ArtisanRecipe.ExtraOutputChancePair extraOutput = extraOutputs.get(i);
        ItemStack output = extraOutput.getOutput();
        stacks.set(index[0], output);
        index[0] += 1;
      }

    } else if (tier == EnumTier.WORKSHOP) {

      for (int i = 0; i < size; i++) {
        stacks.init(index[0], false, 112 + 18 * i, 3);
        ArtisanRecipe.ExtraOutputChancePair extraOutput = extraOutputs.get(i);
        ItemStack output = extraOutput.getOutput();
        stacks.set(index[0], output);
        index[0] += 1;
      }
    }
  }

  private void setupTools(int[] index, IGuiItemStackGroup stacks, ArtisanRecipe recipe, EnumTier tier) {

    List<List<ItemStack>> tools = new ArrayList<>();

    for (ToolEntry tool : recipe.getTools()) {
      ItemStack[] matchingStacks = tool.getTool().getMatchingStacks();
      tools.add(Lists.newArrayList(matchingStacks));
    }

    if (tier == EnumTier.WORKTABLE) {

      if (tools.size() > 0) {
        stacks.init(index[0], true, 74, 31);
        stacks.set(index[0], tools.get(0));
        index[0] += 1;
      }

    } else if (tier == EnumTier.WORKSTATION) {

      if (tools.size() > 0) {
        stacks.init(index[0], true, 74, 20);
        stacks.set(index[0], tools.get(0));
        index[0] += 1;
      }

      if (tools.size() > 1) {
        stacks.init(index[0], true, 74, 20 + 22);
        stacks.set(index[0], tools.get(1));
        index[0] += 1;
      }

    } else if (tier == EnumTier.WORKSHOP) {

      if (tools.size() > 0) {
        stacks.init(index[0], true, 110, 26);
        stacks.set(index[0], tools.get(0));
        index[0] += 1;
      }

      if (tools.size() > 1) {
        stacks.init(index[0], true, 110, 26 + 22);
        stacks.set(index[0], tools.get(1));
        index[0] += 1;
      }

      if (tools.size() > 2) {
        stacks.init(index[0], true, 110, 26 + 44);
        stacks.set(index[0], tools.get(2));
        index[0] += 1;
      }
    }
  }

  private void setupCraftingGrid(int[] index, ArtisanRecipe recipe, IGuiItemStackGroup stacks, EnumTier tier, ICraftingGridHelper craftingGridHelper) {

    List<List<ItemStack>> inputs = new ArrayList<>();

    for (Ingredient ingredient : recipe.getIngredients()) {
      if (ingredient == Ingredient.EMPTY) {
        inputs.add(Collections.emptyList());
      } else {
        inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
      }
    }

    if (tier == EnumTier.WORKSHOP) {

      for (int y = 0; y < 5; y++) {

        for (int x = 0; x < 5; x++) {
          stacks.init(index[0], true, x * 18 + 16, y * 18 + 3);
          index[0] += 1;
        }
      }

    } else {

      for (int y = 0; y < 3; y++) {

        for (int x = 0; x < 3; x++) {
          stacks.init(index[0], true, x * 18 + 16, y * 18 + 13);
          index[0] += 1;
        }
      }
    }

    if (recipe instanceof ArtisanRecipeShaped) {
      craftingGridHelper.setInputs(stacks, inputs, ((ArtisanRecipeShaped) recipe).getWidth(), ((ArtisanRecipeShaped) recipe).getHeight());

    } else {
      craftingGridHelper.setInputs(stacks, inputs);
    }
  }

  private void setupOutput(int[] index, ArtisanRecipe recipe, IGuiItemStackGroup stacks, EnumTier tier) {

    if (tier == EnumTier.WORKTABLE || tier == EnumTier.WORKSTATION) {
      stacks.init(index[0], false, 111, 31);
      stacks.set(index[0], Collections.singletonList(recipe.getRecipeOutput()));
      index[0] += 1;

    } else if (tier == EnumTier.WORKSHOP) {
      stacks.init(index[0], false, 139, 48);
      stacks.set(index[0], Collections.singletonList(recipe.getRecipeOutput()));
      index[0] += 1;
    }
  }
}
