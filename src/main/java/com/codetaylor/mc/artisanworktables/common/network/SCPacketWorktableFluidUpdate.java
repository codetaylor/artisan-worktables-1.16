package com.codetaylor.mc.artisanworktables.common.network;

import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.network.spi.packet.CPacketTileEntityBase;
import com.codetaylor.mc.athenaeum.network.spi.packet.IMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SCPacketWorktableFluidUpdate
    extends CPacketTileEntityBase<SCPacketWorktableFluidUpdate> {

  private FluidTank fluidTank;

  @SuppressWarnings("unused")
  public SCPacketWorktableFluidUpdate() {
    // serialization
  }

  public SCPacketWorktableFluidUpdate(BlockPos blockPos, FluidTank fluidTank) {

    super(blockPos);
    this.fluidTank = fluidTank;
  }

  @Override
  public SCPacketWorktableFluidUpdate decode(SCPacketWorktableFluidUpdate message, PacketBuffer packetBuffer) {

    super.decode(message, packetBuffer);

    CompoundNBT compound = packetBuffer.readCompoundTag();
    message.fluidTank = new FluidTank(0);

    if (compound != null) {
      message.fluidTank.readFromNBT(compound);
    }

    return message;
  }

  @Override
  public void encode(SCPacketWorktableFluidUpdate message, PacketBuffer packetBuffer) {

    super.encode(message, packetBuffer);

    packetBuffer.writeCompoundTag(message.fluidTank.writeToNBT(new CompoundNBT()));
  }

  @Override
  protected IMessage<SCPacketWorktableFluidUpdate> onMessage(
      SCPacketWorktableFluidUpdate message,
      Supplier<NetworkEvent.Context> supplier,
      TileEntity tileEntity
  ) {

    if (tileEntity != null) {
      BaseTileEntity tileEntityBase = (BaseTileEntity) tileEntity;
      tileEntityBase.getTank().setFluid(message.fluidTank.getFluid());

      // We don't force a container recipe update here because it's triggered
      // when the tank's onContentsChanged() method is called.
    }

    supplier.get().setPacketHandled(true);

    return null;
  }
}
