package com.codetaylor.mc.artisanworktables.client.screen;

import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class WorkshopScreen
    extends BaseScreen {

  private static final int WIDTH = 176;
  private static final int HEIGHT = 225;

  public WorkshopScreen(BaseContainer container, PlayerInventory playerInventory, ITextComponent title) {

    super(container, playerInventory, title, WIDTH, HEIGHT);
  }
}
