package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.common.tile.ToolboxTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.function.IntSupplier;

public class GuiElementToolboxSide
    extends GuiElementTextureRectangle {

  private final ToolboxTileEntity toolbox;
  private final IntSupplier elementY;

  public GuiElementToolboxSide(
      GuiContainerBase guiBase,
      ToolboxTileEntity toolbox,
      Texture texture,
      int elementX,
      IntSupplier elementY
  ) {

    super(guiBase, texture, elementX, 0, 68, 176);
    this.toolbox = toolbox;
    this.elementY = elementY;
  }

  @Override
  public void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {

    if (this.toolbox != null && !this.toolbox.isRemoved()) {
      super.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    }
  }

  @Override
  protected int elementYModifiedGet() {

    return super.elementYModifiedGet() + this.elementY.getAsInt();
  }
}
