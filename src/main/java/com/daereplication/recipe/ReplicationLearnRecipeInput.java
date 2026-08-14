package com.daereplication.recipe;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.NonNull;

public record ReplicationLearnRecipeInput(ItemStack learner, ItemStack input, long power) implements RecipeInput {

    @Override
    public ItemStack learner() {
        return learner;
    }

    @Override
    public ItemStack input() {
        return input;
    }

    @Override
    public long power() {
        return power;
    }

    @Override
    public ItemStack getItem(int index) {
        return input;
    }

    @Override
    public int size() {
        return 1;
    }
}
