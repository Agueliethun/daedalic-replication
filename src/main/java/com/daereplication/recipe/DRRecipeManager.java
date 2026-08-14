package com.daereplication.recipe;

import com.daereplication.registry.DRRegistries;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class DRRecipeManager {

    private static final FileToIdConverter RECIPE_LISTER = FileToIdConverter.registry(DRRegistries.REPLICATION_LEARN_RECIPES);

    private final HolderLookup.Provider registries;

    public DRRecipeManager(HolderLookup.Provider registries) {
        this.registries = registries;
    }
}
