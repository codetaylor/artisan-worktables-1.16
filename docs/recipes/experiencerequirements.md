# Experience Requirements

Experience requirements can be set on recipes. The requirement can be defined in experience or levels and set to not actually consume the required amount if desired.

## Add Experience Requirements to Recipes

The following builder methods are used to define experience requirements:

```java
Recipe experience(int experienceRequired);
Recipe experience(int experienceRequired, boolean consume);
Recipe level(int levelRequired);
Recipe level(int levelRequired, boolean consume);
```

By default, recipes will consume experience or levels when the requirements are set.

For example, the following recipe requires and consumes `20` experience when crafting:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<minecraft:dirt>])
  .output(<minecraft:cobblestone>)
  .experience(20)
  .register();
```

To present another example, the following recipe requires that the player has `30` levels, but does not consume any experience when crafting:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;

Recipe.type(Type.BASIC)
  .shapeless([<minecraft:dirt>])
  .level(30, false)
  .output(<minecraft:cobblestone>)
  .register();
```
