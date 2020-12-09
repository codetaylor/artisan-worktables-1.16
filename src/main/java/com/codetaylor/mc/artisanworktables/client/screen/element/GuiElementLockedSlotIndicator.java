package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.client.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

public class GuiElementLockedSlotIndicator
    extends GuiElementTextureRectangle {

  private final BaseTileEntity tile;

  public GuiElementLockedSlotIndicator(
      GuiContainerBase guiBase,
      BaseTileEntity tile,
      int elementX,
      int elementY
  ) {

    super(
        guiBase,
        new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 154, 0, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
        elementX,
        elementY,
        4,
        5
    );
    this.tile = tile;
  }

  @Override
  public void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    //
  }

  @Override
  public void drawForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {

    super.drawForegroundLayer(matrixStack, mouseX, mouseY);

    this.textureBind(this.textures[0]);

    RenderSystem.color3f(1, 1, 1);

    GuiHelper.drawModalRectWithCustomSizedTexture(
        this.elementX,
        this.elementY,
        300,
        this.textures[0].getPositionX(),
        this.textures[0].getPositionY(),
        4,
        5,
        this.textures[0].getWidth(),
        this.textures[0].getHeight()
    );
  }

  @Override
  public boolean elementIsVisible(double mouseX, double mouseY) {

    return this.tile.isLocked();
  }
}
