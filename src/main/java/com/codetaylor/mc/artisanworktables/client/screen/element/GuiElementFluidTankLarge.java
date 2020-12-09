package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.client.ReferenceTexture;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementClickable;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class GuiElementFluidTankLarge
    extends GuiElementFluidTankBase
    implements IGuiElementClickable,
    IGuiElementTooltipProvider {

  private static final int ELEMENT_WIDTH = 6;
  private static final int ELEMENT_HEIGHT = 88;
  private static final int ELEMENT_OVERLAY_WIDTH = 6;
  private static final int ELEMENT_OVERLAY_HEIGHT = 52;

  public GuiElementFluidTankLarge(
      GuiContainerBase guiBase,
      FluidTank fluidTank,
      BlockPos blockPos,
      int overlayColor,
      int elementX,
      int elementY
  ) {

    super(guiBase, fluidTank, elementX, elementY, ELEMENT_WIDTH, ELEMENT_HEIGHT, overlayColor, blockPos);
  }

  @Override
  public void drawForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {

    super.drawForegroundLayer(matrixStack, mouseX, mouseY);

    this.textureBind(ReferenceTexture.TEXTURE_FLUID_OVERLAY);

    RenderHelper.disableStandardItemLighting();
    RenderSystem.enableBlend();
    RenderSystem.color4f(
        ((this.overlayColor >> 16) & 0xFF) / 255f,
        ((this.overlayColor >> 8) & 0xFF) / 255f,
        (this.overlayColor & 0xFF) / 255f,
        1f
    );

    GuiHelper.drawModalRectWithCustomSizedTexture(
        this.elementX,
        this.elementY,
        0,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionX(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionY(),
        ELEMENT_OVERLAY_WIDTH,
        ELEMENT_OVERLAY_HEIGHT,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getWidth(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getHeight()
    );

    GuiHelper.drawModalRectWithCustomSizedTexture(
        this.elementX,
        this.elementY + ELEMENT_OVERLAY_HEIGHT - 16,
        0,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionX(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionY(),
        ELEMENT_OVERLAY_WIDTH,
        ELEMENT_OVERLAY_HEIGHT,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getWidth(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getHeight()
    );
  }

}
