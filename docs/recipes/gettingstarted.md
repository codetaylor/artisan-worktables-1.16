# Getting Started

Artisan Worktables is designed to be used with [CraftTweaker](https://www.curseforge.com/minecraft/mc-mods/crafttweaker) and its scripting language, ZenScript.

!!! note
    For more information about how to use ZenScript, please visit the [official CraftTweaker documentation](https://docs.blamejared.com/).

## Building Recipes

Recipes are created by calling methods on a `Recipe` builder object.

`Recipe` builder objects are retrieved by importing `Recipe` and `Type` and calling `type(Type type)` for the desired table.

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

val builder = Recipe.type(Type.BASIC);
```

You must call `Recipe.type(Type type)` for each new recipe.

For a list of valid table types, see [Quick Reference](quickreference.md#type).

## Using the Builder

The builder object method calls are chained back-to-back:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<item:minecraft:dirt>])
  .output(<item:minecraft:cobblestone>)
  .register();
```

## Finalizing Recipes

To finalize, or actually create, a recipe, you must call `register()` on the builder object. Doing this will validate and save the recipe.
