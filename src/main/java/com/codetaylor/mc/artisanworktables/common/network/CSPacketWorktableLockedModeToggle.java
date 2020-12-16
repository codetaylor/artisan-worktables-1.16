package com.codetaylor.mc.artisanworktables.common.network;

import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.network.spi.packet.IMessage;
import com.codetaylor.mc.athenaeum.network.spi.packet.SPacketTileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CSPacketWorktableLockedModeToggle
    extends SPacketTileEntityBase<CSPacketWorktableLockedModeToggle> {

  @SuppressWarnings("unused")
  public CSPacketWorktableLockedModeToggle() {
    // Serialization
  }

  public CSPacketWorktableLockedModeToggle(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  protected IMessage<CSPacketWorktableLockedModeToggle> onMessage(
      CSPacketWorktableLockedModeToggle message,
      Supplier<NetworkEvent.Context> supplier,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof BaseTileEntity) {
      BaseTileEntity table = (BaseTileEntity) tileEntity;

//      if (ModuleWorktablesConfig.allowSlotLockingForTier(table.getTableTier())) {
//        table.setLocked(!table.isLocked());
//        table.markDirty();
//      }
    }

    supplier.get().setPacketHandled(true);

    return null;
  }
}
