package com.codetaylor.mc.artisanworktables.client.screen;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.client.screen.element.GuiElementFluidTankSmall;
import com.codetaylor.mc.artisanworktables.client.screen.element.GuiElementMageEffect;
import com.codetaylor.mc.artisanworktables.client.screen.element.GuiElementTabs;
import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
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
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.awt.*;

public abstract class BaseScreen
    extends GuiContainerBase<BaseContainer> {

  protected final int textColor;
  private final BaseTileEntity tile;

  public BaseScreen(BaseContainer container, PlayerInventory playerInventory, ITextComponent title, int width, int height) {

    super(container, playerInventory, title, width, height);

    // This needs to be set here because the playerInventoryTitleY is calculated
    // from the ySize in the call to super ctor, but the correct value of ySize is
    // set after the call to super ctor.
    this.playerInventoryTitleX = 8;
    this.playerInventoryTitleY = this.ySize - 93;

    // background texture
    this.guiContainerElementAdd(new GuiElementTextureRectangle(
        this,
        new Texture(this.getBackgroundTexture(), 0, 0, 256, 256),
        0,
        0,
        this.xSize,
        this.ySize
    ));

    EnumType tableType = container.getTile().getTableType();
    this.textColor = TextColorProvider.getColorFor(tableType);

    this.tile = this.getContainer().getTile();

    this.addFluidTankElement(this.tile, this.textColor);

    this.guiContainerElementAdd(new GuiElementTabs(
        this,
        this.tile,
        176
    ));

    if (tableType == EnumType.MAGE) {
      this.guiContainerElementAdd(new GuiElementMageEffect(
          this,
          container,
          115,
          35
      ));
    }
  }

  protected void addFluidTankElement(BaseTileEntity tile, int overlayColor) {

    this.guiContainerElementAdd(new GuiElementFluidTankSmall(
        this,
        tile.getTank(),
        tile.getPos(),
        overlayColor,
        8,
        17
    ));
  }

  public <T extends TileEntity> T getTile() {

    //noinspection unchecked
    return (T) this.container.getTile();
  }

  protected ResourceLocation getBackgroundTexture() {

    BaseTileEntity tile = this.container.getTile();
    String typeName = tile.getTableType().getName();
    String tierName = tile.getTableTier().getName();
    return new ResourceLocation(ArtisanWorktablesMod.MOD_ID, "textures/gui/" + tierName + "_" + typeName + ".png");
  }

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

    if (this.tile.getTableType() == EnumType.MAGE) {
      GuiHelper.drawStringOutlined(matrixStack, GuiHelper.asGalactic((IFormattableTextComponent) this.title), this.titleX, this.titleY, this.font, this.textColor, false);
      GuiHelper.drawStringOutlined(matrixStack, GuiHelper.asGalactic((IFormattableTextComponent) this.playerInventory.getDisplayName()), this.playerInventoryTitleX, this.playerInventoryTitleY, this.font, this.textColor, false);

    } else if (this.tile.getTableType() == EnumType.DESIGNER) {
      this.getFontRenderer().func_243248_b(matrixStack, this.title, this.titleX, this.titleY, Color.WHITE.getRGB());
      this.getFontRenderer().func_243248_b(matrixStack, this.playerInventory.getDisplayName(), this.playerInventoryTitleX, this.playerInventoryTitleY, Color.WHITE.getRGB());

    } else {
      GuiHelper.drawStringOutlined(matrixStack, this.title, this.titleX, this.titleY, this.font, this.textColor, false);
      GuiHelper.drawStringOutlined(matrixStack, this.playerInventory.getDisplayName(), this.playerInventoryTitleX, this.playerInventoryTitleY, this.font, this.textColor, false);
    }
  }

  @Override
  public void drawString(MatrixStack matrixStack, String translateKey, int x, int y) {

    FontRenderer fontRenderer = this.getFontRenderer();

    if (this.tile.getTableType() == EnumType.MAGE) {
      GuiHelper.drawStringOutlined(matrixStack, GuiHelper.asGalactic(new TranslationTextComponent(translateKey)), x, y, fontRenderer, this.textColor);

    } else if (this.tile.getTableType() == EnumType.DESIGNER) {
      String displayText = I18n.format(translateKey);
      fontRenderer.drawString(matrixStack, displayText, x - 1, y, Color.WHITE.getRGB());

    } else {
      GuiHelper.drawStringOutlined(matrixStack, new TranslationTextComponent(translateKey), x, y, fontRenderer, this.textColor);
    }
  }
}