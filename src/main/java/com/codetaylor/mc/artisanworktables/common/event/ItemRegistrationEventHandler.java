package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;
import java.util.Objects;

@ObjectHolder(ArtisanWorktablesMod.MOD_ID)
public class ItemRegistrationEventHandler {

  private final List<Block> registeredWorktables;

  public ItemRegistrationEventHandler(List<Block> registeredWorktables) {

    this.registeredWorktables = registeredWorktables;
  }

  @ObjectHolder("worktable_basic")
  public static final BlockItem BASIC_WORKTABLE;

  static {
    BASIC_WORKTABLE = null;
  }

  @SubscribeEvent
  public void on(RegistryEvent.Register<Item> event) {

    ItemGroup itemGroup = new ItemGroup(ArtisanWorktablesMod.MOD_ID) {

      @Override
      public ItemStack createIcon() {

        return new ItemStack(BASIC_WORKTABLE);
      }
    };

    IForgeRegistry<Item> registry = event.getRegistry();

    for (Block block : this.registeredWorktables) {
      BlockItem item = new BlockItem(block, new Item.Properties().group(itemGroup));
      item.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
      registry.register(item);
    }
  }
}
