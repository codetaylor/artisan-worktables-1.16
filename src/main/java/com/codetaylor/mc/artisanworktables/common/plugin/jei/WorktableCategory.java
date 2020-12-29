package com.codetaylor.mc.artisanworktables.common.plugin.jei;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.google.common.collect.Lists;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WorktableCategory
    extends BaseCategory<ArtisanRecipe> {

  public WorktableCategory(
      EnumTier tier,
      EnumType type,
      String titleKey,
      IDrawable background,
      IDrawable icon,
      ResourceLocation uid,
      ICraftingGridHelper craftingGridHelper
  ) {

    super(type, tier, titleKey, background, icon, uid, craftingGridHelper);
  }

  @Nonnull
  @Override
  public Class<? extends ArtisanRecipe> getRecipeClass() {

    return ArtisanRecipe.class;
  }

  @ParametersAreNonnullByDefault
  @Override
  public void setIngredients(ArtisanRecipe recipe, IIngredients ingredients) {

    List<Ingredient> inputList = new ArrayList<>(recipe.getIngredients());

    for (ToolEntry toolEntry : recipe.getTools()) {
      inputList.add(toolEntry.getTool());
    }

    inputList.addAll(recipe.getSecondaryIngredients());
    ingredients.setInputIngredients(inputList);

    ingredients.setInput(VanillaTypes.FLUID, recipe.getFluidIngredient());

    List<ItemStack> outputList = new ArrayList<>();
    outputList.add(recipe.getRecipeOutput());

    for (ArtisanRecipe.ExtraOutputChancePair extraOutput : recipe.getExtraOutputs()) {
      outputList.add(extraOutput.getOutput());
    }

    ingredients.setOutputs(VanillaTypes.ITEM, outputList);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void setRecipe(IRecipeLayout layout, ArtisanRecipe recipe, IIngredients ingredients) {

    IGuiItemStackGroup stacks = layout.getItemStacks();
    IGuiFluidStackGroup fluidStacks = layout.getFluidStacks();

    List<List<ItemStack>> tools = new ArrayList<>();

    for (ToolEntry tool : recipe.getTools()) {
      ItemStack[] matchingStacks = tool.getTool().getMatchingStacks();
      tools.add(Lists.newArrayList(matchingStacks));
    }

    List<Ingredient> inputList = new ArrayList<>(recipe.getIngredients());
    List<List<ItemStack>> inputs = new ArrayList<>();

    for (Ingredient ingredient : inputList) {
      if (ingredient == Ingredient.EMPTY) {
        inputs.add(Collections.emptyList());
      } else {
        inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
      }
    }

    stacks.init(0, false, 111, 31);
    stacks.set(0, Collections.singletonList(recipe.getRecipeOutput()));

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int index = 1 + x + (y * 3);
        stacks.init(index, true, x * 18 + 16, y * 18 + 13);
      }
    }

    if (recipe instanceof ArtisanRecipeShaped) {
      this.craftingGridHelper.setInputs(stacks, inputs, ((ArtisanRecipeShaped) recipe).getWidth(), ((ArtisanRecipeShaped) recipe).getHeight());

    } else {
      this.craftingGridHelper.setInputs(stacks, inputs);
    }

    stacks.init(10, true, 74, 31);

    if (tools.size() > 0) {
      stacks.set(10, tools.get(0));
    }

    stacks.init(11, false, 148, 13);
    stacks.init(12, false, 148, 31);
    stacks.init(13, false, 148, 49);

    NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs = recipe.getExtraOutputs();

    int nextExtraOutputSlot = 11;

    for (ArtisanRecipe.ExtraOutputChancePair extraOutput : extraOutputs) {
      ItemStack output = extraOutput.getOutput();
      stacks.set(nextExtraOutputSlot, output);
      nextExtraOutputSlot += 1;

      if (nextExtraOutputSlot == 14) {
        break;
      }
    }

    FluidStack fluidStack = recipe.getFluidIngredient();

    if (!fluidStack.isEmpty()) {
      fluidStacks.init(14, true, 5, 14, 6, 52, fluidStack.getAmount() * 2, false, null);
      fluidStacks.set(14, fluidStack);
    }

    layout.moveRecipeTransferButton(157, 67);
  }
}
