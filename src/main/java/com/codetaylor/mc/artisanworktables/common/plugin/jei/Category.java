package com.codetaylor.mc.artisanworktables.common.plugin.jei;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class Category
    extends BaseCategory<ArtisanRecipe> {

  private final CategorySetupHandler categorySetupHandler;
  private final CategoryDrawHandler categoryDrawHandler;

  public Category(
      EnumTier tier,
      EnumType type,
      String titleKey,
      IDrawable background,
      IDrawable icon,
      ResourceLocation uid,
      CategorySetupHandler categorySetupHandler,
      CategoryDrawHandler categoryDrawHandler
  ) {

    super(type, tier, titleKey, background, icon, uid);
    this.categorySetupHandler = categorySetupHandler;
    this.categoryDrawHandler = categoryDrawHandler;
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

    this.categorySetupHandler.setup(recipe, layout);
  }

  @Override
  public void draw(@Nonnull ArtisanRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {

    this.categoryDrawHandler.draw(recipe, matrixStack, this.getBackground().getHeight());
  }
}
