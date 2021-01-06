package com.codetaylor.mc.artisanworktables.common.plugin.crafttweaker;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.artisanworktables.Type")
public enum ZenEnumType {

  @ZenCodeType.Field
  TAILOR(EnumType.TAILOR),

  @ZenCodeType.Field
  CARPENTER(EnumType.CARPENTER),

  @ZenCodeType.Field
  MASON(EnumType.MASON),

  @ZenCodeType.Field
  BLACKSMITH(EnumType.BLACKSMITH),

  @ZenCodeType.Field
  JEWELER(EnumType.JEWELER),

  @ZenCodeType.Field
  BASIC(EnumType.BASIC),

  @ZenCodeType.Field
  ENGINEER(EnumType.ENGINEER),

  @ZenCodeType.Field
  MAGE(EnumType.MAGE),

  @ZenCodeType.Field
  SCRIBE(EnumType.SCRIBE),

  @ZenCodeType.Field
  CHEMIST(EnumType.CHEMIST),

  @ZenCodeType.Field
  FARMER(EnumType.FARMER),

  @ZenCodeType.Field
  CHEF(EnumType.CHEF),

  @ZenCodeType.Field
  DESIGNER(EnumType.DESIGNER),

  @ZenCodeType.Field
  TANNER(EnumType.TANNER),

  @ZenCodeType.Field
  POTTER(EnumType.POTTER);

  private final EnumType type;

  ZenEnumType(EnumType type) {

    this.type = type;
  }

  public EnumType getType() {

    return this.type;
  }
}
