package com.codetaylor.mc.artisanworktables.common.recipe;

import com.codetaylor.mc.artisanworktables.api.IToolHandler;
import com.codetaylor.mc.artisanworktables.common.util.EnchantmentHelper;
import com.codetaylor.mc.artisanworktables.common.util.Util;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CraftHandler {

  public void doCraft(
      World world,
      BlockPos pos,
      @Nullable PlayerEntity player,
      ArtisanRecipe recipe,
      ArtisanInventory inventory,
      List<ItemStack> output
  ) {

    // Reduce player experience
    this.onCraftReduceExperience(player, recipe.consumeExperience(), recipe.getExperienceRequired(), recipe.getLevelRequired());

    ItemStack craftedItem = ItemStack.EMPTY;
    List<ItemStack> extraOutputList = new ArrayList<>(3);

    if (!world.isRemote) {
      // Check if the recipe has multiple, weighted outputs and swap outputs accordingly.
      craftedItem = recipe.getRecipeOutput();

      // This method must only be called on the server. It depends on RNG
      // and, as such, must be calculated with server authority.
      // Check for and populate secondary, tertiary and quaternary outputs
      extraOutputList = this.onCraftProcessExtraOutput(recipe.getExtraOutputs(), extraOutputList);
    }

    // Decrease stacks in crafting matrix
    this.onCraftReduceIngredients(inventory);

    // Decrease fluid
    this.onCraftReduceFluid(inventory.getFluidTank(), recipe.getFluidIngredient());

    // Decrease stacks in secondary ingredient slots
    this.onCraftReduceSecondaryIngredients(inventory.getSecondaryIngredientHandler(), recipe.consumeSecondaryIngredients(), recipe.getSecondaryIngredients(), player, pos, world);

    this.damageTools(recipe, inventory.getToolHandler(), recipe.getTools(), world, player, pos, inventory.getToolReplacementHandler());

    // Issue #150:
    // When shift-clicking a recipe, craftedItem was empty. Now, craftedItem should never be empty.
    // The craftedItem is the return value of calling onCraftCheckAndSwapWeightedOutput
    // and is only used here to determine if onCraftCompleteServer should be called.
    if (!world.isRemote
        && !craftedItem.isEmpty()) {
      this.onCraftCompleteServer(craftedItem, inventory, player);
    }

    if (output != null) {
      output.add(craftedItem);
      output.addAll(extraOutputList);
    }
  }

  private void onCraftReduceExperience(@Nullable PlayerEntity player, boolean consumeExperience, int experienceRequired, int levelRequired) {

    if (player == null) {
      return;
    }

    if (player.isCreative()) {
      // Don't consume experience when the player is in creative mode.
      return;
    }

    if (consumeExperience) {

      if (experienceRequired > 0) {
        EnchantmentHelper.adjustPlayerExperience(player, -experienceRequired);

      } else if (levelRequired > 0) {
        int experience = EnchantmentHelper.getExperienceToLevel(
            player.experienceLevel - levelRequired,
            player.experienceLevel
        );
        EnchantmentHelper.adjustPlayerExperience(player, -experience);
      }
    }
  }

  private List<ItemStack> onCraftProcessExtraOutput(
      NonNullList<ArtisanRecipe.ExtraOutputChancePair> extraOutputs,
      List<ItemStack> result
  ) {

    for (ArtisanRecipe.ExtraOutputChancePair extraOutput : extraOutputs) {

      if (Util.RANDOM.nextFloat() < extraOutput.getChance()) {
        result.add(extraOutput.getOutput());
      }
    }

    return result;
  }

  private void onCraftReduceIngredients(ArtisanInventory inventory) {

    ICraftingMatrixStackHandler matrixHandler = inventory.getCraftingMatrix();

    List<ItemStack> remainingItems = this.getRemainingItems(
        matrixHandler,
        NonNullList.withSize(matrixHandler.getSlots(), ItemStack.EMPTY)
    );

    for (int i = 0; i < remainingItems.size(); i++) {
      matrixHandler.setStackInSlot(i, remainingItems.get(i));
    }
  }

  /**
   * Returns the authoritative list of items remaining after a craft. Responsible
   * for reducing stack sizes. The results of this method will overwrite any
   * items in the crafting grid.
   *
   * @param matrixHandler the matrix handler
   * @param itemStacks    the resulting list
   * @return the authoritative list of items remaining after a craft
   */
  @Nonnull
  private List<ItemStack> getRemainingItems(
      ICraftingMatrixStackHandler matrixHandler,
      NonNullList<ItemStack> itemStacks
  ) {

    for (int i = 0; i < matrixHandler.getSlots(); i++) {
      itemStacks.set(i, Util.decrease(matrixHandler.getStackInSlot(i).copy(), 1, true));
    }

    return itemStacks;
  }

  private void onCraftReduceFluid(IFluidHandler fluidHandler, FluidStack fluidIngredient) {

    if (!fluidIngredient.isEmpty()) {
      fluidHandler.drain(fluidIngredient, IFluidHandler.FluidAction.EXECUTE);
    }
  }

  private void onCraftReduceSecondaryIngredients(IItemHandlerModifiable secondaryIngredientHandler, boolean consumeSecondaryIngredients, List<Ingredient> secondaryIngredients, @Nullable PlayerEntity player, BlockPos pos, World world) {

    if (!consumeSecondaryIngredients
        || secondaryIngredientHandler == null) {
      return;
    }

    if (!secondaryIngredients.isEmpty()) {
      // reduce secondary ingredients

      for (Ingredient requiredIngredient : secondaryIngredients) {
        int requiredAmount = 1; //requiredIngredient.getAmount();
        int slotCount = secondaryIngredientHandler.getSlots();

        for (int i = 0; i < slotCount; i++) {
          ItemStack stackInSlot = secondaryIngredientHandler.getStackInSlot(i);

          if (stackInSlot.isEmpty()) {
            continue;
          }

          if (requiredIngredient.test(stackInSlot)) {

            // get the remaining secondary item
            ItemStack remainingItemStack = this.getRemainingSecondaryItem(secondaryIngredientHandler, requiredIngredient, stackInSlot);
            int existingStackCount = stackInSlot.getCount();

            if (existingStackCount <= requiredAmount) {

              // Replace the entire stack in slot with remaining item.

              if (remainingItemStack.isEmpty()) {

                // If the remaining item is empty, empty the slot.
                secondaryIngredientHandler.setStackInSlot(i, ItemStack.EMPTY);

              } else {

                // First, empty the slot. Next, increase the remaining item count to match
                // the slot's previous stack count. Finally, attempt to insert the remaining
                // item into the secondary ingredient slots spilling any overflow into the
                // player's inventory or onto the ground.

                secondaryIngredientHandler.setStackInSlot(i, ItemStack.EMPTY);
                remainingItemStack.setCount(existingStackCount);
                ItemStack itemStack = secondaryIngredientHandler.insertItem(i, remainingItemStack, false);

                for (int j = 0; !itemStack.isEmpty() && j < secondaryIngredientHandler.getSlots() && j != i; j++) {
                  itemStack = secondaryIngredientHandler.insertItem(j, itemStack, false);
                }

                if (!itemStack.isEmpty()) {
                  this.addToInventoryOrDrop(itemStack, player, world, pos);
                }
              }

              requiredAmount -= existingStackCount;

            } else if (existingStackCount > requiredAmount) {

              // Replace partial stack in slot with remaining item.

              // First, decrease the existing stack size. Next, since we know that the
              // existing stack has been decreased by the required amount, we increase
              // the remaining item stack count by the required amount. Finally, attempt
              // to insert the remaining item stack into the secondary ingredient slots
              // spilling any overflow into the player's inventory or onto the ground.

              ItemStack decreasedStack = Util.decrease(stackInSlot.copy(), requiredAmount, false);
              secondaryIngredientHandler.setStackInSlot(i, decreasedStack);

              if (!remainingItemStack.isEmpty()) {
                remainingItemStack.setCount(requiredAmount);
                ItemStack itemStack = secondaryIngredientHandler.insertItem(i, remainingItemStack, false);

                for (int j = 0; !itemStack.isEmpty() && j < secondaryIngredientHandler.getSlots() && j != i; j++) {
                  itemStack = secondaryIngredientHandler.insertItem(j, itemStack, false);
                }

                if (!itemStack.isEmpty()) {
                  this.addToInventoryOrDrop(itemStack, player, world, pos);
                }
              }

              requiredAmount = 0;
            }

            if (requiredAmount == 0) {
              break;
            }
          }
        }

        if (requiredAmount > 0) {
          // TODO: failed to find all required ingredients... shouldn't happen if the matching code is correct
        }

      }
    }
  }

  private ItemStack getRemainingSecondaryItem(
      IItemHandlerModifiable secondaryIngredientHandler,
      Ingredient ingredient,
      ItemStack stack
  ) {

    return Util.getContainerItem(stack);
  }

  /**
   * If the player entity is present, add the stuff to the player's inventory
   * or drop in world, else just drop in world.
   *
   * @param itemStack the item stack to add or drop
   * @param player    the player, can be null
   * @param world     the world
   * @param position  the position to drop
   */
  private void addToInventoryOrDrop(ItemStack itemStack, @Nullable PlayerEntity player, World world, BlockPos position) {

    if (player != null) {

      if (!player.addItemStackToInventory(itemStack)) {
        player.dropItem(itemStack, false);
      }

    } else {
      StackHelper.spawnStackOnTop(world, itemStack, position);
    }
  }

  private void damageTools(ArtisanRecipe recipe, IItemHandlerModifiable toolStackHandler, NonNullList<ToolEntry> tools, World world, @Nullable PlayerEntity player, BlockPos position, @Nullable IItemHandler toolReplacementHandler) {

    // Damage or destroy tools
    // Check for replacement tool
    long mask = 0;

    recipeTools:
    for (ToolEntry tool : tools) { // recipe tools

      for (int j = 0; j < toolStackHandler.getSlots(); j++) { // table tools
        int bit = 1 << j;
        ItemStack stackInSlot = toolStackHandler.getStackInSlot(j);

        if ((mask & bit) != bit // haven't damaged this slot
            && !stackInSlot.isEmpty()) { // slot isn't empty
          IToolHandler toolHandler = ArtisanToolHandlers.get(stackInSlot);

          if (tool.matches(toolHandler, stackInSlot) // tool matches recipe
              && recipe.hasSufficientToolDurability(toolHandler, stackInSlot)) { // tool has durability

            mask |= bit;

            if (this.onCraftDamageTool(recipe, stackInSlot, world, player)) {

              if (!world.isRemote) {
                world.playSound(
                    null,
                    position.getX(),
                    position.getY(),
                    position.getZ(),
                    SoundEvents.ENTITY_ITEM_BREAK,
                    SoundCategory.PLAYERS,
                    1.0f,
                    1.0f
                );
              }
            }

            if (toolReplacementHandler != null) {
              // TODO: review this method, looks like it will fail if the tool is broken and emptied
              // by the damage logic above
              this.onCraftCheckAndReplaceTool(recipe, j, toolStackHandler, toolReplacementHandler);
            }
            continue recipeTools;
          }
        }
      }
    }
  }

  private boolean onCraftDamageTool(ArtisanRecipe recipe, ItemStack itemStack, World world, @Nullable PlayerEntity player) {

    if (!itemStack.isEmpty()) {
      IToolHandler toolHandler = ArtisanToolHandlers.get(itemStack);
      ToolEntry toolEntry = recipe.findToolEntry(toolHandler, itemStack);

      if (toolEntry == null) {
        return false;
      }

      if (!recipe.hasSufficientToolDurability(toolHandler, itemStack)) {
        return false;
      }

      int toolDamage = toolEntry.getDamage();

      return toolDamage > 0
          && toolHandler.applyDamage(world, itemStack, toolDamage, player, false);
    }

    return false;
  }

  private void onCraftCheckAndReplaceTool(ArtisanRecipe recipe, int toolIndex, IItemHandlerModifiable toolStackHandler, IItemHandler capability) {

    ItemStack itemStack = toolStackHandler.getStackInSlot(toolIndex);
    IToolHandler toolHandler = ArtisanToolHandlers.get(itemStack);

    if (!recipe.hasSufficientToolDurability(toolHandler, itemStack)) {
      // Tool needs to be replaced

      if (capability == null) {
        return;
      }

      int slotCount = capability.getSlots();

      for (int i = 0; i < slotCount; i++) {
        ItemStack potentialTool = capability.getStackInSlot(i);

        if (potentialTool.isEmpty()) {
          continue;
        }

        IToolHandler potentialToolHandler = ArtisanToolHandlers.get(potentialTool);
        ToolEntry toolEntry = recipe.findToolEntry(potentialToolHandler, potentialTool);

        if (toolEntry != null
            && toolEntry.matches(toolHandler, itemStack)
            && recipe.hasSufficientToolDurability(potentialToolHandler, potentialTool)) {
          // Found an acceptable tool
          potentialTool = capability.extractItem(i, 1, false);
          capability.insertItem(i, toolStackHandler.getStackInSlot(toolIndex), false);
          toolStackHandler.setStackInSlot(toolIndex, potentialTool);
        }
      }
    }
  }

  protected void onCraftCompleteServer(
      ItemStack craftedItem,
      ArtisanInventory inventory,
      @Nullable PlayerEntity player
  ) {

    if (player == null) {
      return;
    }

    MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(
        player,
        craftedItem.copy(),
        inventory
    ));
  }
}
