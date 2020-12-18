package com.codetaylor.mc.artisanworktables.common.network;

import com.codetaylor.mc.artisanworktables.common.container.ContainerProvider;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.athenaeum.network.spi.packet.IMessage;
import com.codetaylor.mc.athenaeum.network.spi.packet.SPacketTileEntityBase;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Sent from the client to the server to signal a worktable tab change.
 */
public class CSPacketWorktableTab
    extends SPacketTileEntityBase<CSPacketWorktableTab> {

  @SuppressWarnings("unused")
  public CSPacketWorktableTab() {
    // Serialization
  }

  public CSPacketWorktableTab(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  protected IMessage<CSPacketWorktableTab> onMessage(
      CSPacketWorktableTab message,
      Supplier<NetworkEvent.Context> supplier,
      TileEntity tileEntity
  ) {

    NetworkEvent.Context context = supplier.get();
    ServerPlayerEntity player = context.getSender();
    ItemStack heldStack = Objects.requireNonNull(player).inventory.getItemStack();

    if (!heldStack.isEmpty()) {
      player.inventory.setItemStack(ItemStack.EMPTY);
    }

    if (tileEntity instanceof BaseTileEntity) {
      BaseTileEntity table = (BaseTileEntity) tileEntity;
      ContainerProvider containerProvider = new ContainerProvider(table.getTableTier(), table.getTableType(), table.getWorld(), table.getPos());
      NetworkHooks.openGui(player, containerProvider, tileEntity.getPos());
    }

    if (!heldStack.isEmpty()) {
      player.inventory.setItemStack(heldStack);
      context.getNetworkManager().sendPacket(new SSetSlotPacket(-1, -1, heldStack));
    }

    context.setPacketHandled(true);

    return null;
  }
}
