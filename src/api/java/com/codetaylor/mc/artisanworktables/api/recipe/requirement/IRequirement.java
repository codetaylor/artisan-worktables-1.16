package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IRequirement<C extends IRequirementContext> {

  /**
   * @return the requirement's resource location
   */
  ResourceLocation getResourceLocation();

  /**
   * @param context the requirement context
   * @return true if the requirement passes with the given context
   */
  boolean match(C context);

  /**
   * @return true if JEI should hide any recipe with this requirement when it loads
   */
  @OnlyIn(Dist.CLIENT)
  default boolean shouldJEIHideOnLoad() {

    return false;
  }

  @OnlyIn(Dist.CLIENT)
  default boolean shouldJEIHideOnUpdate() {

    return false;
  }
}
