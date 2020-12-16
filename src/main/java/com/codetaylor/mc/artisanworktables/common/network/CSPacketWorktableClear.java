package com.codetaylor.mc.artisanworktables.common.network;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import com.codetaylor.mc.artisanworktables.common.tile.TileEntitySecondaryInputBase;
import com.codetaylor.mc.athenaeum.network.spi.packet.IMessage;
import com.codetaylor.mc.athenaeum.network.spi.packet.SPacketTileEntityBase;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Supplier;

public class CSPacketWorktableClear
    extends SPacketTileEntityBase<CSPacketWorktableClear> {

  public static final int CLEAR_FLUID = 1;
  public static final int CLEAR_GRID = 2;
  public static final int CLEAR_TOOLS = 4;
  public static final int CLEAR_OUTPUT = 8;
  public static final int CLEAR_EXTRA_OUTPUT = 16;
  public static final int CLEAR_SECONDARY_INPUT = 32;

  public static final int CLEAR_ALL = CLEAR_FLUID | CLEAR_GRID | CLEAR_TOOLS | CLEAR_OUTPUT | CLEAR_EXTRA_OUTPUT | CLEAR_SECONDARY_INPUT;

  private int clearFlags;

  @SuppressWarnings("unused")
  public CSPacketWorktableClear() {
    // Serialization
  }

  public CSPacketWorktableClear(BlockPos blockPos, int clearFlags) {

    super(blockPos);
    this.clearFlags = clearFlags;
  }

  @Override
  public void encode(CSPacketWorktableClear message, PacketBuffer packetBuffer) {

    super.encode(message, packetBuffer);
    packetBuffer.writeInt(this.clearFlags);
  }

  @Override
  public CSPacketWorktableClear decode(CSPacketWorktableClear message, PacketBuffer packetBuffer) {

    super.decode(message, packetBuffer);
    message.clearFlags = packetBuffer.readInt();
    return message;
  }

  @Override
  protected IMessage<CSPacketWorktableClear> onMessage(
      CSPacketWorktableClear message,
      Supplier<NetworkEvent.Context> supplier,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof BaseTileEntity) {
      BaseTileEntity table = (BaseTileEntity) tileEntity;
      CSPacketWorktableClear.clear(table, message.clearFlags);
      tileEntity.markDirty();
    }

    supplier.get().setPacketHandled(true);

    return null;
  }

  public static void clear(BaseTileEntity tileEntity, int clearFlags) {

    if ((clearFlags & CLEAR_FLUID) == CLEAR_FLUID) {
      FluidTank tank = tileEntity.getTank();
      tank.drain(tank.getCapacity(), IFluidHandler.FluidAction.EXECUTE);
      ArtisanWorktablesMod.getProxy().getPacketService().sendToNear(
          tileEntity,
          new SCPacketWorktableFluidUpdate(tileEntity.getPos(), tank)
      );
    }

    if ((clearFlags & CLEAR_GRID) == CLEAR_GRID) {
      ICraftingMatrixStackHandler craftingMatrixHandler = tileEntity.getCraftingMatrixHandler();

      for (int i = 0; i < craftingMatrixHandler.getSlots(); i++) {
        craftingMatrixHandler.setStackInSlot(i, ItemStack.EMPTY);
      }
    }

    if ((clearFlags & CLEAR_OUTPUT) == CLEAR_OUTPUT) {
      ItemStackHandler resultHandler = tileEntity.getResultHandler();
      resultHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    if ((clearFlags & CLEAR_TOOLS) == CLEAR_TOOLS) {
      ItemStackHandler toolHandler = tileEntity.getToolHandler();

      for (int i = 0; i < toolHandler.getSlots(); i++) {
        toolHandler.setStackInSlot(i, ItemStack.EMPTY);
      }
    }

    if ((clearFlags & CLEAR_EXTRA_OUTPUT) == CLEAR_EXTRA_OUTPUT) {
      ItemStackHandler secondaryOutputHandler = tileEntity.getSecondaryOutputHandler();

      for (int i = 0; i < secondaryOutputHandler.getSlots(); i++) {
        secondaryOutputHandler.setStackInSlot(i, ItemStack.EMPTY);
      }
    }

    if (tileEntity instanceof TileEntitySecondaryInputBase
        && (clearFlags & CLEAR_SECONDARY_INPUT) == CLEAR_SECONDARY_INPUT) {
      IItemHandlerModifiable secondaryIngredientHandler = ((TileEntitySecondaryInputBase) tileEntity).getSecondaryIngredientHandler();

      for (int i = 0; i < secondaryIngredientHandler.getSlots(); i++) {
        secondaryIngredientHandler.setStackInSlot(i, ItemStack.EMPTY);
      }
    }
  }
}
