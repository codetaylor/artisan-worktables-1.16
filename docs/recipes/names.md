# Recipe Names

All recipes have names. By default, if no name is supplied when creating a recipe, an attempt is made by the system to provide a unique name that will remain consistent between game loads.

## Custom Names

You can supply your own name for a recipe by providing a `String` argument to the `register` method:

```java
Recipe register(String name);
```

!!! note
    Supplying your own custom name is the best way to guarantee a unique name that will never change.

## Generated Names

Recipe names are optional. If omitted, the system will attempt to generate a unique recipe name by hashing the recipe's components and combining the resulting hash with the name of the table the recipe belongs to. If two recipes generate the same hash, the second recipe's hash will be incremented until it's not a duplicate.

In most cases, the generated names should be fine, however, it is important to know that there is a small chance they could cause problems and why it could happen. Read below for more information.

### Hash Collision

Because we use hashes, duplicate names may be auto-generated for recipes with different components. If a duplicate name is generated, the system will increment the resulting hash by one until the recipe name is not a duplicate. There are edge cases where this hash collision could present a problem.

For example, let's say you create recipe A and recipe B. Both recipes have different components, meaning different inputs and outputs, yet the name generation hashing produces a collision and generates the same name for both recipes. Let's say recipe A is named `tailor_34580`, recipe B will then be incremented and named `tailor_34581`. In most cases, this is fine. Now let's say you create a world and create items in that world that reference recipe A and recipe B. If you were to change the order in which those two recipes are loaded, because their auto-generated names collide, the recipes would swap names. The same type of problem can occur if another recipe with a colliding hash is introduced before recipe A or recipe B. These name changes may cause issues in the existing world.