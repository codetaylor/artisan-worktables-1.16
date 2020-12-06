package com.codetaylor.mc.artisanworktables.datagen;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
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
      this.generate(block);
    }
  }

  private void generate(Block block) {

    ResourceLocation registryName = block.getRegistryName();
    String path = Objects.requireNonNull(registryName).getPath();
    String[] split = path.split("_");
    String tableTier = split[0];
    String tableType = split[1];

    BlockModelBuilder blockModelBuilder = this.models()
        .withExistingParent(path, this.modLoc("block/" + tableTier))
        .texture("side", this.modLoc("block/" + tableType + "_side"))
        .texture("top", this.modLoc("block/" + tableType + "_top"))
        .texture("particle", this.modLoc("block/" + tableType + "_top"));

    this.itemModels().getBuilder(path).parent(blockModelBuilder);
    this.simpleBlock(block, blockModelBuilder);
  }
}
