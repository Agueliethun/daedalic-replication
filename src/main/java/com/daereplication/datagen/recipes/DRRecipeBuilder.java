package com.daereplication.datagen.recipes;


import net.minecraft.world.item.crafting.Recipe;

public interface DRRecipeBuilder<T extends Recipe<?>> {
    public T build();
}
