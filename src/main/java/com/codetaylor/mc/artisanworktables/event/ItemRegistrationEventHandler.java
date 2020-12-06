package com.codetaylor.mc.artisanworktables.event;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.block.BasicWorktableBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemRegistrationEventHandler {

  @SubscribeEvent
  public void on(RegistryEvent.Register<Item> event) {

    ItemGroup itemGroup = new ItemGroup(ArtisanWorktablesMod.MOD_ID) {

      @Override
      public ItemStack createIcon() {

        return new ItemStack(
            ForgeRegistries.ITEMS.getValue(
                new ResourceLocation(ArtisanWorktablesMod.MOD_ID, BasicWorktableBlock.NAME)
            )
        );
      }
    };

    IForgeRegistry<Item> registry = event.getRegistry();

    Block[] blocks = new Block[]{
        ArtisanWorktablesMod.Blocks.BASIC_WORKTABLE
    };

    for (Block block : blocks) {
      BlockItem item = new BlockItem(block, new Item.Properties().group(itemGroup));
      item.setRegistryName(block.getRegistryName());
      registry.register(item);
    }
  }
}
