package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.api.IToolHandler;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ArtisanInventory
    implements IInventory {

  private final EnumTier tableTier;
  private final PlayerData playerData;
  private final ICraftingMatrixStackHandler craftingMatrix;
  private final FluidTank fluidTank;
  private final ItemStack[] tools;
  private final IToolHandler[] toolHandlers;
  private final IItemHandlerModifiable toolHandler;
  private final IItemHandlerModifiable toolReplacementHandler;
  private final ISecondaryIngredientMatcher secondaryIngredientMatcher;
  private final IItemHandlerModifiable secondaryIngredientHandler;
  private final IItemHandlerModifiable secondaryOutputHandler;
  private final int width;
  private final int height;

  public ArtisanInventory(
      EnumTier tableTier,
      @Nullable PlayerData playerData,
      ICraftingMatrixStackHandler craftingMatrix,
      FluidTank fluidTank,
      ItemStack[] tools,
      IToolHandler[] toolHandlers,
      IItemHandlerModifiable toolHandler,
      IItemHandlerModifiable toolReplacementHandler,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      IItemHandlerModifiable secondaryIngredientHandler,
      IItemHandlerModifiable secondaryOutputHandler,
      int width,
      int height
  ) {

    this.tableTier = tableTier;
    this.playerData = playerData;
    this.craftingMatrix = craftingMatrix;
    this.fluidTank = fluidTank;
    this.tools = tools;
    this.toolHandlers = toolHandlers;
    this.toolHandler = toolHandler;
    this.toolReplacementHandler = toolReplacementHandler;
    this.secondaryIngredientMatcher = secondaryIngredientMatcher;
    this.secondaryIngredientHandler = secondaryIngredientHandler;
    this.secondaryOutputHandler = secondaryOutputHandler;
    this.width = width;
    this.height = height;
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  public EnumTier getTableTier() {

    return this.tableTier;
  }

  public ICraftingMatrixStackHandler getCraftingMatrix() {

    return this.craftingMatrix;
  }

  public FluidTank getFluidTank() {

    return this.fluidTank;
  }

  public FluidStack getFluidStack() {

    return this.fluidTank.getFluid();
  }

  public Optional<PlayerData> getPlayerData() {

    return Optional.ofNullable(this.playerData);
  }

  public ItemStack[] getTools() {

    return this.tools;
  }

  public IToolHandler[] getToolHandlers() {

    return this.toolHandlers;
  }

  public IItemHandlerModifiable getToolHandler() {

    return this.toolHandler;
  }

  @Nullable
  public IItemHandlerModifiable getToolReplacementHandler() {

    return this.toolReplacementHandler;
  }

  public ISecondaryIngredientMatcher getSecondaryIngredientMatcher() {

    return this.secondaryIngredientMatcher;
  }

  public IItemHandlerModifiable getSecondaryIngredientHandler() {

    return this.secondaryIngredientHandler;
  }

  public IItemHandlerModifiable getSecondaryOutputHandler() {

    return this.secondaryOutputHandler;
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

  public static class PlayerData {

    public final boolean isCreative;
    public final int experience;
    public final int levels;

    public PlayerData(boolean isCreative, int experience, int levels) {

      this.isCreative = isCreative;
      this.experience = experience;
      this.levels = levels;
    }
  }
}