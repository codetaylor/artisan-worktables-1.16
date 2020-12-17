package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.api.IToolHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class ArtisanToolHandlers {

  private static final List<IToolHandler> HANDLERS = new ArrayList<>();

  public static void register(IToolHandler toolHandler) {

    HANDLERS.add(toolHandler);
  }

  public static IToolHandler get(ItemStack itemStack) {

    for (IToolHandler handler : HANDLERS) {

      if (handler.handles(itemStack)) {
        return handler;
      }
    }

    return DefaultToolHandler.INSTANCE;
  }

  private ArtisanToolHandlers() {
    //
  }
}
