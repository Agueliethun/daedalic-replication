package com.daereplication.recipe;

import com.daereplication.DaedalicReplication;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;

public class DRRecipeTypes {
    public static final String REPLICATION_LEARN_PATH = "replication-learn";
    public static final String REPLICATION_REPLICATE_PATH = "replication-replicate";

    public static final RecipeType<ReplicationLearnRecipe> REPLICATION_LEARN_RECIPE_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE,
            Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, REPLICATION_LEARN_PATH),
            new RecipeType<ReplicationLearnRecipe>() {
                @Override
                public String toString() {
                    return REPLICATION_LEARN_PATH;
                }
            }
    );

    public static final RecipeType<ReplicationReplicateRecipe> REPLICATION_REPLICATE_RECIPE_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE,
            Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, REPLICATION_REPLICATE_PATH),
            new RecipeType<ReplicationReplicateRecipe>() {
                @Override
                public String toString() {
                    return REPLICATION_REPLICATE_PATH;
                }
            }
    );

    public static void init() {

    }
}
