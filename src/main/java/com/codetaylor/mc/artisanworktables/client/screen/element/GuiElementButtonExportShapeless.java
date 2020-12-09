package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.client.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.client.screen.BaseScreen;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

public class GuiElementButtonExportShapeless
    extends GuiElementTextureButtonBase
    implements IGuiElementTooltipProvider {

  public GuiElementButtonExportShapeless(GuiContainerBase guiBase, int elementX, int elementY) {

    super(
        guiBase,
        new Texture[]{
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 11, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 11, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT)
        },
        elementX,
        elementY,
        11,
        11
    );
  }

  @Override
  public void elementClicked(double mouseX, double mouseY, int mouseButton) {

    super.elementClicked(mouseX, mouseY, mouseButton);

    BaseScreen gui = (BaseScreen) this.guiBase;
    BaseTileEntity tileEntity = gui.getTile();
    ClientPlayerEntity player = Minecraft.getInstance().player;

    try {
      String data = ZSRecipeExport.getExportString((AWContainer) gui.inventorySlots, tileEntity, false);
      StringSelection contents = new StringSelection(data);
      Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
      Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
      systemClipboard.setContents(contents, null);

      if (player != null) {
        player.sendMessage(new TranslationTextComponent("chat.artisanworktables.message.recipe.copy.success"), null);
      }

    } catch (Exception e) {

      if (player != null) {
        player.sendMessage(new TranslationTextComponent("chat.artisanworktables.message.recipe.copy.error"), null);
      }
      ArtisanWorktablesMod.LOGGER.error("", e);
    }
  }

  @Override
  public List<ITextComponent> tooltipTextGet(List<ITextComponent> list) {

    list.add(new TranslationTextComponent("gui.artisanworktables.tooltip.button.export.shapeless"));
    return list;
  }

  @Override
  public boolean elementIsVisible(double mouseX, double mouseY) {

    BaseScreen gui = (BaseScreen) this.guiBase;
    BaseTileEntity tileEntity = gui.getTile();
    return tileEntity.isCreative();
  }
}
