package com.codetaylor.mc.artisanworktables.common.reference;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.stream.Stream;

public enum EnumType {

  TAILOR("tailor"),
  CARPENTER("carpenter"),
  MASON("mason"),
  BLACKSMITH("blacksmith"),
  JEWELER("jeweler"),
  BASIC("basic"),
  ENGINEER("engineer"),
  MAGE("mage"),
  SCRIBE("scribe"),
  CHEMIST("chemist"),
  FARMER("farmer"),
  CHEF("chef"),
  DESIGNER("designer"),
  TANNER("tanner"),
  POTTER("potter");

  public static final String[] NAMES = Stream.of(EnumType.values())
      .sorted(Comparator.comparing(EnumType::getName))
      .map(EnumType::getName)
      .toArray(String[]::new);

  private final String name;

  EnumType(
      String name
  ) {

    this.name = name;
  }

  @Nonnull
  public String getName() {

    return this.name;
  }

  public static EnumType fromName(String name) {

    EnumType[] values = EnumType.values();
    name = name.toLowerCase();

    for (EnumType value : values) {

      if (value.name.equals(name)) {
        return value;
      }
    }

    throw new IllegalArgumentException("Unknown name: " + name);
  }
}