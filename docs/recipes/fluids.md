# Recipes with Fluids

Each crafting table in Artisan Worktables has a fluid tank and recipes can require a fluid to craft. Fluids can be inserted and extracted using buckets or any other mechanism that interacts using the Forge fluid capability.

## Add Fluids to Recipes

To add a fluid to a recipe, call the following method on a builder:

```java
Recipe fluid(IFluidStack fluidIngredient);
```

For example, the following recipe will consume `250` millibuckets of `<liquid:water>` to craft:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<item:minecraft:dirt>])
  .fluid(<fluid:minecraft:water> * 250)
  .output(<item:minecraft:cobblestone>)
  .register();
```
