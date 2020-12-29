package com.codetaylor.mc.artisanworktables.common.reference;

import java.util.Comparator;
import java.util.stream.Stream;

public enum EnumTier {

  WORKTABLE(0, "worktable"),
  WORKSTATION(1, "workstation"),
  WORKSHOP(2, "workshop");

  public static final String[] NAMES = Stream.of(EnumType.values())
      .sorted(Comparator.comparing(EnumType::getName))
      .map(EnumType::getName)
      .toArray(String[]::new);

  private int id;
  private String name;

  EnumTier(int id, String name) {

    this.id = id;
    this.name = name;
  }

  public int getId() {

    return this.id;
  }

  public String getName() {

    return this.name;
  }

  public static EnumTier fromId(int id) {

    for (EnumTier tier : EnumTier.values()) {

      if (tier.getId() == id) {
        return tier;
      }
    }

    throw new IllegalArgumentException("Invalid id: " + id);
  }

  public static EnumTier fromName(String name) {

    EnumTier[] values = EnumTier.values();
    name = name.toLowerCase();

    for (EnumTier value : values) {

      if (value.name.equals(name)) {
        return value;
      }
    }

    throw new IllegalArgumentException("Unknown name: " + name);
  }
}
