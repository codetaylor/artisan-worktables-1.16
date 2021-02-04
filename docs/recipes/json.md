# JSON Recipes

Artisan Worktables for 1.16 also supports json recipes in datapacks.

Here is a shaped example to get you started:

```json
{
  "type": "artisanworktables:basic_shaped",
  "pattern": [
    " s ",
    "sps",
    " s "
  ],
  "key": {
    "s": {"item": "minecraft:string"},
    "p": {"tag": "minecraft:planks"}
  },
  "tools": [
    {"item": "minecraft:diamond_pickaxe", "damage": 15}
  ],
  "fluidIngredient": {"fluid": "minecraft:water", "amount": 1000},
  "consumeSecondaryIngredients": true,
  "secondaryIngredients": [
    {"item": "minecraft:emerald"},
    {"tag": "forge:ingots/iron"}
  ],
  "extraOutput": [
    {"item": "minecraft:diamond", "count": 4, "chance": 0.5}
  ],
  "mirrored": true,
  "minimumTier": 0,
  "maximumTier": 2,
  "experienceRequired": 0,
  "levelRequired": 0,
  "consumeExperience": true,
  "result": {
    "item": "minecraft:diamond"
  }
}
```

Here is a shapeless example:

```json
{
  "type": "artisanworktables:basic_shapeless",
  "ingredients": [
    {"item": "minecraft:string"},
    {"tag": "minecraft:planks"}
  ],
  "tools": [
    {"item": "minecraft:diamond_pickaxe", "damage": 15}
  ],
  "fluidIngredient": {"fluid": "minecraft:water", "amount": 1000},
  "consumeSecondaryIngredients": true,
  "secondaryIngredients": [
    {"item": "minecraft:emerald"},
    {"tag": "forge:ingots/iron"}
  ],
  "extraOutput": [
    {"item": "minecraft:diamond", "count": 4, "chance": 0.5}
  ],
  "mirrored": true,
  "minimumTier": 0,
  "maximumTier": 2,
  "experienceRequired": 0,
  "levelRequired": 0,
  "consumeExperience": true,
  "result": {
    "item": "minecraft:diamond"
  }
}
```