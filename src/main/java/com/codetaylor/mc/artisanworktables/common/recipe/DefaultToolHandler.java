package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.api.IToolHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class DefaultToolHandler
    implements IToolHandler {

  public static final DefaultToolHandler INSTANCE = new DefaultToolHandler();

  @Override
  public boolean handles(ItemStack itemStack) {

    return itemStack.isDamageable();
  }

  @Override
  public boolean matches(ItemStack tool, ItemStack toolToMatch) {

    return (tool.getItem() == toolToMatch.getItem());
  }

  @Override
  public boolean canAcceptAllDamage(ItemStack itemStack, int damage) {

    return (itemStack.getDamage() + damage <= itemStack.getMaxDamage());
  }

  @Override
  public boolean applyDamage(World world, ItemStack itemStack, int damage, @Nullable PlayerEntity player, boolean simulate) {

    if (simulate) {
      return (itemStack.getDamage() + damage > itemStack.getMaxDamage());

    } else {
      boolean broken = itemStack.attemptDamageItem(damage, new Random(), null)
          || itemStack.getDamage() == itemStack.getMaxDamage();

      if (broken) {
        itemStack.shrink(1);
      }

      return broken;
    }
  }
}
