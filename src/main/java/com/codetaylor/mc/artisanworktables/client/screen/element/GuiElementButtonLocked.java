package com.codetaylor.mc.artisanworktables.client.screen.element;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.client.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.client.screen.BaseScreen;
import com.codetaylor.mc.artisanworktables.common.network.CSPacketWorktableLockedModeToggle;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class GuiElementButtonLocked
    extends GuiElementTextureButtonBase
    implements IGuiElementTooltipProvider {

  private static final int TEXTURE_BASE_UNLOCKED_INDEX = 2;
  private static final int TEXTURE_HOVERED_UNLOCKED_INDEX = 3;

  public GuiElementButtonLocked(GuiContainerBase guiBase, int elementX, int elementY) {

    super(
        guiBase,
        new Texture[]{
            // locked
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 33, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 33, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            // unlocked
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 44, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 44, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT)
        },
        elementX,
        elementY,
        11,
        11
    );
  }

  public boolean isLocked() {

    BaseScreen gui = (BaseScreen) this.guiBase;
    BaseTileEntity tileEntity = gui.getTile();
    return tileEntity.isLocked();
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    if (this.isLocked()) {
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
        .sendToServer(new CSPacketWorktableLockedModeToggle(tileEntity.getPos()));
  }

  @Override
  public List<ITextComponent> tooltipTextGet(List<ITextComponent> list) {

    BaseScreen gui = (BaseScreen) this.guiBase;
    BaseTileEntity tileEntity = gui.getTile();

    if (tileEntity.isLocked()) {
      list.add(new TranslationTextComponent("gui.artisanworktables.tooltip.button.locked.enabled"));

    } else {
      list.add(new TranslationTextComponent("gui.artisanworktables.tooltip.button.locked.disabled"));
    }
    return list;
  }

  @Override
  public boolean elementIsVisible(double mouseX, double mouseY) {

    BaseScreen gui = (BaseScreen) this.guiBase;
    BaseTileEntity tileEntity = gui.getTile();
    return tileEntity.isCreative();
  }
}
