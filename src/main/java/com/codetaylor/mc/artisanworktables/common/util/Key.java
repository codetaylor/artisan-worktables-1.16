package com.codetaylor.mc.artisanworktables.common.util;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.util.ResourceLocation;

public final class Key {

  public static ResourceLocation from(String path) {

    return new ResourceLocation(ArtisanWorktablesMod.MOD_ID, path);
  }

  private Key() {
    //
  }
}
