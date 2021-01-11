package com.codetaylor.mc.artisanworktables.client.screen;

import com.codetaylor.mc.artisanworktables.client.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.common.container.ToolboxMechanicalContainer;
import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ToolboxMechanicalScreen
    extends ToolboxBaseScreen<ToolboxMechanicalContainer> {

  private static final int WIDTH = 176;
  private static final int HEIGHT = 166;

  public ToolboxMechanicalScreen(ToolboxMechanicalContainer container, PlayerInventory playerInventory, ITextComponent title) {

    super(container, playerInventory, title, WIDTH, HEIGHT);
  }

  @Override
  protected Texture getTexture() {

    return ReferenceTexture.TEXTURE_TOOLBOX_MECHANICAL;
  }
}
