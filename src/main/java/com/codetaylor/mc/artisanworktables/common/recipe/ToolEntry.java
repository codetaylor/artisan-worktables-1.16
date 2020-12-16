package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.api.IToolHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ToolEntry {

  private final Ingredient tool;
  private final ItemStack[] toolStacks;
  private final ItemStack[] toolItemStacks;
  private final int damage;

  public ToolEntry(Ingredient tool, int damage) {

    this.tool = tool;
    this.toolStacks = tool.getMatchingStacks();
    this.damage = damage;
    this.toolItemStacks = new ItemStack[this.toolStacks.length];

    System.arraycopy(this.toolStacks, 0, this.toolItemStacks, 0, this.toolStacks.length);
  }

  public ItemStack[] getToolStacks() {

    return this.toolStacks;
  }

  public Ingredient getTool() {

    return this.tool;
  }

  public int getDamage() {

    return this.damage;
  }

  public boolean matches(IToolHandler handler, ItemStack tool) {

    for (ItemStack toolItemStack : this.toolItemStacks) {

      if (handler.matches(tool, toolItemStack)) {
        return true;
      }
    }

    return false;
  }
}
