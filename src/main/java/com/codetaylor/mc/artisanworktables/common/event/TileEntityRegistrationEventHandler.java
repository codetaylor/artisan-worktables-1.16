package com.codetaylor.mc.artisanworktables.common.event;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.tile.ToolboxTileEntity;
import com.codetaylor.mc.artisanworktables.common.tile.WorkshopTileEntity;
import com.codetaylor.mc.artisanworktables.common.tile.WorkstationTileEntity;
import com.codetaylor.mc.artisanworktables.common.tile.WorktableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TileEntityRegistrationEventHandler {

  private final Map<EnumTier, List<Block>> registeredWorktablesByTier;

  public TileEntityRegistrationEventHandler(Map<EnumTier, List<Block>> registeredWorktablesByTier) {

    this.registeredWorktablesByTier = registeredWorktablesByTier;
  }

  @SubscribeEvent
  public void on(RegistryEvent.Register<TileEntityType<?>> event) {

    IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();

    this.register(registry, EnumTier.WORKTABLE, WorktableTileEntity::new);
    this.register(registry, EnumTier.WORKSTATION, WorkstationTileEntity::new);
    this.register(registry, EnumTier.WORKSHOP, WorkshopTileEntity::new);

    registry.register(TileEntityType.Builder.create(ToolboxTileEntity::new, new Block[]{ArtisanWorktablesMod.Blocks.TOOLBOX, ArtisanWorktablesMod.Blocks.MECHANICAL_TOOLBOX}).build(null).setRegistryName("toolbox"));
  }

  private void register(IForgeRegistry<TileEntityType<?>> registry, EnumTier tier, Supplier<TileEntity> tileEntitySupplier) {

    List<Block> blockList = this.registeredWorktablesByTier.get(tier);
    Block[] blockArray = blockList.toArray(new Block[0]);
    registry.register(TileEntityType.Builder.create(tileEntitySupplier, blockArray).build(null).setRegistryName(tier.getName()));
  }
}