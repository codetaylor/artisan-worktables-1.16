package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.athenaeum.network.spi.packet.IPacketService;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.ITileDataService;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;

public interface IProxy {

  void initialize();

  void registerModEventHandlers(IEventBus eventBus);

  void registerForgeEventHandlers(IEventBus eventBus);

  List<Block> getRegisteredWorktables();

  ITileDataService getTileDataService();

  IPacketService getPacketService();

  boolean isIntegratedServerRunning();

  EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShaped();

  EnumMap<EnumType, IRecipeSerializer<? extends ArtisanRecipe>> getRegisteredSerializersShapeless();

  @Nullable
  RecipeManager getRecipeManager();
}
