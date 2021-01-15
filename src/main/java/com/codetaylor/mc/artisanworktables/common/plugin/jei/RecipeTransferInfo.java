package com.codetaylor.mc.artisanworktables.common.plugin.jei;

import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.tile.BaseTileEntity;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RecipeTransferInfo<C extends BaseContainer>
    implements IRecipeTransferInfo<C> {

  private final Class<C> containerClass;
  private final EnumTier tier;
  private final EnumType type;
  private final ResourceLocation uid;

  public RecipeTransferInfo(
      Class<C> containerClass,
      EnumTier tier,
      EnumType type,
      ResourceLocation uid
  ) {

    this.containerClass = containerClass;
    this.type = type;
    this.uid = uid;
    this.tier = tier;
  }

  @Nonnull
  @Override
  public Class<C> getContainerClass() {

    return this.containerClass;
  }

  @Nonnull
  @Override
  public ResourceLocation getRecipeCategoryUid() {

    return this.uid;
  }

  @Override
  public boolean canHandle(C container) {

    BaseTileEntity tile = container.getTile();
    EnumType type = tile.getTableType();
    int tableTierId = tile.getTableTier().getId();
    return this.type == type
        && this.tier != null
        && this.tier.getId() <= tableTierId;
  }

  @Nonnull
  @Override
  public List<Slot> getRecipeSlots(C container) {

    // grid

    /*
    2019-10-15 - Issue #196
    A smaller grid size is returned if the transfer handler making the request
    passes a smaller transferGridSize parameter. This prevents the smaller
    transfer handlers from making a mess in the larger tables.
     */

    List<Slot> result = new ArrayList<>();
    BaseTileEntity tile = container.getTile();
    int rowLength = (tile.getTableTier() == EnumTier.WORKSHOP) ? 5 : 3;
    int transferGridSize = (this.tier == EnumTier.WORKSHOP) ? 5 : 3;

    for (int row = 0; row < transferGridSize; row++) {
      for (int col = 0; col < transferGridSize; col++) {
        result.add(container.inventorySlots.get(container.slotIndexCraftingMatrixStart + col + (row * rowLength)));
      }
    }

    // tool
    for (int i = container.slotIndexToolsStart; i <= container.slotIndexToolsEnd; i++) {
      result.add(container.inventorySlots.get(i));
    }

    // Intentionally left out the secondary ingredient slots to prevent JEI from
    // transferring items to these slots when the transfer button is clicked.

    return result;
  }

  @Nonnull
  @Override
  public List<Slot> getInventorySlots(C container) {

    List<Slot> result = new ArrayList<>();

    for (int i = container.slotIndexInventoryStart; i <= container.slotIndexInventoryEnd; i++) {
      result.add(container.inventorySlots.get(i));
    }

    for (int i = container.slotIndexHotbarStart; i <= container.slotIndexHotbarEnd; i++) {
      result.add(container.inventorySlots.get(i));
    }

    if (container.canPlayerUseToolbox()) {

      for (int i = container.slotIndexToolboxStart; i <= container.slotIndexToolboxEnd; i++) {
        result.add(container.inventorySlots.get(i));
      }
    }

    for (int i = container.slotIndexSecondaryInputStart; i < container.slotIndexSecondaryInputEnd; i++) {
      result.add(container.inventorySlots.get(i));
    }

    return result;
  }

  @Override
  public boolean requireCompleteSets() {

    return false;
  }
}