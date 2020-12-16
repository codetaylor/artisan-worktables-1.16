package com.codetaylor.mc.artisanworktables.common.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class ArtisanInventory
    implements IInventory {

  private final ICraftingMatrixStackHandler craftingMatrix;
  private final FluidStack fluidStack;
  private final int width;
  private final int height;

  public ArtisanInventory(ICraftingMatrixStackHandler craftingMatrix, FluidStack fluidStack, int width, int height) {

    this.craftingMatrix = craftingMatrix;
    this.fluidStack = fluidStack;
    this.width = width;
    this.height = height;
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  public ICraftingMatrixStackHandler getCraftingMatrix() {

    return this.craftingMatrix;
  }

  public FluidStack getFluidStack() {

    return this.fluidStack;
  }

  // ---------------------------------------------------------------------------
  // Implementation
  // ---------------------------------------------------------------------------

  @Override
  public int getSizeInventory() {

    return this.width * this.height;
  }

  @Override
  public boolean isEmpty() {

    for (int i = 0; i < this.craftingMatrix.getSlots(); i++) {
      ItemStack itemStack = this.craftingMatrix.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        return false;
      }
    }

    return true;
  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int index) {

    if (index < 0 || index >= this.craftingMatrix.getSlots()) {
      return ItemStack.EMPTY;
    }

    return this.craftingMatrix.getStackInSlot(index);
  }

  @Nonnull
  @Override
  public ItemStack decrStackSize(int index, int count) {

    if (index < 0 || index >= this.craftingMatrix.getSlots()) {
      return ItemStack.EMPTY;
    }

    ItemStack stackInSlot = this.craftingMatrix.getStackInSlot(index);
    ItemStack splitStack = stackInSlot.copy().split(count);
    stackInSlot.setCount(stackInSlot.getCount() - splitStack.getCount());
    this.craftingMatrix.setStackInSlot(index, stackInSlot);
    return splitStack;
  }

  @Nonnull
  @Override
  public ItemStack removeStackFromSlot(int index) {

    if (index < 0 || index >= this.craftingMatrix.getSlots()) {
      return ItemStack.EMPTY;
    }

    ItemStack stackInSlot = this.craftingMatrix.getStackInSlot(index);
    this.craftingMatrix.setStackInSlot(index, ItemStack.EMPTY);
    return stackInSlot;
  }

  @Override
  public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {

    if (index < 0 || index >= this.craftingMatrix.getSlots()) {
      return;
    }

    this.craftingMatrix.setStackInSlot(index, stack);
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

    for (int i = 0; i < this.craftingMatrix.getSlots(); i++) {
      this.craftingMatrix.setStackInSlot(i, ItemStack.EMPTY);
    }
  }
}