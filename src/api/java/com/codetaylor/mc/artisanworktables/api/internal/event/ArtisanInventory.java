package com.codetaylor.mc.artisanworktables.api.internal.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class ArtisanInventory
    implements IInventory {

  private final IItemHandlerModifiable stackHandler;
  private final int width;
  private final int height;

  public ArtisanInventory(IItemHandlerModifiable stackHandler, int width, int height) {

    this.stackHandler = stackHandler;
    this.width = width;
    this.height = height;
  }

  @Override
  public int getSizeInventory() {

    return this.width * this.height;
  }

  @Override
  public boolean isEmpty() {

    for (int i = 0; i < this.stackHandler.getSlots(); i++) {
      ItemStack itemStack = this.stackHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        return false;
      }
    }

    return true;
  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int index) {

    if (index < 0 || index >= this.stackHandler.getSlots()) {
      return ItemStack.EMPTY;
    }

    return this.stackHandler.getStackInSlot(index);
  }

  @Nonnull
  @Override
  public ItemStack decrStackSize(int index, int count) {

    if (index < 0 || index >= this.stackHandler.getSlots()) {
      return ItemStack.EMPTY;
    }

    ItemStack stackInSlot = this.stackHandler.getStackInSlot(index);
    ItemStack splitStack = stackInSlot.copy().split(count);
    stackInSlot.setCount(stackInSlot.getCount() - splitStack.getCount());
    this.stackHandler.setStackInSlot(index, stackInSlot);
    return splitStack;
  }

  @Nonnull
  @Override
  public ItemStack removeStackFromSlot(int index) {

    if (index < 0 || index >= this.stackHandler.getSlots()) {
      return ItemStack.EMPTY;
    }

    ItemStack stackInSlot = this.stackHandler.getStackInSlot(index);
    this.stackHandler.setStackInSlot(index, ItemStack.EMPTY);
    return stackInSlot;
  }

  @Override
  public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {

    if (index < 0 || index >= this.stackHandler.getSlots()) {
      return;
    }

    this.stackHandler.setStackInSlot(index, stack);
  }

  @Override
  public int getInventoryStackLimit() {

    return 64;
  }

  @Override
  public void markDirty() {
    //
  }

  @Override
  public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {

    return true;
  }

  @Override
  public void openInventory(@Nonnull PlayerEntity player) {
    //
  }

  @Override
  public void closeInventory(@Nonnull PlayerEntity player) {
    //
  }

  @Override
  public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {

    return true;
  }

  @Override
  public void clear() {

    for (int i = 0; i < this.stackHandler.getSlots(); i++) {
      this.stackHandler.setStackInSlot(i, ItemStack.EMPTY);
    }
  }
}