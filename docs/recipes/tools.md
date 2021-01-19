# Recipes with Tools

Artisan Worktables supports recipes that require up to three different tools to craft. Any item that has durability will work as a tool.

Adding tool requirements to a recipe is optional. Recipes will work just fine if you don't add a tool.

## Add Tools to Recipes

To add a tool to a recipe, call the following method on the builder:

```java
Recipe tool(IIngredient tool, int damage);
```

The `damage` parameter in this method represents the amount of damage applied to a tool during the craft operation.

Here is an example:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<item:minecraft:dirt>])
  .output(<item:minecraft:cobblestone>)
  .tool(<tag:items:artisantools:type/hammer>, 10)
  .register();
```

Up to three tools can be used in a recipe.

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<item:minecraft:dirt>])
  .output(<item:minecraft:cobblestone>)
  .tool(<tag:items:artisantools:type/hammer>, 10)
  .tool(<tag:items:artisantools:type/athame>, 5)
  .tool(<tag:items:artisantools:type/quill>, 13)
  .register();
```

You might notice that tag entries for the tools were used in these examples. Artisan Tools provides two different types of tag groups for its tools: groups by tool type and groups by tool material.

For the complete list of tool type and material groups, see the documentation for [Artisan Tools](https://www.curseforge.com/minecraft/mc-mods/artisan-tools-1-16).