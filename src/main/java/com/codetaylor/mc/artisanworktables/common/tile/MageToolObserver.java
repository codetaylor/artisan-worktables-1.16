package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.common.block.MageBaseBlock;
import com.codetaylor.mc.athenaeum.inventory.spi.ObservableStackHandler;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class MageToolObserver
    implements ObservableStackHandler.IContentsChangedEventHandler {

  private final BaseTileEntity tile;

  public MageToolObserver(BaseTileEntity tile) {

    this.tile = tile;
  }

  @Override
  public void onContentsChanged(ItemStackHandler stackHandler, int slotIndex) {

    this.checkToolState(stackHandler);
  }

  public void checkToolState(ItemStackHandler stackHandler) {

    World world = this.tile.getWorld();

    if (world == null || world.isRemote) {
      return;
    }

    BlockPos pos = this.tile.getPos();
    BlockState blockState = world.getBlockState(pos);

    if (blockState.getBlock() instanceof MageBaseBlock) {
      boolean empty = true;

      for (int i = 0; i < stackHandler.getSlots(); i++) {

        if (!stackHandler.getStackInSlot(i).isEmpty()) {
          empty = false;
          break;
        }
      }

      if (empty && blockState.get(MageBaseBlock.ACTIVE)) {
        world.setBlockState(pos, blockState.with(MageBaseBlock.ACTIVE, false));

      } else if (!empty && !blockState.get(MageBaseBlock.ACTIVE)) {
        world.setBlockState(pos, blockState.with(MageBaseBlock.ACTIVE, true));
      }
    }
  }
}
