package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.common.tile.ITileEntityDesigner;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.mojang.blaze3d.matrix.MatrixStack;

public class GuiElementDesignersSide
    extends GuiElementTextureRectangle {

  private final ITileEntityDesigner tile;

  public GuiElementDesignersSide(
      GuiContainerBase guiBase,
      ITileEntityDesigner tile,
      Texture texture,
      int elementX,
      int elementY
  ) {

    super(guiBase, texture, elementX, elementY, 68, 176);
    this.tile = tile;
  }

  @Override
  public void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {

    if (this.tile != null && !this.tile.getTileEntity().isRemoved()) {
      super.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    }
  }
}
