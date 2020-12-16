package com.codetaylor.mc.artisanworktables.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class ArtisanToolHandlers {

  private static final List<IToolHandler> HANDLERS = new ArrayList<>();

  public static void register(IToolHandler toolHandler) {

    HANDLERS.add(toolHandler);
  }

  @Nullable
  public static IToolHandler get(ItemStack itemStack) {

    for (IToolHandler handler : HANDLERS) {

      if (handler.matches(itemStack)) {
        return handler;
      }
    }

    return null;
  }

  private ArtisanToolHandlers() {
    //
  }
}
