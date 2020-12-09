package com.codetaylor.mc.artisanworktables.common.recipe.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IRecipeBuilderCopyStrategy;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.common.recipe.RecipeBuilderInternal;

import java.util.List;

public interface IRecipeBuilderCopyStrategyInternal
    extends IRecipeBuilderCopyStrategy {

  EnumCopyPhase getCopyPhase();

  boolean isExcludeInput();

  boolean isExcludeOutput();

  boolean isValid();

  void apply(RecipeBuilderInternal recipeBuilder, List<RecipeBuilderInternal> resultList) throws RecipeBuilderException;

}
