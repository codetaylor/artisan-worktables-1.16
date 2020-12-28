package com.codetaylor.mc.artisanworktables.datagen;

import com.codetaylor.mc.artisanworktables.ArtisanWorktablesMod;
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
      String resourceLocationNamespace = Objects.requireNonNull(resourceLocation).getNamespace();
      String resourceLocationPath = resourceLocation.getPath();
      Path path = this.outputFolder.resolve("data/" + resourceLocationNamespace + "/loot_tables/blocks/" + resourceLocationPath + ".json");
      LootTable.Builder lootTable = this.createTable(resourceLocationPath, block);

      try {
        IDataProvider.save(this.gson, cache, LootTableManager.toJson(lootTable.build()), path);

      } catch (IOException e) {
        this.logger.error("Couldn't write loot table {}", path, e);
      }
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
