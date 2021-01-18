# Table Tiers

The Artisan Worktables crafting tables come in three different flavors: `Tier.WORKTABLE`, `Tier.WORKSTATION`, and `Tier.WORKSHOP`.

## Recipe Tiers

Recipes that don't exceed any of a tier's limitations can be crafted in that tier. For example, a recipe that uses a 3x3 grid pattern and one tool can be crafted in any of the three tiers, while a recipe that uses a 4x4 grid and two tools will only be craftable in a tier three table.

If you want to restrict a recipe from being crafted in a lower tier table, you can use the following method when constructing a recipe:

```java
Recipe restrict(Tier minimumTier);
```

Conversely, if you want to restrict a recipe from being crafted in a higher tier table, you can use the following method:

```java
Recipe restrict(Tier minimumTier, Tier maximumTier);
```

Accepted values for `minimumTier` and `maximumTier` are:

```java
Tier.WORKTABLE
Tier.WORKSTATION
Tier.WORKSHOP
```

## Examples

In the following recipe, even though the table doesn't exceed the limitations of the three tiers, it can only be crafted in a `Workshop` tier table:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;
import mods.artisanworktables.Tier;

Recipe.type(Type.BASIC)
  .shapeless([<minecraft:dirt>])
  .output(<minecraft:cobblestone>)
  .restrict(Tier.WORKSHOP)
  .register();
```

Alternately, the following recipe can only be crafted in a `Worktable` tier table:

```js
import mods.artisanworktables.Recipe;
import mods.artisanworktables.Type;
import mods.artisanworktables.Tier;

Recipe.type(Type.BASIC)
  .shapeless([<minecraft:dirt>])
  .output(<minecraft:cobblestone>)
  .restrict(Tier.WORKTABLE, Tier.WORKTABLE)
  .register();
```
