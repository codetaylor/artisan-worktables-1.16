# Extra Chance Output

Each recipe can define extra outputs with a chance of being created when the craft operation is performed. When these extra outputs are created, they are placed into one of the three extra output slots found near the main result slot of the GUI. If these slots are full and the extra output can't be placed into one of these storage slots, it will pop out into the world on top of the table.

## Add Extra Output to Recipes

```java
Recipe extra(IItemStack output, float chance);
```

In these methods, `chance` is a float in the range `[0,1]`.

For example, the following recipe has a `75%` chance of dropping a `<minecraft:string>` and a `25%` chance of dropping a `<minecraft:diamond>`:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<item:minecraft:dirt>])
  .output(<item:minecraft:cobblestone>)
  .extra(<item:minecraft:string>, 0.75)
  .extra(<item:minecraft:diamond>, 0.25)
  .register();
```