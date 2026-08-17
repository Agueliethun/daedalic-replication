package com.daereplication.datagen.recipes;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;

public class DRRecipeProvider extends RecipeProvider {
    private final Map<Identifier, DRRecipeBuilder<?>> recipeBuilders;

    protected DRRecipeProvider(HolderLookup.Provider registries, RecipeOutput output, Map<Identifier, DRRecipeBuilder<?>> recipeBuilders) {
        super(registries, output);
        this.recipeBuilders = recipeBuilders;
    }

    @Override
    public void buildRecipes() {
        for (Map.Entry<Identifier, DRRecipeBuilder<?>> entry : recipeBuilders.entrySet()) {
            ResourceKey<Recipe<?>> recipeKeyID = ResourceKey.create(Registries.RECIPE, entry.getKey());
            output.accept(recipeKeyID, entry.getValue().build(), null);
        }
    }
}