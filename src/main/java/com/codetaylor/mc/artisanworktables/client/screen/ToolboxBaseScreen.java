package com.codetaylor.mc.artisanworktables.client.screen;

import com.codetaylor.mc.artisanworktables.common.container.ToolboxBaseContainer;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipExtendedProvider;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import com.codetaylor.mc.athenaeum.util.KeyHelper;
import com.codetaylor.mc.athenaeum.util.TooltipHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.awt.*;

public abstract class ToolboxBaseScreen<C extends ToolboxBaseContainer>
    extends GuiContainerBase<C> {

  private static final int TEXT_SHADOW_COLOR = new Color(103, 69, 29).getRGB();

  public ToolboxBaseScreen(C container, PlayerInventory playerInventory, ITextComponent title, int width, int height) {

    super(container, playerInventory, title, width, height);

    this.guiContainerElementAdd(new GuiElementTextureRectangle(
        this,
        this.getTexture(),
        0,
        0,
        176,
        166
    ));
  }

  protected abstract Texture getTexture();

  @Override
  public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
  }
  
  @Override
  protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {

    this.tooltipProviderList.clear();

    for (GuiElementBase element : this.guiElementList) {

      if (element.elementIsVisible(mouseX, mouseY)) {
        element.drawForegroundLayer(matrixStack, mouseX, mouseY);

        if (element instanceof IGuiElementTooltipProvider
            && element.elementIsMouseInside(mouseX, mouseY)) {

          this.tooltipProviderList.add((IGuiElementTooltipProvider) element);
        }
      }
    }

    for (IGuiElementTooltipProvider element : this.tooltipProviderList) {
      this.tooltipTextList.clear();

      if (element.elementIsVisible(mouseX, mouseY)
          && element.elementIsMouseInside(mouseX, mouseY)) {
        element.tooltipTextGet(this.tooltipTextList);

        if (element instanceof IGuiElementTooltipExtendedProvider) {

          InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT);

          if (KeyHelper.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)
              || KeyHelper.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
            ((IGuiElementTooltipExtendedProvider) element).tooltipTextExtendedGet(this.tooltipTextList);

          } else {
            this.tooltipTextList.add(TooltipHelper.getTooltipHoldShiftTextComponent());
          }
        }

        GuiUtils.drawHoveringText(
            matrixStack,
            this.tooltipTextList,
            mouseX - this.guiContainerOffsetXGet(),
            mouseY - this.guiContainerOffsetYGet(),
            this.width,
            this.height,
            -1,
            this.font
        );
      }
    }

    GuiHelper.drawStringOutlined(matrixStack, this.title, this.titleX, this.titleY, this.font, TEXT_SHADOW_COLOR, false);
    GuiHelper.drawStringOutlined(matrixStack, this.playerInventory.getDisplayName(), this.playerInventoryTitleX, this.playerInventoryTitleY, this.font, TEXT_SHADOW_COLOR, false);
  }
}
