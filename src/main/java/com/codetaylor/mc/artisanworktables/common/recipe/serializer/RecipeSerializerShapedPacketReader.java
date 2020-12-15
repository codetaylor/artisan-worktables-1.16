package com.codetaylor.mc.artisanworktables.common.recipe.serializer;

import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.common.recipe.ArtisanRecipeShaped;
import com.codetaylor.mc.artisanworktables.common.recipe.ToolEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeSerializerShapedPacketReader
    implements IRecipeSerializerPacketReader<ArtisanRecipeShaped> {

  @Nullable
  @Override
  public ArtisanRecipeShaped read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {

    // Common between shaped and shapeless
    String group = buffer.readString();
    ToolEntry[] tools = this.readTools(buffer);
    ItemStack result = buffer.readItemStack();
    NonNullList<Ingredient> ingredients = this.readIngredientList(buffer);
    NonNullList<Ingredient> secondaryIngredients = this.readIngredientList(buffer);
    boolean consumeSecondaryIngredients = buffer.readBoolean();
    FluidStack fluidIngredient = FluidStack.readFromPacket(buffer);
    ArtisanRecipe.ExtraOutputChancePair[] extraOutputs = this.readExtraOutputs(buffer);
    int minimumTier = buffer.readInt();
    int maximumTier = buffer.readInt();
    int experienceRequired = buffer.readInt();
    int levelRequired = buffer.readInt();
    boolean consumeExperience = buffer.readBoolean();

    // Shaped only
    boolean mirrored = buffer.readBoolean();
    int width = buffer.readInt();
    int height = buffer.readInt();

    return new ArtisanRecipeShaped(
        recipeId,
        group,
        tools,
        result,
        ingredients,
        secondaryIngredients,
        consumeSecondaryIngredients,
        fluidIngredient,
        extraOutputs,
        mirrored,
        minimumTier,
        maximumTier,
        experienceRequired,
        levelRequired,
        consumeExperience,
        width,
        height
    );
  }

  private ArtisanRecipe.ExtraOutputChancePair[] readExtraOutputs(PacketBuffer buffer) {

    int size = buffer.readInt();
    ArtisanRecipe.ExtraOutputChancePair[] result = new ArtisanRecipe.ExtraOutputChancePair[size];

    for (int i = 0; i < size; i++) {
      ItemStack itemStack = buffer.readItemStack();
      float chance = buffer.readFloat();
      result[i] = new ArtisanRecipe.ExtraOutputChancePair(itemStack, chance);
    }

    return result;
  }

  private NonNullList<Ingredient> readIngredientList(PacketBuffer buffer) {

    int size = buffer.readInt();
    NonNullList<Ingredient> result = NonNullList.create();

    for (int i = 0; i < size; i++) {
      Ingredient ingredient = Ingredient.read(buffer);
      result.add(ingredient);
    }

    return result;
  }

  private ToolEntry[] readTools(PacketBuffer buffer) {

    int size = buffer.readInt();
    ToolEntry[] result = new ToolEntry[size];

    for (int i = 0; i < size; i++) {
      Ingredient ingredient = Ingredient.read(buffer);
      int damage = buffer.readInt();
      result[i] = new ToolEntry(ingredient, damage);
    }

    return result;
  }
}
