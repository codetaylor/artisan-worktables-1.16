package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.common.util.ToolValidationHelper;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TagsUpdatedEventHandler {

  @SubscribeEvent
  public void on(TagsUpdatedEvent event) {

    ToolValidationHelper.clear();
  }
}