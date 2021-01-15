# Basic Recipes

All recipes require, at the very least, an input and an output. Artisan Worktables' recipes come in two forms that most of you are probably familiar with: shaped and shapeless.

## Shaped Recipes

Shaped recipes are created by calling the following method on the builder:

```java
Recipe shaped(IIngredient[][] ingredients);
```

For example, the following recipe will craft a furnace using the vanilla furnace pattern in any of the `basic` tables.

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shaped([
    [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>],
    [<minecraft:cobblestone>, null, <minecraft:cobblestone>],
    [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>]])
  .output(<minecraft:furnace>)
  .register();
```

Shaped recipes are mirrored by default. To prevent a shaped recipe from being mirrored in the crafting grid, call the following method on the builder:

```java
Recipe mirrored(boolean mirrored);
```

## Shapeless Recipes

Shapeless recipes are created by calling the following method on the builder:

```java
Recipe shapeless(IIngredient[] ingredients);
```

For example, the following recipe will take any item matching the tag for iron ingots and craft an oak plank in any of the `basic` tables.

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<tag:items:forge:ingots/iron>])
  .output(<minecraft:planks>)
  .register();
```