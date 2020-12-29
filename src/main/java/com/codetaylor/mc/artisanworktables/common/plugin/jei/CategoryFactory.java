package com.codetaylor.mc.artisanworktables.common.plugin.jei;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.util.Key;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryFactory {

  public BaseCategory<?> create(EnumTier tier, EnumType type, Block block, IGuiHelper guiHelper) {

    return new WorktableCategory(
        tier,
        type,
        String.format("block.%s.%s_%s", ArtisanWorktablesMod.MOD_ID, tier.getName(), type.getName()),
        this.createBackground(tier, guiHelper, Key.from(String.format("textures/gui/%s_%s.png", tier.getName(), type.getName()))),
        guiHelper.createDrawableIngredient(new ItemStack(block)),
        Plugin.CATEGORY_KEYS.get(tier).get(type),
        guiHelper.createCraftingGridHelper(1)
    );
  }

  private IDrawableStatic createBackground(EnumTier tier, IGuiHelper guiHelper, ResourceLocation resourceLocation) {

    switch (tier) {
      case WORKTABLE:
        return guiHelper.createDrawable(resourceLocation, 3, 3, 170, 80);
      case WORKSTATION:
        return guiHelper.createDrawable(resourceLocation, 3, 3, 170, 102);
      case WORKSHOP:
        return guiHelper.createDrawable(resourceLocation, 3, 13, 170, 128);
      default:
        throw new IllegalArgumentException("Unknown tier: " + tier.getName());
    }
  }
}
