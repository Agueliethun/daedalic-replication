package com.daereplication.registry;

import com.daereplication.DaedalicReplication;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class DRRegistries {

    public static final ResourceKey<Registry<ReplicationLearnRecipe>> REPLICATION_LEARN_RECIPES = createRegistryKey(DRRecipeTypes.REPLICATION_LEARN_PATH);
    public static final ResourceKey<Registry<ReplicationLearnRecipe>> REPLICATION_REPLICATION_RECIPES = createRegistryKey(DRRecipeTypes.REPLICATION_REPLICATE_PATH);

    private static <T> ResourceKey<Registry<T>> createRegistryKey(final String name) {
        return ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, name));
    }

    public static void init() {

    }
}
