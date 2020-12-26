package com.codetaylor.mc.artisanworktables.datagen;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.block.MageBaseBlock;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class BlockStates
    extends BlockStateProvider {

  public BlockStates(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {

    super(dataGenerator, ArtisanWorktablesMod.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {

    for (Block block : ArtisanWorktablesMod.getProxy().getRegisteredWorktables()) {

      ResourceLocation registryName = block.getRegistryName();
      String path = Objects.requireNonNull(registryName).getPath();
      String[] split = path.split("_");
      String tableTier = split[0];
      String tableType = split[1];

      if (block instanceof MageBaseBlock) {
        this.generateMage(block, path, tableTier, tableType);

      } else {
        this.generate(block, path, tableTier, tableType);
      }
    }
  }

  private void generate(Block block, String path, String tableTier, String tableType) {

    BlockModelBuilder blockModelBuilder = this.getBlockModelBuilder(path, tableTier, tableType, "");

    this.simpleBlockItem(block, blockModelBuilder);
    this.simpleBlock(block, blockModelBuilder);
  }

  private void generateMage(Block block, String path, String tableTier, String tableType) {

    BlockModelBuilder blockModelBuilder = this.getBlockModelBuilder(path, tableTier, tableType, "");
    BlockModelBuilder blockModelBuilderActive = this.getBlockModelBuilder(path + "_active", tableTier, tableType, "_active");

    this.itemModels().getBuilder(path).parent(blockModelBuilder);

    this.getVariantBuilder(block).partialState().with(MageBaseBlock.ACTIVE, false).addModels(new ConfiguredModel(blockModelBuilder));
    this.getVariantBuilder(block).partialState().with(MageBaseBlock.ACTIVE, true).addModels(new ConfiguredModel(blockModelBuilderActive));
  }

  private BlockModelBuilder getBlockModelBuilder(String path, String tableTier, String tableType, String suffix) {

    return this.models()
        .withExistingParent(path, this.modLoc("block/" + tableTier))
        .texture("side", this.modLoc("block/" + tableType + "_side" + suffix))
        .texture("top", this.modLoc("block/" + tableType + "_top" + suffix))
        .texture("particle", this.modLoc("block/" + tableType + "_top" + suffix));
  }
}
