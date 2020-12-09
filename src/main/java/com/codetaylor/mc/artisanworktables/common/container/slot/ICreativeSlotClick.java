package com.codetaylor.mc.artisanworktables.common.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;

public interface ICreativeSlotClick {

  ItemStack creativeSlotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player);

  default boolean allowTag() {

    return false;
  }
}
