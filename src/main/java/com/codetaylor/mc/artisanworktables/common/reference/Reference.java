package com.codetaylor.mc.artisanworktables.common.reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Reference {

  public static final String MOD_ID = "artisanworktables";

  public static final int MAX_RECIPE_WIDTH = 5;
  public static final int MAX_RECIPE_HEIGHT = 5;

  // --------------------------------------------------------------------------
  // Worktable Names
  // --------------------------------------------------------------------------

  private static final List<String> WORKTABLE_NAME_LIST;

  static {
    List<String> names = Arrays.asList(EnumType.NAMES);
    List<String> nameList = new ArrayList<>(names);
    WORKTABLE_NAME_LIST = Collections.unmodifiableList(nameList);
  }

  /**
   * @return unmodifiable list of worktable names
   */
  public static List<String> getWorktableNames() {

    return WORKTABLE_NAME_LIST;
  }

  /**
   * @param name the worktable name
   * @return true if the worktable name is valid
   */
  public static boolean isWorktableNameValid(String name) {

    return WORKTABLE_NAME_LIST.contains(name.toLowerCase());
  }

  private Reference() {
    //
  }
}
