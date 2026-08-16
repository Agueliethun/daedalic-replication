package com.daereplication.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record ReplicationReplicateRecipeInput(ItemStack learner) implements RecipeInput {

    @Override
    public ItemStack learner() {
        return learner;
    }

    @Override
    public ItemStack getItem(int index) {
        return learner;
    }

    @Override
    public int size() {
        return 1;
    }
}
