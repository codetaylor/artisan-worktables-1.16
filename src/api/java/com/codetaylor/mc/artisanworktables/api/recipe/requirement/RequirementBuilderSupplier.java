package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class RequirementBuilderSupplier<C extends IRequirementContext, R extends IRequirement<C>>
    extends ForgeRegistryEntry<RequirementBuilderSupplier<C, R>>
    implements Supplier<IRequirementBuilder<C, R>> {

  private final Supplier<IRequirementBuilder<C, R>> supplier;

  public RequirementBuilderSupplier(String modId, String name, Supplier<IRequirementBuilder<C, R>> supplier) {

    this.supplier = supplier;
    this.setRegistryName(modId, name);
  }

  @Override
  public IRequirementBuilder<C, R> get() {

    return this.supplier.get();
  }
}
