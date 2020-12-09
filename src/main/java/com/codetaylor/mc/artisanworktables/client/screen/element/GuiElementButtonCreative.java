package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.client.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.client.screen.BaseScreen;
import com.codetaylor.mc.artisanworktables.common.network.CSPacketWorktableCreativeToggle;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class GuiElementButtonCreative
    extends GuiElementTextureButtonBase
    implements IGuiElementTooltipProvider {

  private static final int TEXTURE_BASE_UNLOCKED_INDEX = 2;
  private static final int TEXTURE_HOVERED_UNLOCKED_INDEX = 3;

  public GuiElementButtonCreative(GuiContainerBase guiBase, int elementX, int elementY) {

    super(
        guiBase,
        new Texture[]{
            // locked
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 77, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 77, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            // unlocked
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 88, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 88, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT)
        },
        elementX,
        elementY,
        11,
        11
    );
  }

  private boolean isCreative() {

    BaseScreen gui = (BaseScreen) this.guiBase;
    BaseTileEntity tileEntity = gui.getTile();
    return !tileEntity.isCreative();
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    if (this.isCreative()) {
      return super.textureIndexGet(mouseX, mouseY);

    } else {

      if (this.elementIsMouseInside(mouseX, mouseY)) {
        return TEXTURE_HOVERED_UNLOCKED_INDEX;
      }

      return TEXTURE_BASE_UNLOCKED_INDEX;
    }
  }

  @Override
  public void elementClicked(double mouseX, double mouseY, int mouseButton) {

    super.elementClicked(mouseX, mouseY, mouseButton);

    BaseScreen gui = (BaseScreen) this.guiBase;
    BaseTileEntity tileEntity = gui.getTile();
    ArtisanWorktablesMod.getProxy().getPacketService()
        .sendToServer((new CSPacketWorktableCreativeToggle(tileEntity.getPos())));
  }

  @Override
  public List<ITextComponent> tooltipTextGet(List<ITextComponent> list) {

    BaseScreen gui = (BaseScreen) this.guiBase;
    BaseTileEntity tileEntity = gui.getTile();

    if (tileEntity.isCreative()) {
      list.add(new TranslationTextComponent("gui.artisanworktables.tooltip.button.creative.enabled"));

    } else {
      list.add(new TranslationTextComponent("gui.artisanworktables.tooltip.button.creative.disabled"));
    }
    return list;
  }
}
