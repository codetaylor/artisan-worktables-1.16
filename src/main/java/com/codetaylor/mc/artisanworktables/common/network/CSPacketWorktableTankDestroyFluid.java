package com.codetaylor.mc.artisanworktables.common.network;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.network.spi.packet.IMessage;
import com.codetaylor.mc.athenaeum.network.spi.packet.SPacketTileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent from the client to the server to signal a fluid destroy.
 */
public class CSPacketWorktableTankDestroyFluid
    extends SPacketTileEntityBase<CSPacketWorktableTankDestroyFluid> {

  @SuppressWarnings("unused")
  public CSPacketWorktableTankDestroyFluid() {
    // Serialization
  }

  public CSPacketWorktableTankDestroyFluid(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  protected IMessage<CSPacketWorktableTankDestroyFluid> onMessage(
      CSPacketWorktableTankDestroyFluid message,
      Supplier<NetworkEvent.Context> supplier,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof BaseTileEntity) {
      FluidTank tank = ((BaseTileEntity) tileEntity).getTank();
      tank.drain(tank.getCapacity(), IFluidHandler.FluidAction.EXECUTE);
      ArtisanWorktablesMod.getProxy().getPacketService().sendToNear(
          tileEntity,
          new SCPacketWorktableFluidUpdate(tileEntity.getPos(), tank)
      );
    }

    supplier.get().setPacketHandled(true);

    return null;
  }
}
