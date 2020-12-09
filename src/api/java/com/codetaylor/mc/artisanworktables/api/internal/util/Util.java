package com.codetaylor.mc.artisanworktables.api.internal.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class Util {

  public static final Random RANDOM = new Random();

  /**
   * Spawns an {@link ItemStack} in the world, directly above the given position.
   * <p>
   * Server only.
   *
   * @param world     the world
   * @param itemStack the {@link ItemStack} to spawn
   * @param pos       the position to spawn
   * @author codetaylor
   */
  public static void spawnStackOnTop(World world, ItemStack itemStack, BlockPos pos, double offsetY) {

    ItemEntity entityItem = new ItemEntity(
        world,
        pos.getX() + 0.5,
        pos.getY() + 0.5 + offsetY,
        pos.getZ() + 0.5,
        itemStack
    );
    entityItem.setMotion(0, 0.1, 0);

    world.addEntity(entityItem);
  }

  /**
   * Container sensitive decrease stack.
   * <p>
   * ie. bucket
   *
   * @param itemStack      the {@link ItemStack}
   * @param amount         decrease amount
   * @param checkContainer check for container
   * @return the resulting {@link ItemStack}
   * @author codetaylor
   */
  public static ItemStack decrease(ItemStack itemStack, int amount, boolean checkContainer) {

    if (itemStack.isEmpty()) {
      return ItemStack.EMPTY;
    }

    if (itemStack.getCount() - amount <= 0) {

      if (checkContainer && itemStack.getItem().hasContainerItem(itemStack)) {
        return itemStack.getItem().getContainerItem(itemStack);

      } else {
        return ItemStack.EMPTY;
      }
    }

    itemStack.shrink(amount);

    return itemStack;
  }

  @Nonnull
  public static ItemStack getContainerItem(@Nonnull ItemStack stack) {

    if (stack.getItem().hasContainerItem(stack)) {
      stack = stack.getItem().getContainerItem(stack);

      if (!stack.isEmpty() && stack.isDamageable() && stack.getDamage() <= stack.getMaxDamage()) {
        return ItemStack.EMPTY;
      }
      return stack;
    }
    return ItemStack.EMPTY;
  }

}
