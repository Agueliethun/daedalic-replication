package com.daereplication.util;

import com.daereplication.DaedalicReplication;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import net.fabricmc.loader.impl.util.log.Log;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.*;

public class ReplicatorUtil {

    public static double getEfficiency(ReplicationLearnRecipe recipe, int numStored) {
        if (recipe == null) {
            return 0.0;
        }

        return numStored / (numStored + (double)recipe.getLearnFactor());
    }
}
