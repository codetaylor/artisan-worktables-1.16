package com.codetaylor.mc.artisanworktables.common.plugin.jei;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShapeless;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class CategoryDrawHandler {

  private final EnumTier tier;

  public CategoryDrawHandler(EnumTier tier) {

    this.tier = tier;
  }

  public void draw(@Nonnull ArtisanRecipe recipe, MatrixStack matrixStack, int backgroundHeight) {

    Minecraft minecraft = Minecraft.getInstance();
    FontRenderer fontRenderer = minecraft.fontRenderer;

    matrixStack.push();
    matrixStack.translate(0, 0, 200);

    this.drawToolDamageStrings(recipe, matrixStack, fontRenderer);
    this.drawExperienceString(recipe, matrixStack, backgroundHeight, fontRenderer);

    matrixStack.push();
    matrixStack.scale(0.5f, 0.5f, 1);

    this.drawExtraOutputChanceStrings(recipe, matrixStack, fontRenderer);
    this.drawShapelessIndicator(recipe, matrixStack, minecraft);

    matrixStack.pop();
    matrixStack.pop();
  }

  private void drawExperienceString(@Nonnull ArtisanRecipe recipe, MatrixStack matrixStack, int backgroundHeight, FontRenderer fontRenderer) {

    String experienceString = null;

    if (recipe.getExperienceRequired() > 0) {

      if (recipe.consumeExperience()) {
        experienceString = I18n.format("jei.artisanworktables.xp.cost", recipe.getExperienceRequired());

      } else {
        experienceString = I18n.format(
            "jei.artisanworktables.xp.required",
            recipe.getExperienceRequired()
        );
      }

    } else if (recipe.getLevelRequired() > 0) {

      if (recipe.consumeExperience()) {
        experienceString = I18n.format("jei.artisanworktables.level.cost", recipe.getLevelRequired());

      } else {
        experienceString = I18n.format("jei.artisanworktables.level.required", recipe.getLevelRequired());
      }
    }

    if (experienceString != null) {
      this.drawExperienceString(fontRenderer, matrixStack, backgroundHeight, experienceString);
    }
  }

  private void drawToolDamageStrings(@Nonnull ArtisanRecipe recipe, MatrixStack matrixStack, FontRenderer fontRenderer) {

    switch (this.tier) {
      case WORKTABLE:
        this.drawToolDamageString(recipe, fontRenderer, matrixStack, 83, 52);
        break;
      case WORKSTATION:
        this.drawToolDamageString(recipe, fontRenderer, matrixStack, 83, 33);
        break;
      case WORKSHOP:
        this.drawToolDamageString(recipe, fontRenderer, matrixStack, 119, 39);
        break;
    }
  }

  private void drawExtraOutputChanceStrings(@Nonnull ArtisanRecipe recipe, MatrixStack matrixStack, FontRenderer fontRenderer) {

    int size = Math.min(recipe.getExtraOutputs().size(), 3);

    if (this.tier == EnumTier.WORKSHOP) {

      int xPos = 256;
      int yPos = 12;

      for (int i = 0; i < size; i++) {
        float chance = recipe.getExtraOutputs().get(i).getChance();
        this.drawExtraOutputChanceString(fontRenderer, matrixStack, chance, xPos + 36 * i, yPos);
      }

    } else {

      int xPos = 331;
      int yPos = 32;

      for (int i = 0; i < size; i++) {
        float chance = recipe.getExtraOutputs().get(i).getChance();
        this.drawExtraOutputChanceString(fontRenderer, matrixStack, chance, xPos, yPos + 36 * i);
      }
    }
  }

  private void drawShapelessIndicator(@Nonnull ArtisanRecipe recipe, MatrixStack matrixStack, Minecraft minecraft) {

    if (recipe instanceof ArtisanRecipeShapeless) {

      RenderSystem.enableBlend();

      if (this.tier == EnumTier.WORKSHOP) {
        GuiHelper.drawTexturedRect(minecraft, Plugin.RECIPE_BACKGROUND, matrixStack, 288, 58, 18, 17, 0, 0, 0, 1, 1);

      } else {
        GuiHelper.drawTexturedRect(minecraft, Plugin.RECIPE_BACKGROUND, matrixStack, 234, 8, 18, 17, 0, 0, 0, 1, 1);
      }
    }
  }

  private void drawExtraOutputChanceString(
      FontRenderer fontRenderer,
      MatrixStack matrixStack,
      float secondaryOutputChance,
      int positionX,
      int positionY
  ) {

    String label = (int) (secondaryOutputChance * 100) + "%";
    fontRenderer.func_243248_b(matrixStack, new TranslationTextComponent(label), positionX - fontRenderer.getStringWidth(label) * 0.5f, positionY, 0xFFFFFFFF);
  }

  private void drawToolDamageString(ArtisanRecipe recipe, FontRenderer fontRenderer, MatrixStack matrixStack, int offsetX, int offsetY) {

    NonNullList<ToolEntry> tools = recipe.getTools();
    int toolCount = tools.size();

    for (int i = 0; i < toolCount; i++) {
      String label = "-" + tools.get(i).getDamage();
      fontRenderer.drawString(
          matrixStack,
          label,
          offsetX - fontRenderer.getStringWidth(label) * 0.5f,
          offsetY + (22 * i),
          0xFFFFFFFF
      );
    }
  }

  private void drawExperienceString(FontRenderer fontRenderer, MatrixStack matrixStack, int backgroundHeight, String experienceString) {

    fontRenderer.drawString(
        matrixStack,
        experienceString,
        5,
        backgroundHeight - 10,
        0xFF80FF20
    );
  }
}
