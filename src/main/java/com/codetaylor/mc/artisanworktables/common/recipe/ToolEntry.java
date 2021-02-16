package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.api.IToolHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ToolEntry {

  private static final ThreadLocal<List<ItemStack>> TOOL_CACHE = ThreadLocal.withInitial(ArrayList::new);

  private static ItemStack cache(ItemStack itemStack) {

    if (itemStack.hasTag()) {
      return itemStack;
    }

    List<ItemStack> list = TOOL_CACHE.get();

    for (ItemStack stack : list) {

      if (stack.getItem() == itemStack.getItem()) {
        return stack;
      }
    }

    list.add(itemStack);
    return itemStack;
  }

  private final Ingredient tool;
  private final ItemStack[] toolItemStacks;
  private final int damage;

  public ToolEntry(Ingredient tool, int damage) {

    this.tool = tool;
    this.damage = damage;

    ItemStack[] matchingStacks = tool.getMatchingStacks();
    this.toolItemStacks = new ItemStack[matchingStacks.length];

    for (int i = 0; i < matchingStacks.length; i++) {
      this.toolItemStacks[i] = ToolEntry.cache(matchingStacks[i]);
    }
  }

  public ItemStack[] getToolStacks() {

    return this.toolItemStacks;
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
