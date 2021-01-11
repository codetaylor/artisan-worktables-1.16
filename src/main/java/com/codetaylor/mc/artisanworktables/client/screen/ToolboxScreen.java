package com.codetaylor.mc.artisanworktables.client.screen;

import com.codetaylor.mc.artisanworktables.client.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.common.container.ToolboxContainer;
import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ToolboxScreen
    extends ToolboxBaseScreen<ToolboxContainer> {

  private static final int WIDTH = 176;
  private static final int HEIGHT = 166;

  public ToolboxScreen(ToolboxContainer container, PlayerInventory playerInventory, ITextComponent title) {

    super(container, playerInventory, title, WIDTH, HEIGHT);
  }

  @Override
  protected Texture getTexture() {

    return ReferenceTexture.TEXTURE_TOOLBOX;
  }
}
