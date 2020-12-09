package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.client.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.client.screen.BaseScreen;
import com.codetaylor.mc.artisanworktables.common.network.CSPacketWorktableClear;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class GuiElementButtonClearAll
    extends GuiElementTextureButtonBase
    implements IGuiElementTooltipProvider {

  public GuiElementButtonClearAll(GuiContainerBase guiBase, int elementX, int elementY) {

    super(
        guiBase,
        new Texture[]{
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 22, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 22, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT)
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
    tileEntity.tagMap.clear();
    ArtisanWorktablesMod.getProxy().getPacketService()
        .sendToServer(new CSPacketWorktableClear(tileEntity.getPos(), CSPacketWorktableClear.CLEAR_ALL));
  }

  @Override
  public List<ITextComponent> tooltipTextGet(List<ITextComponent> list) {

    list.add(new TranslationTextComponent("gui.artisanworktables.tooltip.button.clear.table"));
    return list;
  }

  @Override
  public boolean elementIsVisible(double mouseX, double mouseY) {

    BaseScreen gui = (BaseScreen) this.guiBase;
    BaseTileEntity tileEntity = gui.getTile();
    return tileEntity.isCreative();
  }
}
