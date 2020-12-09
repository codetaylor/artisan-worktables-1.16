package com.codetaylor.mc.artisanworktables.common.tile;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.api.ArtisanRegistries;
import com.codetaylor.mc.artisanworktables.api.EnumTier;
import com.codetaylor.mc.artisanworktables.api.EnumType;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ISecondaryIngredientMatcher;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.internal.util.EnchantmentHelper;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementContext;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementContextSupplier;
import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import com.codetaylor.mc.artisanworktables.common.recipe.CraftingContextFactory;
import com.codetaylor.mc.artisanworktables.common.recipe.VanillaRecipeCache;
import com.codetaylor.mc.artisanworktables.common.tile.handler.*;
import com.codetaylor.mc.athenaeum.inventory.spi.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.ITileData;
import com.codetaylor.mc.athenaeum.network.spi.tile.TileEntityDataBase;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.TileDataFluidTank;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.spi.tile.data.service.ITileDataService;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Predicate;

public abstract class BaseTileEntity
    extends TileEntityDataBase
    implements ITickableTileEntity,
    IContainerProvider {

  private String uuid;
  private EnumType type;
  private ToolStackHandler toolHandler;
  private CraftingMatrixStackHandler craftingMatrixHandler;
  private CraftingMatrixStackHandler craftingMatrixHandlerGhost;
  private IItemHandler roundRobinGhostStackHandler;
  private SecondaryOutputStackHandler secondaryOutputHandler;
  private ResultStackHandler resultHandler;
  private TankHandler tank;
  private boolean creative; // TODO: some of these need to be networked
  private boolean locked;
  private boolean initialized;

  private VanillaRecipeCache.InventoryWrapper inventoryWrapper;
  private final List<BaseContainer> containerList = new ArrayList<>();

  protected boolean requiresRecipeUpdate;

  private final LazyOptional<IItemHandler> itemCapability = LazyOptional.of(() -> this.roundRobinGhostStackHandler);
  private final LazyOptional<FluidTank> fluidCapability = LazyOptional.of(() -> this.tank);

  // used client-side for storing creative table data
  public Int2ObjectMap<String> tagMap = new Int2ObjectOpenHashMap<>();
  private boolean tagLinked = true;

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

    this.craftingMatrixHandlerGhost = new CraftingMatrixStackHandler(
        this.craftingMatrixHandler.getWidth(),
        this.craftingMatrixHandler.getHeight()
    );

    this.roundRobinGhostStackHandler = new RoundRobinGhostStackHandler(
        this,
        this.craftingMatrixHandler,
        this.craftingMatrixHandlerGhost
    );

    // Observers

    {
      ObservableStackHandler.IContentsChangedEventHandler contentsChangedEventHandler;
      contentsChangedEventHandler = (stackHandler, slotIndex) -> {
        this.markDirty();
        this.requiresRecipeUpdate = true;

        if (this.isLocked()) {
          ItemStack stackInSlot = stackHandler.getStackInSlot(slotIndex);

          if (!stackInSlot.isEmpty()) {
            ItemStack copy = stackInSlot.copy();
            copy.setCount(1);
            this.craftingMatrixHandlerGhost.setStackInSlot(slotIndex, copy);
          }
        }
      };
      this.craftingMatrixHandler.addObserver(contentsChangedEventHandler);
      this.toolHandler.addObserver(contentsChangedEventHandler);
      this.secondaryOutputHandler.addObserver(contentsChangedEventHandler);
    }

    {
      this.tank.addObserver((fluidTank, amount) -> {
        this.requiresRecipeUpdate = true;
        this.markDirty();
      });
    }

    {
      this.craftingMatrixHandlerGhost.addObserver((stackHandler, slotIndex) -> this.markDirty());
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

  public EnumType getTableType() {

    return this.type;
  }

  protected void setTableType(EnumType type) {

    this.type = type;
  }

  public ICraftingMatrixStackHandler getCraftingMatrixHandler() {

    return this.craftingMatrixHandler;
  }

  public ICraftingMatrixStackHandler getCraftingMatrixHandlerGhost() {

    return this.craftingMatrixHandlerGhost;
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

  public boolean isLocked() {

    return this.locked;
  }

  public void setLocked(boolean locked) {

    this.locked = locked;

    if (locked) {

      for (int i = 0; i < this.craftingMatrixHandler.getSlots(); i++) {
        ItemStack copy = this.craftingMatrixHandler.getStackInSlot(i).copy();

        if (copy.isEmpty()) {
          this.craftingMatrixHandlerGhost.setStackInSlot(i, ItemStack.EMPTY);

        } else {
          copy.setCount(1);
          this.craftingMatrixHandlerGhost.setStackInSlot(i, copy);
        }
      }

    } else {

      for (int i = 0; i < this.craftingMatrixHandlerGhost.getSlots(); i++) {
        this.craftingMatrixHandlerGhost.setStackInSlot(i, ItemStack.EMPTY);
      }
    }
  }

  public boolean isCreative() {

    return this.creative;
  }

  public void addContainer(BaseContainer container) {

    this.containerList.add(container);
  }

  public void removeContainer(BaseContainer container) {

    this.containerList.remove(container);
  }

  public boolean isTagLinked() {

    return this.tagLinked;
  }

  public void setTagLinked(boolean linked) {

    this.tagLinked = linked;
  }

  public void setCreative(boolean creative) {

    this.creative = creative;
  }

  public ICraftingContext getCraftingContext(PlayerEntity player) {

    return CraftingContextFactory.createContext(this, player, null);
  }

  public void setRequiresRecipeUpdate() {

    this.requiresRecipeUpdate = true;
  }

  public VanillaRecipeCache.InventoryWrapper getInventoryWrapper() {

    if (this.inventoryWrapper == null) {
      this.inventoryWrapper = new VanillaRecipeCache.InventoryWrapper(this);
    }

    return this.inventoryWrapper;
  }

  public ISecondaryIngredientMatcher getSecondaryIngredientMatcher() {

    return ISecondaryIngredientMatcher.FALSE;
  }

  public ItemStack[] getTools() {

    int slotCount = this.toolHandler.getSlots();
    List<ItemStack> tools = new ArrayList<>(slotCount);

    for (int i = 0; i < slotCount; i++) {
      ItemStack stackInSlot = this.toolHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        tools.add(stackInSlot);
      }
    }

    return tools.toArray(new ItemStack[0]);
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

  /**
   * Searches cardinal directions around all joined tables and returns an adjacent toolbox.
   * <p>
   * If more than one toolbox is found, the first toolbox found is returned.
   * <p>
   * If no toolbox is found, null is returned.
   *
   * @return adjacent toolbox or null
   */
  // TODO
  /*@Nullable
  public TileEntityToolbox getAdjacentToolbox() {

    List<TileEntityBase> joinedTables = this.getJoinedTables(new ArrayList<>());

    for (TileEntityBase joinedTable : joinedTables) {
      BlockPos pos = joinedTable.getPos();
      TileEntity tileEntity;

      for (EnumFacing facing : EnumFacing.HORIZONTALS) {

        if ((tileEntity = this.world.getTileEntity(pos.offset(facing))) != null) {

          if (tileEntity instanceof TileEntityToolbox) {

            return (TileEntityToolbox) tileEntity;
          }
        }
      }
    }

    return null;
  }*/
  @Nullable
  public ITileEntityDesigner getAdjacentDesignersTable() {

    if (this.world != null) {
      List<BaseTileEntity> joinedTables = this.getJoinedTables(new ArrayList<>());

      for (BaseTileEntity joinedTable : joinedTables) {
        BlockPos pos = joinedTable.getPos();
        TileEntity tileEntity;

        if ((tileEntity = this.world.getTileEntity(pos)) != null) {

          if (tileEntity instanceof ITileEntityDesigner) {
            return (ITileEntityDesigner) tileEntity;
          }
        }
      }
    }

    return null;
  }

  public RecipeRegistry getWorktableRecipeRegistry() {

    String name = this.getTableType().getName();
    ResourceLocation resourceLocation = new ResourceLocation(ArtisanWorktablesMod.MOD_ID, name);
    return ArtisanRegistries.RECIPE_REGISTRY.get(resourceLocation);
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

    if (this.isCreative()) {
      return;
    }

    IArtisanRecipe recipe = this.getRecipe(player);

    if (recipe == null) {
      return;
    }

    recipe.doCraft(this.getCraftingContext(player), null);

    this.markDirty();
  }

  // ---------------------------------------------------------------------------
  // Recipe
  // ---------------------------------------------------------------------------

  @Nullable
  private IArtisanRecipe getVanillaCraftingRecipe() {

    VanillaRecipeCache.InventoryWrapper inventoryWrapper = this.getInventoryWrapper();
    return VanillaRecipeCache.getArtisanRecipe(inventoryWrapper, this.world);
  }

  @Nullable
  public IArtisanRecipe getRecipe(@Nonnull PlayerEntity player) {

    if (this.craftingMatrixHandler.isEmpty()) {
      // If the crafting grid is empty, we don't even try matching a recipe.
      return null;
    }

    FluidStack fluidStack = this.getTank().getFluid();

    if (fluidStack != null) {
      fluidStack = fluidStack.copy();
    }

    int playerExperience = EnchantmentHelper.getPlayerExperienceTotal(player);
    int playerLevels = player.experienceLevel;
    boolean isPlayerCreative = player.isCreative();

    Map<ResourceLocation, RequirementContextSupplier> contextSupplierRegistry = ArtisanRegistries.REQUIREMENT_CONTEXT_SUPPLIER_REGISTRY;
    Map<ResourceLocation, IRequirementContext> contextMap = new HashMap<>();
    ICraftingContext craftingContext = this.getCraftingContext(player);

    for (Map.Entry<ResourceLocation, RequirementContextSupplier> entry : contextSupplierRegistry.entrySet()) {
      RequirementContextSupplier contextSupplier = entry.getValue();
      IRequirementContext context = contextSupplier.get();
      context.initialize(craftingContext);
      contextMap.put(entry.getKey(), context);
    }

    IArtisanRecipe customRecipe = this.getWorktableRecipeRegistry().findRecipe(
        playerExperience,
        playerLevels,
        isPlayerCreative,
        this.getTools(),
        this.craftingMatrixHandler,
        fluidStack,
        this.getSecondaryIngredientMatcher(),
        this.getTableTier(),
        contextMap
    );

    if (customRecipe != null) {
      return customRecipe;
    }

    // TODO
    //boolean isVanillaCraftingEnabled = ModuleWorktablesConfig.isVanillaCraftingEnabledFor(this.getType(), this.getTableTier());
    boolean isVanillaCraftingEnabled = true;
    return isVanillaCraftingEnabled ? this.getVanillaCraftingRecipe() : null;
  }

  // ---------------------------------------------------------------------------
  // Update
  // ---------------------------------------------------------------------------

  @Override
  public void tick() {

    if (this.isCreative() ||
        (this.world != null && this.world.isRemote && ArtisanWorktablesMod.getProxy().isIntegratedServerRunning())) {
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

    // The given tier will be null if we're checking for a vanilla recipe.

    if ("vanilla".equals(name)) {
      // Check config if allows vanilla crafting.
      // TODO
      //return ModuleWorktablesConfig.isVanillaCraftingEnabledFor(this.getType(), this.getTableTier());
      return true;
    }

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

    } else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != Direction.DOWN) {
      return this.itemCapability.cast();
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
    this.craftingMatrixHandlerGhost.deserializeNBT(nbt.getCompound("craftingMatrixHandlerGhost"));
    this.toolHandler.deserializeNBT(nbt.getCompound("toolHandler"));
    this.secondaryOutputHandler.deserializeNBT(nbt.getCompound("secondaryOutputHandler"));
    this.tank.readFromNBT(nbt.getCompound("tank"));
    this.creative = nbt.getBoolean("creative");
    this.resultHandler.deserializeNBT(nbt.getCompound("resultHandler"));
    this.locked = nbt.getBoolean("locked");
  }

  @Nonnull
  @ParametersAreNonnullByDefault
  @Override
  public CompoundNBT write(CompoundNBT nbt) {

    super.write(nbt);
    nbt.putString("type", this.type.getName());
    nbt.put("craftingMatrixHandler", this.craftingMatrixHandler.serializeNBT());
    nbt.put("craftingMatrixHandlerGhost", this.craftingMatrixHandlerGhost.serializeNBT());
    nbt.put("toolHandler", this.toolHandler.serializeNBT());
    nbt.put("secondaryOutputHandler", this.secondaryOutputHandler.serializeNBT());
    nbt.put("tank", this.tank.writeToNBT(new CompoundNBT()));
    nbt.putBoolean("creative", this.creative);
    nbt.put("resultHandler", this.resultHandler.serializeNBT());
    nbt.putBoolean("locked", this.locked);
    return nbt;
  }
}
