package com.codetaylor.mc.artisanworktables.common.plugin.jei;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.block.BaseBlock;
import com.codetaylor.mc.artisanworktables.common.container.BaseContainer;
import com.codetaylor.mc.artisanworktables.common.container.WorkshopContainer;
import com.codetaylor.mc.artisanworktables.common.container.WorkstationContainer;
import com.codetaylor.mc.artisanworktables.common.container.WorktableContainer;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.RecipeTypes;
import com.codetaylor.mc.artisanworktables.common.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.common.reference.EnumType;
import com.codetaylor.mc.artisanworktables.common.util.Key;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.*;

@JeiPlugin
public class Plugin
    implements IModPlugin {

  public static final ResourceLocation RECIPE_BACKGROUND = Key.from("textures/gui/recipe_background.png");

  public static final Map<EnumTier, Map<EnumType, ResourceLocation>> CATEGORY_KEYS;

  static {
    CATEGORY_KEYS = new EnumMap<>(EnumTier.class);
    for (EnumTier tier : EnumTier.values()) {
      for (EnumType type : EnumType.values()) {
        Map<EnumType, ResourceLocation> map = CATEGORY_KEYS.computeIfAbsent(tier, t -> new EnumMap<>(EnumType.class));
        map.put(type, Key.from(String.format("%s_%s", tier.getName(), type.getName())));
      }
    }
  }

  @Nonnull
  @Override
  public ResourceLocation getPluginUid() {

    return Key.from("jei_plugin");
  }

  @Override
  public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {

    IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    ICraftingGridHelper craftingGridHelper = guiHelper.createCraftingGridHelper(1);
    WorkshopCraftingGridHelper workshopCraftingGridHelper = new WorkshopCraftingGridHelper(1);
    CategoryFactory categoryFactory = new CategoryFactory();
    List<Block> registeredWorktables = ArtisanWorktablesMod.getProxy().getRegisteredWorktables();
    List<IRecipeCategory<?>> recipeCategoryList = new ArrayList<>(registeredWorktables.size());

    Map<EnumTier, CategoryDrawHandler> categoryDrawHandlerMap = new EnumMap<>(EnumTier.class);
    Map<EnumTier, CategorySetupHandler> categorySetupHandlerMap = new EnumMap<>(EnumTier.class);

    for (EnumTier tier : EnumTier.values()) {
      categoryDrawHandlerMap.put(tier, new CategoryDrawHandler(tier));
      categorySetupHandlerMap.put(tier, new CategorySetupHandler(tier, tier == EnumTier.WORKSHOP ? workshopCraftingGridHelper : craftingGridHelper));
    }

    for (Block block : registeredWorktables) {
      ResourceLocation registryName = block.getRegistryName();
      String path = Objects.requireNonNull(registryName).getPath();
      String[] split = path.split("_");
      EnumTier tier = EnumTier.fromName(split[0]);
      EnumType type = EnumType.fromName(split[1]);
      CategorySetupHandler categorySetupHandler = categorySetupHandlerMap.get(tier);
      CategoryDrawHandler categoryDrawHandler = categoryDrawHandlerMap.get(tier);

      BaseCategory<?> category = categoryFactory.create(tier, type, block, guiHelper, categorySetupHandler, categoryDrawHandler);
      recipeCategoryList.add(category);
    }

    registry.addRecipeCategories(recipeCategoryList.toArray(new IRecipeCategory[0]));
  }

  @Override
  public void registerRecipes(@Nonnull IRecipeRegistration registry) {

    RecipeManager recipeManager = ArtisanWorktablesMod.getProxy().getRecipeManager();

    if (recipeManager == null) {
      throw new RuntimeException("Null recipe manager");
    }

    for (EnumTier tier : EnumTier.values()) {
      for (EnumType type : EnumType.values()) {
        List<ArtisanRecipe> recipes = this.getRecipes(recipeManager, tier, type);
        registry.addRecipes(recipes, CATEGORY_KEYS.get(tier).get(type));
      }
    }
  }

  @Override
  public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registry) {

    List<Block> registeredWorktables = ArtisanWorktablesMod.getProxy().getRegisteredWorktables();

    for (Block block : registeredWorktables) {
      BaseBlock baseBlock = (BaseBlock) block;
      EnumType type = baseBlock.getType();
      EnumTier tier = EnumTier.fromName(Objects.requireNonNull(baseBlock.getRegistryName()).getPath().split("_")[0]);

      registry.addRecipeCatalyst(new ItemStack(block), CATEGORY_KEYS.get(tier).get(type));
    }
  }

  @Override
  public void registerRecipeTransferHandlers(@Nonnull IRecipeTransferRegistration registry) {

    for (EnumTier tier : EnumTier.values()) {

      Class<? extends BaseContainer> containerClass;

      switch (tier) {
        case WORKTABLE:
          containerClass = WorktableContainer.class;
          break;
        case WORKSTATION:
          containerClass = WorkstationContainer.class;
          break;
        case WORKSHOP:
          containerClass = WorkshopContainer.class;
          break;
        default:
          throw new RuntimeException("Unknown tier: " + tier);
      }

      for (EnumType type : EnumType.values()) {
        registry.addRecipeTransferHandler(new RecipeTransferInfo<>(containerClass, tier, type, CATEGORY_KEYS.get(tier).get(type)));
      }
    }
  }

  private List<ArtisanRecipe> getRecipes(RecipeManager recipeManager, EnumTier tier, EnumType type) {

    List<ArtisanRecipe> result = new ArrayList<>();

    {
      List<ArtisanRecipe> recipesForType = recipeManager.getRecipesForType(RecipeTypes.SHAPED_RECIPE_TYPES.get(type));

      for (ArtisanRecipe recipe : recipesForType) {
        if (recipe.matchTier(tier)) {
          result.add(recipe);
        }
      }
    }

    {
      List<ArtisanRecipe> recipesForType = recipeManager.getRecipesForType(RecipeTypes.SHAPELESS_RECIPE_TYPES.get(type));

      for (ArtisanRecipe recipe : recipesForType) {
        if (recipe.matchTier(tier)) {
          result.add(recipe);
        }
      }
    }

    result.sort(Comparator.comparing(recipe -> recipe.getId().toString()));

    return result;
  }
}
