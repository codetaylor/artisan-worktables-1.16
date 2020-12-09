package com.codetaylor.mc.artisanworktables.common.container.slot;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public final class TagSlotClickDelegate {

  public static boolean slotClick(List<Slot> inventorySlots, int slotId, ItemStack stack, Int2ObjectMap<String> tagMap, ClickType clickType, boolean isRemote, boolean tagLinked) {

    if (stack.isEmpty()) {
      return false;
    }

    if (clickType != ClickType.QUICK_MOVE) {
      return false;
    }

    if (!isRemote) {
      return true;
    }

    // System.out.println("Slot: " + slotId);

    int[] oreIDs = OreDictionary.getOreIDs(stack);

    if (oreIDs.length > 0) {
      String lookup = tagMap.get(slotId);

      if (lookup == null) {
        String oreName = OreDictionary.getOreName(oreIDs[0]);
        tagMap.put(slotId, oreName);

        if (tagLinked) {
          TagSlotClickDelegate.applyToAll(inventorySlots, stack, oreName, tagMap);
        }

      } else {

        for (int i = 0; i < oreIDs.length; i++) {
          String oreName = OreDictionary.getOreName(oreIDs[i]);

          if (lookup.equals(oreName)) {

            if (i == oreIDs.length - 1) {
              tagMap.remove(slotId);
              if (tagLinked) {
                TagSlotClickDelegate.applyToAll(inventorySlots, stack, null, tagMap);
              }

            } else {
              String nextName = OreDictionary.getOreName(oreIDs[i + 1]);
              tagMap.put(slotId, nextName);
              if (tagLinked) {
                TagSlotClickDelegate.applyToAll(inventorySlots, stack, nextName, tagMap);
              }
            }
            break;
          }
        }
      }
    }

    return true;
  }

  private static void applyToAll(List<Slot> inventorySlots, ItemStack stack, @Nullable String oreName, Int2ObjectMap<String> tagMap) {

    for (Slot inventorySlot : inventorySlots) {
      int slotNumber = inventorySlot.slotNumber;

      if (inventorySlot instanceof ICreativeSlotClick
          && ((ICreativeSlotClick) inventorySlot).allowTag()) {

        ItemStack otherStack = inventorySlot.getStack();

        if (otherStack.isEmpty()) {
          // System.out.println("Skipping empty stack");
          continue;
        }

        if (stack.getItem() != otherStack.getItem()) {
          // System.out.println("Item mismatch: " + stack + " != " + otherStack);
          continue;
        }

        if (oreName != null
            && (!OreDictHelper.contains(oreName, stack)
            || !OreDictHelper.contains(oreName, otherStack))) {
          // System.out.println("Oredict mismatch");
          continue;
        }

        if (oreName == null) {
          tagMap.remove(slotNumber);
          // System.out.println("Removed slot: " + slotNumber);

        } else {
          tagMap.put(slotNumber, oreName);
          // System.out.println("Added: " + slotNumber + "=" + oreName);
        }
      }
    }
  }

  private TagSlotClickDelegate() {
    //
  }
}
