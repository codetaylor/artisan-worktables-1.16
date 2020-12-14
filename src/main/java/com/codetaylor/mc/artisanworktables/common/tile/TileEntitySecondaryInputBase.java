package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.common.recipe.ISecondaryIngredientMatcher;
import com.codetaylor.mc.artisanworktables.common.recipe.CraftingContextFactory;
import com.codetaylor.mc.artisanworktables.common.recipe.SecondaryIngredientMatcher;
import com.codetaylor.mc.artisanworktables.common.tile.handler.MutuallyExclusiveStackHandlerWrapper;
import com.codetaylor.mc.athenaeum.inventory.spi.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.ITileDataService;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public abstract class TileEntitySecondaryInputBase
    extends BaseTileEntity {

  protected ObservableStackHandler secondaryIngredientHandler;
  protected MutuallyExclusiveStackHandlerWrapper wrapper;

  protected final LazyOptional<IItemHandler> itemCapability = LazyOptional.of(() -> this.secondaryIngredientHandler);

  // ---------------------------------------------------------------------------
  // Initialization
  // ---------------------------------------------------------------------------

  protected TileEntitySecondaryInputBase(TileEntityType<?> tileEntityType, ITileDataService tileDataService) {

    super(tileEntityType, tileDataService);
  }

  protected TileEntitySecondaryInputBase(TileEntityType<?> tileEntityType, ITileDataService tileDataService, EnumType type) {

    super(tileEntityType, tileDataService, type);
  }

  @Override
  protected void initialize(EnumType type) {

    super.initialize(type);
    this.secondaryIngredientHandler = new ObservableStackHandler(9);
    this.secondaryIngredientHandler.addObserver((stackHandler, slotIndex) -> {
      this.markDirty();
      this.requiresRecipeUpdate = true;
    });
    this.wrapper = new MutuallyExclusiveStackHandlerWrapper(this.secondaryIngredientHandler);
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  @Override
  public ICraftingContext getCraftingContext(PlayerEntity player) {

    return CraftingContextFactory.createContext(this, player, this.secondaryIngredientHandler);
  }

  @Override
  public ISecondaryIngredientMatcher getSecondaryIngredientMatcher() {

    int slotCount = this.secondaryIngredientHandler.getSlots();
    List<IArtisanItemStack> inputs = new ArrayList<>(slotCount);

    for (int i = 0; i < slotCount; i++) {
      ItemStack itemStack = this.secondaryIngredientHandler.getStackInSlot(i);
      inputs.add(ArtisanItemStack.from(itemStack));
    }

    return new SecondaryIngredientMatcher(inputs);
  }

  public IItemHandlerModifiable getSecondaryIngredientHandler() {

    return this.secondaryIngredientHandler;
  }

  // ---------------------------------------------------------------------------
  // Capability
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != Direction.DOWN) {
      return this.itemCapability.cast();
    }

    return super.getCapability(capability, side);
  }

  @ParametersAreNonnullByDefault
  @Override
  public void read(BlockState state, CompoundNBT nbt) {

    super.read(state, nbt);
    this.secondaryIngredientHandler.deserializeNBT(nbt.getCompound("secondaryIngredientHandler"));
  }

  // ---------------------------------------------------------------------------
  // Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public CompoundNBT write(@Nonnull CompoundNBT nbt) {

    nbt = super.write(nbt);
    nbt.put("secondaryIngredientHandler", this.secondaryIngredientHandler.serializeNBT());
    return nbt;
  }
}
