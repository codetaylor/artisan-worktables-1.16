package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.api.IToolHandler;
import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import com.codetaylor.mc.artisanworktables.common.recipe.*;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.tile.handler.*;
import com.codetaylor.mc.artisanworktables.common.util.EnchantmentHelper;
import com.codetaylor.mc.athenaeum.inventory.spi.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.ITileData;
import com.codetaylor.mc.athenaeum.network.spi.tile.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.ITileDataService;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Predicate;

public abstract class BaseTileEntity
    extends TileEntityDataBase
    implements ITickableTileEntity {

  private static final IItemHandlerModifiable SECONDARY_INGREDIENT_HANDLER_DEFAULT = new ItemStackHandler(0);

  private String uuid;
  private EnumType type;
  private ToolStackHandler toolHandler;
  private CraftingMatrixStackHandler craftingMatrixHandler;
  private SecondaryOutputStackHandler secondaryOutputHandler;
  private ResultStackHandler resultHandler;
  private TankHandler tank;
  private boolean initialized;

  private MageToolObserver mageToolObserver;

  private final List<BaseContainer> containerList = new ArrayList<>();
  private final CraftHandler craftHandler = new CraftHandler();

  protected boolean requiresRecipeUpdate;

  private final LazyOptional<FluidTank> fluidCapability = LazyOptional.of(() -> this.tank);

  // ---------------------------------------------------------------------------
  // Initialization
  // ---------------------------------------------------------------------------

  protected BaseTileEntity(TileEntityType<?> tileEntityType, ITileDataService tileDataService) {

    // serialization
    super(tileEntityType, tileDataService);
  }

  protected BaseTileEntity(TileEntityType<?> tileEntityType, ITileDataService tileDataService, EnumType type) {

    super(tileEntityType, tileDataService);
    this.type = type;
    this.initializeInternal(type);
  }

  private void initializeInternal(EnumType type) {

    if (!this.initialized) {
      this.initialize(type);
      this.initialized = true;
    }
  }

  protected void initialize(EnumType type) {

    // Initialization

    this.uuid = type.getName() + "." + this.getTableTier().getName();
    this.craftingMatrixHandler = new CraftingMatrixStackHandler(this.getCraftingMatrixWidth(), this.getCraftingMatrixHeight());
    this.toolHandler = new ToolStackHandler(this.getMaxToolCount());
    this.secondaryOutputHandler = new SecondaryOutputStackHandler(3);
    this.resultHandler = new ResultStackHandler(1);
    this.tank = new TankHandler(this.getFluidTankCapacity());

    // Observers

    {
      ObservableStackHandler.IContentsChangedEventHandler contentsChangedEventHandler;
      contentsChangedEventHandler = (stackHandler, slotIndex) -> {
        this.markDirty();
        this.requiresRecipeUpdate = true;
      };
      this.craftingMatrixHandler.addObserver(contentsChangedEventHandler);
      this.toolHandler.addObserver(contentsChangedEventHandler);
      this.secondaryOutputHandler.addObserver(contentsChangedEventHandler);
    }

    // Mage table observer
    {
      this.mageToolObserver = new MageToolObserver(this);
      this.toolHandler.addObserver(this.mageToolObserver);
    }

    {
      this.tank.addObserver((fluidTank, amount) -> {
        this.requiresRecipeUpdate = true;
        this.markDirty();
      });
    }

    // Network

    this.registerTileDataForNetwork(new ITileData[]{
        new TileDataItemStackHandler<>(this.craftingMatrixHandler),
        new TileDataItemStackHandler<>(this.toolHandler),
        new TileDataItemStackHandler<>(this.secondaryOutputHandler),
        new TileDataItemStackHandler<>(this.resultHandler),
        new TileDataFluidTank<>(this.tank)
    });
  }

  // ---------------------------------------------------------------------------
  // Accessors
  // ---------------------------------------------------------------------------

  public boolean allowTabs() {

    return true;
  }

  public EnumType getTableType() {

    return this.type;
  }

  protected void setTableType(EnumType type) {

    this.type = type;
  }

  public ICraftingMatrixStackHandler getCraftingMatrixHandler() {

    return this.craftingMatrixHandler;
  }

  public ItemStackHandler getToolHandler() {

    return this.toolHandler;
  }

  public ItemStackHandler getSecondaryOutputHandler() {

    return this.secondaryOutputHandler;
  }

  public FluidTank getTank() {

    return this.tank;
  }

  public ItemStackHandler getResultHandler() {

    return this.resultHandler;
  }

  public void addContainer(BaseContainer container) {

    this.containerList.add(container);
  }

  public void removeContainer(BaseContainer container) {

    this.containerList.remove(container);
  }

  public ICraftingContext getCraftingContext(PlayerEntity player) {

    return CraftingContextFactory.createContext(this, player, null);
  }

  public void setRequiresRecipeUpdate() {

    this.requiresRecipeUpdate = true;
  }

  public ISecondaryIngredientMatcher getSecondaryIngredientMatcher() {

    return ISecondaryIngredientMatcher.FALSE;
  }

  public IItemHandlerModifiable getSecondaryIngredientHandler() {

    return SECONDARY_INGREDIENT_HANDLER_DEFAULT;
  }

  public ItemStack[] getTools() {

    switch (this.toolHandler.getSlots()) {
      case 1:
        return new ItemStack[]{
            this.toolHandler.getStackInSlot(0)
        };
      case 2:
        return new ItemStack[]{
            this.toolHandler.getStackInSlot(0),
            this.toolHandler.getStackInSlot(1)
        };
      case 3:
        return new ItemStack[]{
            this.toolHandler.getStackInSlot(0),
            this.toolHandler.getStackInSlot(1),
            this.toolHandler.getStackInSlot(2)
        };
      default:
        throw new IllegalStateException("Tool handler should have between 1 and 3 slots!");
    }
  }

  public boolean hasTool() {

    ItemStack[] tools = this.getTools();
    boolean hasTool = false;

    for (ItemStack tool : tools) {

      if (!tool.isEmpty()) {
        hasTool = true;
        break;
      }
    }

    return hasTool;
  }

  public List<BaseTileEntity> getJoinedTables(List<BaseTileEntity> result) {

    return this.getJoinedTables(result, null, tileEntityBase -> true);
  }

  /**
   * Uses a flood fill to find all tables adjacent to this one. An empty list is returned
   * if any of the tables found are of the same type and tier. If a player is provided,
   * any tables outside of the player's reach will not be returned in the list.
   *
   * @param result a list to store the result
   * @param player the player, can be null
   * @return result list
   */
  public List<BaseTileEntity> getJoinedTables(List<BaseTileEntity> result, @Nullable PlayerEntity player, Predicate<BaseTileEntity> filter) {

    if (this.world == null) {
      return Collections.emptyList();
    }

    Map<String, BaseTileEntity> joinedTableMap = new TreeMap<>();
    joinedTableMap.put(this.uuid, this);

    Set<BlockPos> searchedPositionSet = new HashSet<>();
    searchedPositionSet.add(this.pos);

    Queue<BlockPos> toSearchQueue = new ArrayDeque<>();
    toSearchQueue.offer(this.pos.offset(Direction.NORTH));
    toSearchQueue.offer(this.pos.offset(Direction.EAST));
    toSearchQueue.offer(this.pos.offset(Direction.SOUTH));
    toSearchQueue.offer(this.pos.offset(Direction.WEST));

    BlockPos searchPosition;

    while ((searchPosition = toSearchQueue.poll()) != null) {

      if (searchedPositionSet.contains(searchPosition)) {
        // we've already looked here, skip
        continue;
      }

      // record that we've looked here
      searchedPositionSet.add(searchPosition);

      TileEntity tileEntity = this.world.getTileEntity(searchPosition);

      if (tileEntity instanceof BaseTileEntity) {
        BaseTileEntity tileEntityBase = (BaseTileEntity) tileEntity;
        String key = (tileEntityBase).uuid;

        if (joinedTableMap.containsKey(key)) {
          // this indicates two tables of the same type joined in the pseudo-multiblock
          // and we need to invalidate the structure by returning nothing
          return Collections.emptyList();
        }

        // found a table!
        if (filter.test(tileEntityBase) && (player == null || (tileEntityBase).canPlayerUse(player))) {
          joinedTableMap.put(key, tileEntityBase);
        }

        // check around this newly discovered table
        toSearchQueue.offer(tileEntity.getPos().offset(Direction.NORTH));
        toSearchQueue.offer(tileEntity.getPos().offset(Direction.EAST));
        toSearchQueue.offer(tileEntity.getPos().offset(Direction.SOUTH));
        toSearchQueue.offer(tileEntity.getPos().offset(Direction.WEST));
      }
    }

    result.addAll(joinedTableMap.values());
    //return result.size() < 2 ? Collections.emptyList() : result;
    return result;
  }

  public boolean canPlayerUse(PlayerEntity player) {

    return this.world != null
        && this.world.getTileEntity(this.getPos()) == this
        && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
  }

  public ItemStack getItemStackForTabDisplay(BlockState state) {

    Block block = state.getBlock();
    Item item = Item.getItemFromBlock(block);
    return new ItemStack(item, 1);
  }

  // ---------------------------------------------------------------------------
  // Subclass
  // ---------------------------------------------------------------------------

  public abstract EnumTier getTableTier();

  protected abstract int getMaxToolCount();

  protected abstract int getFluidTankCapacity();

  protected abstract int getCraftingMatrixWidth();

  protected abstract int getCraftingMatrixHeight();

  // ---------------------------------------------------------------------------
  // Actions
  // ---------------------------------------------------------------------------

  public void onJoinedBlockBreak(BlockPos pos) {

    for (BaseContainer container : this.containerList) {
      container.onJoinedBlockBreak(this.world, pos);
    }
  }

  public void triggerContainerRecipeUpdate() {

    for (BaseContainer container : this.containerList) {
      container.updateRecipeOutput();
    }
  }

  public void onTakeResult(PlayerEntity player) {

    ArtisanRecipe recipe = this.getRecipe(player);

    if (recipe == null || this.world == null) {
      return;
    }

    this.craftHandler.doCraft(this.world, this.pos, player, recipe, this.getInventory(player), null);

    this.markDirty();
  }

  public void notifyCraftComplete() {

    this.mageToolObserver.checkToolState(this.toolHandler);
  }

  // ---------------------------------------------------------------------------
  // Recipe
  // ---------------------------------------------------------------------------

  @Nullable
  public ArtisanRecipe getRecipe(@Nonnull PlayerEntity player) {

    if (this.world == null) {
      return null;
    }

    if (this.craftingMatrixHandler.isEmpty()) {
      // If the crafting grid is empty, we don't even try matching a recipe.
      return null;
    }

    RecipeManager recipeManager = this.world.getRecipeManager();
    ArtisanInventory inventory = this.getInventory(player);

    {
      IRecipeType<ArtisanRecipe> recipeType = RecipeTypes.SHAPED_RECIPE_TYPES.get(this.type);
      Optional<ArtisanRecipe> recipe = recipeManager.getRecipe(recipeType, inventory, this.world);

      if (recipe.isPresent()) {
        return recipe.get();
      }
    }

    {
      IRecipeType<ArtisanRecipe> recipeType = RecipeTypes.SHAPELESS_RECIPE_TYPES.get(this.type);
      Optional<ArtisanRecipe> recipe = recipeManager.getRecipe(recipeType, inventory, this.world);

      if (recipe.isPresent()) {
        return recipe.get();
      }
    }

    return null;
  }

  private ArtisanInventory getInventory(@Nonnull PlayerEntity player) {

    ItemStack[] tools = this.getTools();

    return new ArtisanInventory(
        this.getTableTier(),
        this.getPlayerData(player),
        this.getCraftingMatrixHandler(),
        this.getTank(),
        tools,
        this.getToolHandlers(tools),
        this.getToolHandler(),
        this.getSecondaryIngredientMatcher(),
        this.getSecondaryIngredientHandler(),
        this.getSecondaryOutputHandler(),
        this.getCraftingMatrixWidth(),
        this.getCraftingMatrixHeight()
    );
  }

  private ArtisanInventory.PlayerData getPlayerData(PlayerEntity player) {

    int playerExperience = EnchantmentHelper.getPlayerExperienceTotal(player);
    int playerLevels = player.experienceLevel;
    boolean isPlayerCreative = player.isCreative();

    return new ArtisanInventory.PlayerData(isPlayerCreative, playerExperience, playerLevels);
  }

  private IToolHandler[] getToolHandlers(ItemStack[] tools) {

    IToolHandler[] handlers = new IToolHandler[tools.length];

    for (int i = 0; i < tools.length; i++) {
      handlers[i] = ArtisanToolHandlers.get(tools[i]);
    }

    return handlers;
  }

  // ---------------------------------------------------------------------------
  // Update
  // ---------------------------------------------------------------------------

  @Override
  public void tick() {

    if (this.world != null
        && this.world.isRemote
        && ArtisanWorktablesMod.getProxy().isIntegratedServerRunning()) {
      this.requiresRecipeUpdate = false;
    }

    if (this.requiresRecipeUpdate) {
      this.triggerContainerRecipeUpdate();
      this.requiresRecipeUpdate = false;
    }
  }

  // ---------------------------------------------------------------------------
  // Integration
  // ---------------------------------------------------------------------------

  public boolean canHandleRecipeTransferJEI(String name, @Nullable EnumTier tier) {

    return this.type.getName().equals(name) && tier != null && tier.getId() <= this.getTableTier().getId();
  }

  // ---------------------------------------------------------------------------
  // GUI
  // ---------------------------------------------------------------------------

  public int getWorktableGuiTabTextureYOffset() {

    switch (this.type) {
      case TAILOR:
        return 1;
      case CARPENTER:
        return 2;
      case MASON:
        return 4;
      case BLACKSMITH:
        return 5;
      case JEWELER:
        return 3;
      case BASIC:
        return 0;
      case ENGINEER:
        return 6;
      case MAGE:
        return 7;
      case SCRIBE:
        return 8;
      case CHEMIST:
        return 9;
      case FARMER:
        return 10;
      case CHEF:
        return 11;
      case DESIGNER:
        return 12;
      case TANNER:
        return 13;
      case POTTER:
        return 14;
      default:
        throw new RuntimeException("Unknown table type: " + this.type);
    }
  }

  // ---------------------------------------------------------------------------
  // Capability
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return this.fluidCapability.cast();
    }

    return super.getCapability(capability, side);
  }

  // ---------------------------------------------------------------------------
  // Serialization
  // ---------------------------------------------------------------------------

  @ParametersAreNonnullByDefault
  @Override
  public void read(BlockState state, CompoundNBT nbt) {

    super.read(state, nbt);
    this.type = EnumType.fromName(nbt.getString("type"));
    this.initializeInternal(this.type);
    this.craftingMatrixHandler.deserializeNBT(nbt.getCompound("craftingMatrixHandler"));
    this.toolHandler.deserializeNBT(nbt.getCompound("toolHandler"));
    this.secondaryOutputHandler.deserializeNBT(nbt.getCompound("secondaryOutputHandler"));
    this.tank.readFromNBT(nbt.getCompound("tank"));
    this.resultHandler.deserializeNBT(nbt.getCompound("resultHandler"));
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public CompoundNBT write(CompoundNBT nbt) {

    super.write(nbt);
    nbt.putString("type", this.type.getName());
    nbt.put("craftingMatrixHandler", this.craftingMatrixHandler.serializeNBT());
    nbt.put("toolHandler", this.toolHandler.serializeNBT());
    nbt.put("secondaryOutputHandler", this.secondaryOutputHandler.serializeNBT());
    nbt.put("tank", this.tank.writeToNBT(new CompoundNBT()));
    nbt.put("resultHandler", this.resultHandler.serializeNBT());
    return nbt;
  }
}
