package com.codetaylor.mc.artisanworktables.client.screen;

import com.codetaylor.mc.artisanworktables.common.container.ToolboxBaseContainer;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public abstract class ToolboxBaseScreen<C extends ToolboxBaseContainer>
    extends GuiContainerBase<C> {

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
}
