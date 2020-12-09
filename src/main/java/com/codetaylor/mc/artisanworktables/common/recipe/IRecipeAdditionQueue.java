package com.codetaylor.mc.artisanworktables.common.recipe;

public interface IRecipeAdditionQueue {

  void offer(RecipeBuilderInternal recipeBuilder);

  void offerWithCopy(RecipeBuilderInternal recipeBuilder);

}
