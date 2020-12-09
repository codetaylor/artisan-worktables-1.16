package com.codetaylor.mc.artisanworktables.common.container.slot;

import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class CraftingExtraResultSlot
    extends ResultSlot
    implements ICreativeSlotClick {

  private final BaseTileEntity tile;

  public CraftingExtraResultSlot(
      BaseTileEntity tile,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.tile = tile;
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return this.tile.isCreative();
  }

  @Override
  public boolean canTakeStack(PlayerEntity player) {

    return !this.tile.isCreative()
        && super.canTakeStack(player);
  }

  @Override
  public ItemStack creativeSlotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {

    this.putStack(player.inventory.getItemStack().copy());
    return ItemStack.EMPTY;
  }
}
