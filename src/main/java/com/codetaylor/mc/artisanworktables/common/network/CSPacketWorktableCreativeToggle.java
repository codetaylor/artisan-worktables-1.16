package com.codetaylor.mc.artisanworktables.common.network;

import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.network.spi.packet.IMessage;
import com.codetaylor.mc.athenaeum.network.spi.packet.SPacketTileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CSPacketWorktableCreativeToggle
    extends SPacketTileEntityBase<CSPacketWorktableCreativeToggle> {

  @SuppressWarnings("unused")
  public CSPacketWorktableCreativeToggle() {
    // Serialization
  }

  public CSPacketWorktableCreativeToggle(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  protected IMessage<CSPacketWorktableCreativeToggle> onMessage(
      CSPacketWorktableCreativeToggle message,
      Supplier<NetworkEvent.Context> supplier,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof BaseTileEntity) {
      BaseTileEntity table = (BaseTileEntity) tileEntity;
      table.setCreative(!table.isCreative());
      table.setLocked(false);
      CSPacketWorktableClear.clear(table, CSPacketWorktableClear.CLEAR_ALL);
      table.markDirty();
    }

    supplier.get().setPacketHandled(true);

    return null;
  }
}
