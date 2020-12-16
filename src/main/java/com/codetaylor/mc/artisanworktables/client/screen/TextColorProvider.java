package com.codetaylor.mc.artisanworktables.client.screen;

import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.awt.*;

public final class TextColorProvider {

  private static final Object2IntMap<EnumType> COLOR_MAP;

  static {

    COLOR_MAP = new Object2IntOpenHashMap<>(EnumType.values().length);

    COLOR_MAP.put(EnumType.TAILOR, new Color(172, 81, 227).getRGB());
    COLOR_MAP.put(EnumType.CARPENTER, new Color(188, 152, 98).getRGB());
    COLOR_MAP.put(EnumType.MASON, new Color(151, 151, 151).getRGB());
    COLOR_MAP.put(EnumType.BLACKSMITH, new Color(162, 162, 162).getRGB());
    COLOR_MAP.put(EnumType.JEWELER, new Color(105, 89, 133).getRGB());
    COLOR_MAP.put(EnumType.BASIC, new Color(188, 152, 98).getRGB());
    COLOR_MAP.put(EnumType.ENGINEER, new Color(202, 103, 27).getRGB());
    COLOR_MAP.put(EnumType.MAGE, new Color(172, 81, 227).getRGB());
    COLOR_MAP.put(EnumType.SCRIBE, new Color(182, 136, 79).getRGB());
    COLOR_MAP.put(EnumType.CHEMIST, new Color(71, 97, 71).getRGB());
    COLOR_MAP.put(EnumType.FARMER, new Color(128, 198, 82).getRGB());
    COLOR_MAP.put(EnumType.CHEF, new Color(255, 255, 255).getRGB());
    COLOR_MAP.put(EnumType.DESIGNER, new Color(255, 255, 255).getRGB());
    COLOR_MAP.put(EnumType.TANNER, new Color(199, 125, 79).getRGB());
    COLOR_MAP.put(EnumType.POTTER, new Color(183, 90, 64).getRGB());
  }

  public static int getColorFor(EnumType type) {

    return COLOR_MAP.getInt(type);
  }

  private TextColorProvider() {
    //
  }
}
