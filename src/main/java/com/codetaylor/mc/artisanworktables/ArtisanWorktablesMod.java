package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.api.Reference;
import com.codetaylor.mc.artisanworktables.client.ClientProxy;
import com.codetaylor.mc.artisanworktables.common.CommonProxy;
import com.codetaylor.mc.artisanworktables.common.container.WorkshopContainer;
import com.codetaylor.mc.artisanworktables.common.container.WorkstationContainer;
import com.codetaylor.mc.artisanworktables.common.container.WorktableContainer;
import com.codetaylor.mc.artisanworktables.common.tile.WorkshopTileEntity;
import com.codetaylor.mc.artisanworktables.common.tile.WorkstationTileEntity;
import com.codetaylor.mc.artisanworktables.common.tile.WorktableTileEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArtisanWorktablesMod.MOD_ID)
public class ArtisanWorktablesMod {

  public static final String MOD_ID = Reference.MOD_ID;
  public static final Logger LOGGER = LogManager.getLogger();
  public static final String PACKET_SERVICE_PROTOCOL_VERSION = "1";

  private static ArtisanWorktablesMod instance;

  private final IProxy proxy;

  public ArtisanWorktablesMod() {

    ArtisanWorktablesMod.instance = this;

    this.proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    this.proxy.initialize();
    this.proxy.registerModEventHandlers(FMLJavaModLoadingContext.get().getModEventBus());
    this.proxy.registerForgeEventHandlers(MinecraftForge.EVENT_BUS);
  }

  public static ArtisanWorktablesMod getInstance() {

    return ArtisanWorktablesMod.instance;
  }

  public static IProxy getProxy() {

    return ArtisanWorktablesMod.getInstance().proxy;
  }

  @ObjectHolder(MOD_ID)
  public static class TileEntityTypes {

    @ObjectHolder(WorktableTileEntity.NAME)
    public static final TileEntityType<WorktableTileEntity> WORKTABLE_TILE_ENTITY_TYPE;

    @ObjectHolder(WorkstationTileEntity.NAME)
    public static final TileEntityType<WorkstationTileEntity> WORKSTATION_TILE_ENTITY_TYPE;

    @ObjectHolder(WorkshopTileEntity.NAME)
    public static final TileEntityType<WorkshopTileEntity> WORKSHOP_TILE_ENTITY_TYPE;

    static {
      WORKTABLE_TILE_ENTITY_TYPE = null;
      WORKSTATION_TILE_ENTITY_TYPE = null;
      WORKSHOP_TILE_ENTITY_TYPE = null;
    }
  }

  @ObjectHolder(MOD_ID)
  public static class ContainerTypes {

    @ObjectHolder(WorktableContainer.NAME)
    public static final ContainerType<WorktableContainer> WORKTABLE_CONTAINER_TYPE;

    @ObjectHolder(WorkstationContainer.NAME)
    public static final ContainerType<WorkstationContainer> WORKSTATION_CONTAINER_TYPE;

    @ObjectHolder(WorkshopContainer.NAME)
    public static final ContainerType<WorkshopContainer> WORKSHOP_CONTAINER_TYPE;

    static {
      WORKTABLE_CONTAINER_TYPE = null;
      WORKSTATION_CONTAINER_TYPE = null;
      WORKSHOP_CONTAINER_TYPE = null;
    }
  }
}