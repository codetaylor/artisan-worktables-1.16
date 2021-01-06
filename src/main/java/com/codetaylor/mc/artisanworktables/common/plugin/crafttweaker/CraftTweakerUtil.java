package com.codetaylor.mc.artisanworktables.common.plugin.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;

public final class CraftTweakerUtil {

  public static String validateRecipeName(String name) {

    name = CraftTweakerUtil.fixRecipeName(name);
    if(!name.chars().allMatch((ch) -> ch == 95 || ch == 45 || ch >= 97 && ch <= 122 || ch >= 48 && ch <= 57 || ch == 47 || ch == 46)) {
      throw new IllegalArgumentException("Given name does not fit the \"[a-z0-9/._-]\" regex! Name: \"" + name + "\"");
    }
    return name;
  }

  public static String fixRecipeName(String name) {

    String fixed = name;
    if (fixed.indexOf(':') >= 0) {
      String temp = fixed.replaceAll(":", ".");
      CraftTweakerAPI.logWarning("Invalid recipe name \"%s\", recipe names cannot have a \":\"! New recipe name: \"%s\"", fixed, temp);
      fixed = temp;
    }
    if (fixed.indexOf(' ') >= 0) {
      String temp = fixed.replaceAll(" ", ".");
      CraftTweakerAPI.logWarning("Invalid recipe name \"%s\", recipe names cannot have a \" \"! New recipe name: \"%s\"", fixed, temp);
      fixed = temp;
    }
    if (!fixed.toLowerCase().equals(fixed)) {
      String temp = fixed.toLowerCase();
      CraftTweakerAPI.logWarning("Invalid recipe name \"%s\", recipe names have to be lowercase! New recipe name: \"%s\"", fixed, temp);
      fixed = temp;
    }
    return fixed;
  }

  private CraftTweakerUtil() {
    //
  }

}
