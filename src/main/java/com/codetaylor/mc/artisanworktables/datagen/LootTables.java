package com.codetaylor.mc.artisanworktables.datagen;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
import com.codetaylor.mc.artisanworktables.common.block.ToolboxBlock;
import com.codetaylor.mc.artisanworktables.common.block.ToolboxMechanicalBlock;
import com.google.gson.Gson;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class LootTables
    extends LootTableProvider {

  private final Path outputFolder;
  private final Gson gson;
  private final Logger logger;

  public LootTables(DataGenerator dataGenerator, Gson gson, Logger logger) {

    super(dataGenerator);

    this.outputFolder = dataGenerator.getOutputFolder();
    this.gson = gson;
    this.logger = logger;
  }

  @Override
  public void act(@Nonnull DirectoryCache cache) {

    for (Block block : ArtisanWorktablesMod.getProxy().getRegisteredWorktables()) {
      ResourceLocation resourceLocation = block.getRegistryName();
      String resourceLocationPath = Objects.requireNonNull(resourceLocation).getPath();
      this.createAndSaveTable(cache, block, resourceLocationPath);
    }

    this.createAndSaveTable(cache, ArtisanWorktablesMod.Blocks.TOOLBOX, ToolboxBlock.NAME);
    this.createAndSaveTable(cache, ArtisanWorktablesMod.Blocks.MECHANICAL_TOOLBOX, ToolboxMechanicalBlock.NAME);
  }

  private void createAndSaveTable(@Nonnull DirectoryCache cache, Block block, String resourceLocationPath) {

    LootTable.Builder lootTable = this.createTable(resourceLocationPath, block);
    Path path = this.outputFolder.resolve("data/" + ArtisanWorktablesMod.MOD_ID + "/loot_tables/blocks/" + resourceLocationPath + ".json");

    try {
      IDataProvider.save(this.gson, cache, LootTableManager.toJson(lootTable.build()), path);

    } catch (IOException e) {
      this.logger.error("Couldn't write loot table {}", path, e);
    }
  }

  private LootTable.Builder createTable(String name, Block block) {

    LootPool.Builder builder = LootPool.builder()
        .name(name)
        .acceptCondition(SurvivesExplosion.builder())
        .rolls(ConstantRange.of(1))
        .addEntry(ItemLootEntry.builder(block));
    return LootTable.builder().addLootPool(builder).setParameterSet(LootParameterSets.BLOCK);
  }
}
