# Secondary Ingredients

Secondary ingredients are items that are required by a recipe and consumed when crafting, but aren't placed in the crafting grid. Instead, these items are placed in the secondary ingredient storage located directly above the player's inventory in the crafting GUI.

`Tier.WORKSTATION` and `Tier.WORKSHOP` recipes can support up to nine unique secondary ingredients.

## Add Secondary Ingredients to Recipes

To add secondary ingredients to a recipe, call the following method on a builder:

```java
Recipe secondary(IIngredient[] secondaryIngredients);
```

If you want your recipe to require secondary ingredients, but not actually consume them, use the following method:

```java
Recipe secondary(IIngredient[] secondaryIngredients, boolean consume);
```

## Examples

The following recipe requires `<minecraft:gravel>` and `<minecraft:string>` in the secondary ingredient storage area to craft:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<item:minecraft:dirt>])
  .secondary([<item:minecraft:gravel>, <item:minecraft:string>])
  .output(<item:minecraft:cobblestone>)
  .register();
```

The following recipe is identical to the above recipe, except that it won't actually consume the secondary ingredients when crafted:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<item:minecraft:dirt>])
  .secondary([<item:minecraft:gravel>, <item:minecraft:string>], false)
  .output(<item:minecraft:cobblestone>)
  .register();
```
