# Quick Reference

## Type

```java
import mods.artisanworktables.Type;
```

```java
Type.BASIC
Type.BLACKSMITH
Type.CARPENTER
Type.CHEF
Type.CHEMIST
Type.DESIGNER
Type.ENGINEER
Type.FARMER
Type.JEWELER
Type.MAGE
Type.MASON
Type.POTTER
Type.SCRIBE
Type.TAILOR
Type.TANNER
```

## Tier

```java
import mods.artisanworktables.Tier;
```

```java
Tier.WORKTABLE
Tier.WORKSTATION
Tier.WORKSHOP
```

## Recipe

```java
import mods.artisanworktables.Recipe;
```

```java
static Recipe type(Type type);
```

```java
Recipe shaped(IIngredient[][] ingredients);

Recipe shapeless(IIngredient[] ingredients);

Recipe fluid(IFluidStack fluidIngredient);

Recipe secondary(IIngredient[] ingredients);

Recipe secondary(IIngredient[] ingredients, boolean consume);

Recipe mirrored(boolean mirrored);

Recipe tool(IIngredient tool, int damage);

Recipe output(IItemStack output);

Recipe extra(IItemStack output, float chance);

Recipe restrict(Tier minimumTier);

Recipe restrict(Tier minimumTier, Tier maximumTier);

Recipe experience(int experienceRequired);

Recipe experience(int experienceRequired, boolean consume);

Recipe level(int levelRequired);

Recipe level(int levelRequired, boolean consume);

Recipe register();

Recipe register(String recipeName);
```