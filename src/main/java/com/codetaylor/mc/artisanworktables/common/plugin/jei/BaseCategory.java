package com.codetaylor.mc.artisanworktables.common.plugin.jei;

import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class BaseCategory<R>
    implements IRecipeCategory<R> {

  protected final EnumType type;
  protected final EnumTier tier;
  protected final String titleKey;
  protected final IDrawable background;
  protected final IDrawable icon;
  protected final ResourceLocation uid;

  public BaseCategory(
      EnumType type,
      EnumTier tier,
      String titleKey,
      IDrawable background,
      IDrawable icon,
      ResourceLocation uid
  ) {

    this.type = type;
    this.tier = tier;
    this.titleKey = titleKey;
    this.background = background;
    this.icon = icon;
    this.uid = uid;
  }

  @Nonnull
  @Override
  public ResourceLocation getUid() {

    return this.uid;
  }

  @Nonnull
  @Override
  public String getTitle() {

    return I18n.format(this.titleKey);
  }

  @Nonnull
  @Override
  public IDrawable getBackground() {

    return this.background;
  }

  @Nonnull
  @Override
  public IDrawable getIcon() {

    return this.icon;
  }
}
