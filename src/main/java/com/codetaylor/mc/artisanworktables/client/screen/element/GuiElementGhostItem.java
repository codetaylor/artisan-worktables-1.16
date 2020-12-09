package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementBase;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class GuiElementGhostItem
    extends GuiElementBase {

  private final BaseTileEntity tile;
  private final IItemHandler handler;
  private final IItemHandler handlerGhost;
  private final int index;

  public GuiElementGhostItem(
      GuiContainerBase guiBase,
      BaseTileEntity tile,
      IItemHandler handler,
      IItemHandler handlerGhost,
      int index,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight
  ) {

    super(guiBase, elementX, elementY, elementWidth, elementHeight);
    this.tile = tile;
    this.handler = handler;
    this.handlerGhost = handlerGhost;
    this.index = index;
  }

  @Override
  public void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {

    ItemStack handlerStack = this.handler.getStackInSlot(this.index);
    ItemStack ghostStack = this.handlerGhost.getStackInSlot(this.index);

    if (handlerStack.isEmpty()
        && !ghostStack.isEmpty()
        && this.tile.isLocked()) {
      ItemRenderer itemRender = this.guiBase.getItemRender();
      RenderHelper.enableStandardItemLighting();
      itemRender.renderItemIntoGUI(ghostStack, this.elementXModifiedGet(), this.elementYModifiedGet());
      itemRender.renderItemOverlayIntoGUI(this.guiBase.getFontRenderer(), ghostStack, this.elementXModifiedGet(), this.elementYModifiedGet(), "-");
      RenderHelper.disableStandardItemLighting();
    }
  }

  @Override
  public void drawForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
    //
  }
}
