package com.codetaylor.mc.artisanworktables.common.network;

import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import com.codetaylor.mc.athenaeum.network.spi.packet.IMessage;
import com.codetaylor.mc.athenaeum.network.spi.packet.PacketBlockPosBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SCPacketWorktableContainerJoinedBlockBreak
    extends PacketBlockPosBase<SCPacketWorktableContainerJoinedBlockBreak> {

  @SuppressWarnings("unused")
  public SCPacketWorktableContainerJoinedBlockBreak() {
    // serialization
  }

  public SCPacketWorktableContainerJoinedBlockBreak(BlockPos blockPos) {

    super(blockPos);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public IMessage<SCPacketWorktableContainerJoinedBlockBreak> onMessage(
      SCPacketWorktableContainerJoinedBlockBreak message,
      Supplier<NetworkEvent.Context> supplier
  ) {

    Minecraft minecraft = Minecraft.getInstance();
    PlayerEntity player = minecraft.player;
    World world = minecraft.world;

    if (player != null
        && world != null
        && player.openContainer instanceof BaseContainer) {
      ((BaseContainer) player.openContainer).onJoinedBlockBreak(world, message.blockPos);
    }

    supplier.get().setPacketHandled(true);

    return null;
  }
}
