package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.client.ClientProxy;
import com.codetaylor.mc.artisanworktables.common.CommonProxy;
import com.codetaylor.mc.artisanworktables.common.block.ToolboxBlock;
import com.codetaylor.mc.artisanworktables.common.block.ToolboxMechanicalBlock;
import com.codetaylor.mc.artisanworktables.common.container.*;
import com.codetaylor.mc.artisanworktables.common.reference.Reference;
import com.codetaylor.mc.artisanworktables.common.tile.ToolboxTileEntity;
import com.codetaylor.mc.artisanworktables.common.tile.WorkshopTileEntity;
import com.codetaylor.mc.artisanworktables.common.tile.WorkstationTileEntity;
import com.codetaylor.mc.artisanworktables.common.tile.WorktableTileEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.particles.BasicParticleType;
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
  public static class Blocks {

    @ObjectHolder(ToolboxBlock.NAME)
    public static final ToolboxBlock TOOLBOX;

    @ObjectHolder(ToolboxMechanicalBlock.NAME)
    public static final ToolboxMechanicalBlock MECHANICAL_TOOLBOX;

    static {
      TOOLBOX = null;
      MECHANICAL_TOOLBOX = null;
    }
  }

  @ObjectHolder(MOD_ID)
  public static class TileEntityTypes {

    @ObjectHolder(WorktableTileEntity.NAME)
    public static final TileEntityType<WorktableTileEntity> WORKTABLE;

    @ObjectHolder(WorkstationTileEntity.NAME)
    public static final TileEntityType<WorkstationTileEntity> WORKSTATION;

    @ObjectHolder(WorkshopTileEntity.NAME)
    public static final TileEntityType<WorkshopTileEntity> WORKSHOP;

    @ObjectHolder(ToolboxTileEntity.NAME)
    public static final TileEntityType<ToolboxTileEntity> TOOLBOX;

    static {
      WORKTABLE = null;
      WORKSTATION = null;
      WORKSHOP = null;
      TOOLBOX = null;
    }
  }

  @ObjectHolder(MOD_ID)
  public static class ContainerTypes {

    @ObjectHolder(WorktableContainer.NAME)
    public static final ContainerType<WorktableContainer> WORKTABLE;

    @ObjectHolder(WorkstationContainer.NAME)
    public static final ContainerType<WorkstationContainer> WORKSTATION;

    @ObjectHolder(WorkshopContainer.NAME)
    public static final ContainerType<WorkshopContainer> WORKSHOP;

    @ObjectHolder(ToolboxContainer.NAME)
    public static final ContainerType<ToolboxContainer> TOOLBOX;

    @ObjectHolder(ToolboxMechanicalContainer.NAME)
    public static final ContainerType<ToolboxMechanicalContainer> MECHANICAL_TOOLBOX;

    static {
      WORKTABLE = null;
      WORKSTATION = null;
      WORKSHOP = null;
      TOOLBOX = null;
      MECHANICAL_TOOLBOX = null;
    }
  }

  @ObjectHolder(MOD_ID)
  public static class ParticleTypes {

    @ObjectHolder("mage")
    public static final BasicParticleType MAGE;

    static {
      MAGE = null;
    }
  }
}