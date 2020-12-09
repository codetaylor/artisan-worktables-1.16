package com.codetaylor.mc.artisanworktables.api.recipe;

import com.codetaylor.mc.artisanworktables.api.ArtisanToolHandlers;
import com.codetaylor.mc.artisanworktables.api.EnumTier;
import com.codetaylor.mc.artisanworktables.api.Reference;
import com.codetaylor.mc.artisanworktables.api.event.ArtisanCraftEvent;
import com.codetaylor.mc.artisanworktables.api.internal.event.ArtisanInventory;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.internal.util.EnchantmentHelper;
import com.codetaylor.mc.artisanworktables.api.internal.util.Util;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementContext;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class ArtisanRecipe
    implements IArtisanRecipe {

  private String name;
  private ToolEntry[] tools;
  private Map<ResourceLocation, IRequirement<?>> requirementMap;
  private IArtisanItemStack output;
  private List<IArtisanIngredient> ingredients;
  private List<IArtisanIngredient> secondaryIngredients;
  private boolean consumeSecondaryIngredients;
  private FluidStack fluidIngredient;
  private ExtraOutputChancePair[] extraOutputs;
  private IRecipeMatrixMatcher recipeMatrixMatcher;
  private boolean mirrored;
  private int width;
  private int height;
  private int minimumTier;
  private int maximumTier;
  private int experienceRequired;
  private int levelRequired;
  private boolean consumeExperience;
  private boolean hidden;

  @ParametersAreNonnullByDefault
  public ArtisanRecipe(
      @Nullable String name,
      Map<ResourceLocation, IRequirement<?>> requirementMap,
      IArtisanItemStack output,
      ToolEntry[] tools,
      List<IArtisanIngredient> ingredients,
      List<IArtisanIngredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
      @Nullable FluidStack fluidIngredient,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience,
      ExtraOutputChancePair[] extraOutputs,
      IRecipeMatrixMatcher recipeMatrixMatcher,
      boolean mirrored,
      int width,
      int height,
      int minimumTier,
      int maximumTier,
      boolean hidden
  ) {

    this.name = name;
    this.requirementMap = Collections.unmodifiableMap(requirementMap);
    this.output = output;
    this.tools = tools;
    this.ingredients = ingredients;
    this.secondaryIngredients = Collections.unmodifiableList(secondaryIngredients);
    this.consumeSecondaryIngredients = consumeSecondaryIngredients;
    this.fluidIngredient = fluidIngredient;
    this.experienceRequired = experienceRequired;
    this.levelRequired = levelRequired;
    this.consumeExperience = consumeExperience;
    this.extraOutputs = extraOutputs;
    this.recipeMatrixMatcher = recipeMatrixMatcher;
    this.mirrored = mirrored;
    this.width = width;
    this.height = height;
    this.minimumTier = minimumTier;
    this.maximumTier = maximumTier;
    this.hidden = hidden;
  }

  // --------------------------------------------------------------------------
  // - Getters

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public int getExperienceRequired() {

    return this.experienceRequired;
  }

  @Override
  public int getLevelRequired() {

    return this.levelRequired;
  }

  @Override
  public boolean consumeExperience() {

    return this.consumeExperience;
  }

  @Override
  public IArtisanItemStack getSecondaryOutput() {

    if (this.extraOutputs[0].getOutput().isEmpty()) {
      return ArtisanItemStack.EMPTY;
    }

    return this.extraOutputs[0].getOutput();
  }

  @Override
  public float getSecondaryOutputChance() {

    return this.extraOutputs[0].getChance();
  }

  @Override
  public IArtisanItemStack getTertiaryOutput() {

    if (this.extraOutputs[1].getOutput().isEmpty()) {
      return ArtisanItemStack.EMPTY;
    }

    return this.extraOutputs[1].getOutput();
  }

  @Override
  public float getTertiaryOutputChance() {

    return this.extraOutputs[1].getChance();
  }

  @Override
  public IArtisanItemStack getQuaternaryOutput() {

    if (this.extraOutputs[2].getOutput().isEmpty()) {
      return ArtisanItemStack.EMPTY;
    }

    return this.extraOutputs[2].getOutput();
  }

  @Override
  public float getQuaternaryOutputChance() {

    return this.extraOutputs[2].getChance();
  }

  @Nonnull
  @Override
  public List<IArtisanIngredient> getSecondaryIngredients() {

    return this.secondaryIngredients;
  }

  @Override
  public boolean consumeSecondaryIngredients() {

    return this.consumeSecondaryIngredients;
  }

  @Override
  public IArtisanItemStack[] getTools(int toolIndex) {

    if (toolIndex >= this.tools.length) {
      return new IArtisanItemStack[0];
    }

    return this.tools[toolIndex].getToolStacks();
  }

  @Override
  public ToolEntry[] getToolEntries() {

    return this.tools;
  }

  @Override
  public List<IArtisanIngredient> getIngredientList() {

    return Collections.unmodifiableList(this.ingredients);
  }

  @Override
  @Nullable
  public FluidStack getFluidIngredient() {

    if (this.fluidIngredient != null) {
      return this.fluidIngredient.copy();
    }

    return null;
  }

  @Override
  public IArtisanItemStack getOutput() {

    return this.output.copy();
  }

  @Override
  public int getToolDamage(int toolIndex) {

    if (toolIndex >= this.tools.length) {
      return 0;
    }

    return this.tools[toolIndex].getDamage();
  }

  @Override
  public int getWidth() {

    return this.width;
  }

  @Override
  public int getHeight() {

    return this.height;
  }

  @Override
  public boolean isShaped() {

    return this.width > 0 && this.height > 0;
  }

  @Override
  public boolean isMirrored() {

    return this.mirrored;
  }

  @Override
  public int getToolCount() {

    return this.tools.length;
  }

  @Nullable
  @Override
  public <C extends IRequirementContext> IRequirement<C> getRequirement(ResourceLocation resourceLocation) {

    //noinspection unchecked
    return (IRequirement<C>) this.requirementMap.get(resourceLocation);
  }

  @Override
  public Map<ResourceLocation, IRequirement<?>> getRequirements() {

    return this.requirementMap;
  }

  @Override
  public boolean isHidden() {

    return this.hidden;
  }

  // --------------------------------------------------------------------------
  // - Matching

  @Nullable
  private ToolEntry findToolEntry(IToolHandler handler, ItemStack tool) {

    for (ToolEntry toolEntry : this.tools) {

      if (toolEntry.matches(handler, tool)) {
        return toolEntry;
      }
    }
    return null;
  }

  @Override
  public boolean usesTool(IToolHandler handler, ItemStack tool) {

    for (ToolEntry toolEntry : this.tools) {

      if (toolEntry.matches(handler, tool)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean hasSufficientToolDurability(IToolHandler handler, ItemStack tool) {

    if (tool.isEmpty()) {
      return false;
    }

    if (Reference.Config.restrictCraftMinimumDurability) {
      ToolEntry toolEntry = this.findToolEntry(handler, tool);

      if (toolEntry != null) {
        int toolDamage = toolEntry.getDamage();
        return handler.canAcceptAllDamage(tool, toolDamage);
      }
    }

    return true;
  }

  @Override
  public boolean matches(
      Map<ResourceLocation, IRequirementContext> requirementContextMap,
      int playerExperienceTotal,
      int playerLevels,
      boolean isPlayerCreative,
      ItemStack[] tools,
      IToolHandler[] toolHandlers,
      ICraftingMatrixStackHandler craftingMatrix,
      FluidStack fluidStack,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      EnumTier tier
  ) {

    if (!this.matchTier(tier)) {
      return false;
    }

    if (!isPlayerCreative) {

      if (playerExperienceTotal < this.experienceRequired) {
        return false;
      }

      if (playerLevels < this.levelRequired) {
        return false;
      }
    }

    if (this.getToolCount() > tools.length) {
      // this recipe requires more tools than the number of tools available in the table
      return false;
    }

    if (!this.matchesRequirements(requirementContextMap)) {
      return false;
    }

    if (!this.recipeMatrixMatcher.matches(this, craftingMatrix, fluidStack)) {
      return false;
    }

    if (!this.secondaryIngredients.isEmpty()
        && !secondaryIngredientMatcher.matches(this.secondaryIngredients)) {
      return false;
    }

    // We need to match each recipe tool to a tool in the table
    // Each time a table tool is matched, it needs to be removed from the tools to match
    // so each tool in the table can only be matched once.

    // table_tools
    // recipe_tools
    // boolean[] recipe_tools_matched

    /*

    table:
    for each table_tool in table_tools
      for each recipe_tool in recipe_tools

        // does this table_tool satisfy a required recipe_tool?
        if !recipe_tools_matched[recipe_tool_index] && table_tool == recipe_tool
          recipe_tools_matched[recipe_tool_index] = true

     */

    return this.matchesTools(tools, toolHandlers);
  }

  @Override
  public boolean matchesTools(ItemStack[] tools, IToolHandler[] toolHandlers) {

    int toolCount = this.getToolCount();
    byte mask = 0;
    byte matchCount = 0;

    tableTools:
    for (int i = 0; i < tools.length; i++) {

      for (int j = 0; j < toolCount; j++) {
        int bit = (1 << j);

        // Has the recipe tool already been matched?
        // Is the table tool valid?
        // Does the table tool have sufficient durability?
        if ((mask & bit) != bit
            && this.tools[j].matches(toolHandlers[i], tools[i])
            && this.hasSufficientToolDurability(toolHandlers[i], tools[i])) {
          mask |= bit;
          matchCount += 1;
          continue tableTools;
        }
      }
    }

    return (matchCount == toolCount);
  }

  @Override
  public boolean matchesRequirements(Map<ResourceLocation, IRequirementContext> requirementContextMap) {

    // match requirements
    for (IRequirement requirement : this.requirementMap.values()) {

      ResourceLocation location = requirement.getResourceLocation();
      IRequirementContext context = requirementContextMap.get(location);

      if (context == null) {
        return false;
      }

      //noinspection unchecked
      if (!requirement.match(context)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean matchTier(EnumTier tier) {

    return this.minimumTier <= tier.getId()
        && this.maximumTier >= tier.getId();
  }

  // --------------------------------------------------------------------------
  // - Crafting

  @Override
  public void doCraft(
      ICraftingContext context,
      List<ItemStack> output
  ) {

    World world = context.getWorld();

    // Reduce player experience
    this.onCraftReduceExperience(context);

    ItemStack craftedItem = ItemStack.EMPTY;
    List<IArtisanItemStack> extraOutputList = new ArrayList<>(3);

    if (!world.isRemote) {
      craftedItem = this.getOutput().toItemStack();

      // This methods must only be called on the server. They depend on RNG
      // and, as such, must be calculated with server authority.
      // Check for and populate secondary, tertiary and quaternary outputs
      extraOutputList = this.onCraftProcessExtraOutput(context, extraOutputList);
    }

    // Decrease stacks in crafting matrix
    this.onCraftReduceIngredients(context);

    // Decrease fluid
    this.onCraftReduceFluid(context);

    // Decrease stacks in secondary ingredient slots
    this.onCraftReduceSecondaryIngredients(context);

    this.damageTools(context.getToolHandler(), context.getWorld(), context.getPlayer().orElse(null), context.getPosition(), context.getToolReplacementHandler());

    // Issue #150:
    // When shift-clicking a recipe, craftedItem was empty. Now, craftedItem should never be empty.
    // The craftedItem is the return value of calling onCraftCheckAndSwapWeightedOutput
    // and is only used here to determine if onCraftCompleteServer should be called.
    if (!world.isRemote
        && !craftedItem.isEmpty()) {
      this.onCraftCompleteServer(craftedItem, extraOutputList, context);
    }

    if (output != null) {
      output.add(craftedItem);

      for (IArtisanItemStack artisanItemStack : extraOutputList) {
        output.add(artisanItemStack.toItemStack());
      }
    }
  }

  @Override
  public void damageTools(IItemHandlerModifiable toolStackHandler, World world, @Nullable PlayerEntity player, BlockPos position, @Nullable IItemHandler toolReplacementHandler) {

    // Damage or destroy tools
    // Check for replacement tool
    long mask = 0;

    recipeTools:
    for (int i = 0; i < this.tools.length; i++) { // recipe tools

      for (int j = 0; j < toolStackHandler.getSlots(); j++) { // table tools
        int bit = 1 << j;
        ItemStack stackInSlot = toolStackHandler.getStackInSlot(j);

        if ((mask & bit) != bit // haven't damaged this slot
            && !stackInSlot.isEmpty()) { // slot isn't empty
          IToolHandler toolHandler = ArtisanToolHandlers.get(stackInSlot);

          if (this.tools[i].matches(toolHandler, stackInSlot) // tool matches recipe
              && this.hasSufficientToolDurability(toolHandler, stackInSlot)) { // tool has durability

            mask |= bit;

            if (this.onCraftDamageTool(stackInSlot, world, player)) {

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
              this.onCraftCheckAndReplaceTool(j, toolStackHandler, toolReplacementHandler);
            }
            continue recipeTools;
          }
        }
      }
    }
  }

  protected void onCraftCompleteServer(
      ItemStack craftedItem,
      List<IArtisanItemStack> extraOutputList,
      ICraftingContext context
  ) {

    if (!context.getPlayer().isPresent()) {
      return;
    }

    List<ItemStack> extraOutputConvertedList = new ArrayList<>(extraOutputList.size());

    for (IArtisanItemStack artisanItemStack : extraOutputList) {
      extraOutputConvertedList.add(artisanItemStack.toItemStack());
    }

    MinecraftForge.EVENT_BUS.post(new ArtisanCraftEvent.Post(
        context.getPlayer().get(),
        context.getType(),
        context.getTier(),
        craftedItem.copy(),
        extraOutputConvertedList
    ));

    MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(
        context.getPlayer().get(),
        craftedItem.copy(),
        new ArtisanInventory(
            context.getCraftingMatrixStackHandler(),
            context.getCraftingMatrixStackHandler().getWidth(),
            context.getCraftingMatrixStackHandler().getHeight()
        )
    ));
  }

  protected void onCraftReduceFluid(ICraftingContext context) {

    FluidStack fluidIngredient = this.getFluidIngredient();

    if (fluidIngredient != null) {
      IFluidHandler fluidHandler = context.getFluidHandler();
      fluidHandler.drain(fluidIngredient, IFluidHandler.FluidAction.EXECUTE);
    }
  }

  protected void onCraftReduceIngredients(ICraftingContext context) {

    IItemHandlerModifiable matrixHandler = context.getCraftingMatrixStackHandler();

    List<ItemStack> remainingItems = this.getRemainingItems(
        context,
        NonNullList.withSize(matrixHandler.getSlots(), ItemStack.EMPTY)
    );

    for (int i = 0; i < remainingItems.size(); i++) {
      matrixHandler.setStackInSlot(i, remainingItems.get(i));
    }
  }

  /**
   * If the player entity is present, add the stuff to the player's inventory
   * or drop in world, else just drop in world.
   *
   * @param itemStack      the item stack to add or drop
   * @param optionalPlayer the optional player
   * @param world          the world
   * @param position       the position to drop
   */
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private void addToInventoryOrDrop(ItemStack itemStack, Optional<PlayerEntity> optionalPlayer, World world, BlockPos position) {

    if (optionalPlayer.isPresent()) {
      PlayerEntity player = optionalPlayer.get();

      if (!player.addItemStackToInventory(itemStack)) {
        player.dropItem(itemStack, false);
      }

    } else {
      StackHelper.spawnStackOnTop(world, itemStack, position);
    }
  }

  /**
   * Returns the authoritative list of items remaining after a craft. Responsible
   * for reducing stack sizes. The results of this method will overwrite any
   * items in the crafting grid.
   *
   * @param context    the crafting context
   * @param itemStacks the resulting list
   * @return the authoritative list of items remaining after a craft
   */
  @Nonnull
  protected List<ItemStack> getRemainingItems(
      ICraftingContext context,
      NonNullList<ItemStack> itemStacks
  ) {

    ICraftingMatrixStackHandler matrixHandler = context.getCraftingMatrixStackHandler();

    for (int i = 0; i < matrixHandler.getSlots(); i++) {
      itemStacks.set(i, Util.decrease(matrixHandler.getStackInSlot(i).copy(), 1, true));
    }

    return itemStacks;
  }

  protected void onCraftReduceSecondaryIngredients(ICraftingContext context) {

    IItemHandlerModifiable secondaryIngredientHandler = context.getSecondaryIngredientHandler();

    if (!this.consumeSecondaryIngredients()
        || secondaryIngredientHandler == null) {
      return;
    }

    List<IArtisanIngredient> secondaryIngredients = this.secondaryIngredients;

    if (!secondaryIngredients.isEmpty()) {
      // reduce secondary ingredients

      for (IArtisanIngredient requiredIngredient : secondaryIngredients) {
        int requiredAmount = requiredIngredient.getAmount();
        int slotCount = secondaryIngredientHandler.getSlots();

        for (int i = 0; i < slotCount; i++) {
          ItemStack stackInSlot = secondaryIngredientHandler.getStackInSlot(i);

          if (stackInSlot.isEmpty()) {
            continue;
          }

          if (requiredIngredient.matchesIgnoreAmount(stackInSlot)) {

            // get the remaining secondary item
            ItemStack remainingItemStack = this.getRemainingSecondaryItem(context, requiredIngredient, stackInSlot);
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
                  this.addToInventoryOrDrop(itemStack, context.getPlayer(), context.getWorld(), context.getPosition());
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
                  this.addToInventoryOrDrop(itemStack, context.getPlayer(), context.getWorld(), context.getPosition());
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

  protected ItemStack getRemainingSecondaryItem(
      ICraftingContext context,
      IArtisanIngredient ingredient,
      ItemStack stack
  ) {

    return Util.getContainerItem(stack);
  }

  protected void onCraftReduceExperience(ICraftingContext context) {

    if (!context.getPlayer().isPresent()) {
      return;
    }

    PlayerEntity player = context.getPlayer().get();

    if (player.isCreative()) {
      // Don't consume experience when the player is in creative mode.
      return;
    }

    if (this.consumeExperience()) {

      if (this.getExperienceRequired() > 0) {
        EnchantmentHelper.adjustPlayerExperience(player, -this.getExperienceRequired());

      } else if (this.getLevelRequired() > 0) {
        int experience = EnchantmentHelper.getExperienceToLevel(
            player.experienceLevel - this.getLevelRequired(),
            player.experienceLevel
        );
        EnchantmentHelper.adjustPlayerExperience(player, -experience);
      }
    }
  }

  @Nonnull
  protected List<IArtisanItemStack> onCraftProcessExtraOutput(
      ICraftingContext context,
      List<IArtisanItemStack> result
  ) {

    IArtisanItemStack extraOutput = this.getSecondaryOutput();

    if (!extraOutput.isEmpty()) {

      if (Util.RANDOM.nextFloat() < this.getSecondaryOutputChance()) {
        result.add(this.generateExtraOutput(context, extraOutput));
      }
    }

    extraOutput = this.getTertiaryOutput();

    if (!extraOutput.isEmpty()) {

      if (Util.RANDOM.nextFloat() < this.getTertiaryOutputChance()) {
        result.add(this.generateExtraOutput(context, extraOutput));
      }
    }

    extraOutput = this.getQuaternaryOutput();

    if (!extraOutput.isEmpty()) {

      if (Util.RANDOM.nextFloat() < this.getQuaternaryOutputChance()) {
        result.add(this.generateExtraOutput(context, extraOutput));
      }
    }

    return result;
  }

  /**
   * @return true if the tool was damaged and broken
   */
  protected boolean onCraftDamageTool(ItemStack itemStack, World world, @Nullable PlayerEntity player) {

    if (!itemStack.isEmpty()) {
      IToolHandler toolHandler = ArtisanToolHandlers.get(itemStack);
      ToolEntry toolEntry = this.findToolEntry(toolHandler, itemStack);

      if (toolEntry == null) {
        return false;
      }

      if (!this.hasSufficientToolDurability(toolHandler, itemStack)) {
        return false;
      }

      int toolDamage = toolEntry.getDamage();

      return toolDamage > 0
          && toolHandler.applyDamage(world, itemStack, toolDamage, player, false);
    }

    return false;
  }

  protected void onCraftCheckAndReplaceTool(int toolIndex, IItemHandlerModifiable toolStackHandler, IItemHandler capability) {

    ItemStack itemStack = toolStackHandler.getStackInSlot(toolIndex);
    IToolHandler toolHandler = ArtisanToolHandlers.get(itemStack);

    if (!this.hasSufficientToolDurability(toolHandler, itemStack)) {
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
        ToolEntry toolEntry = this.findToolEntry(potentialToolHandler, potentialTool);

        if (toolEntry != null
            && toolEntry.matches(toolHandler, itemStack)
            && this.hasSufficientToolDurability(potentialToolHandler, potentialTool)) {
          // Found an acceptable tool
          potentialTool = capability.extractItem(i, 1, false);
          capability.insertItem(i, toolStackHandler.getStackInSlot(toolIndex), false);
          toolStackHandler.setStackInSlot(toolIndex, potentialTool);
        }
      }
    }
  }

  @Nonnull
  protected IArtisanItemStack generateExtraOutput(ICraftingContext context, IArtisanItemStack extraOutput) {

    World world = context.getWorld();
    IItemHandler secondaryOutputHandler = context.getSecondaryOutputHandler();
    ItemStack result = extraOutput.toItemStack();
    ItemStack insertResult = result.copy();

    for (int i = 0; i < 3; i++) {
      insertResult = secondaryOutputHandler.insertItem(i, insertResult, false);

      if (insertResult.isEmpty()) {
        break;
      }
    }

    if (!insertResult.isEmpty() && !world.isRemote) {
      BlockPos pos = context.getPosition();
      Util.spawnStackOnTop(world, insertResult, pos.add(0, 0, 0), 1.0);
    }

    return ArtisanItemStack.from(result);
  }

}
