package com.daereplication.recipe;

import com.daereplication.DaedalicReplication;
import net.fabricmc.fabric.api.resource.v1.FabricResource;
import net.fabricmc.fabric.impl.resource.FabricResourceReloader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.FireworkRocketRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class DRRecipeTypes {
    public static final String REPLICATION_LEARN_PATH = "replication-learn";
    public static final String REPLICATION_REPLICATE_PATH = "replication-replication";

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

    public static final RecipeType<ReplicationLearnRecipe> REPLICATION_REPLICATION_RECIPE_TYPE = Registry.register(
            BuiltInRegistries.RECIPE_TYPE,
            Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, REPLICATION_REPLICATE_PATH),
            new RecipeType<ReplicationLearnRecipe>() {
                @Override
                public String toString() {
                    return REPLICATION_REPLICATE_PATH;
                }
            }
    );

    public static void init() {

    }
}
