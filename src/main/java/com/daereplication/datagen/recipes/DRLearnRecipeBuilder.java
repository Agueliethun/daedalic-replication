package com.daereplication.datagen.recipes;

import com.daereplication.DaedalicReplication;
import com.daereplication.recipe.ReplicationLearnRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class DRLearnRecipeBuilder implements DRRecipeBuilder<ReplicationLearnRecipe> {

    public static final int ENERGY_MEDIUM = 10;

    public static final int TIME_LOW = 5;       // nugget
    public static final int TIME_MEDIUM = 45;   // ingot/standard
    public static final int TIME_HIGH = 405;    // block

    private final Identifier recipeID;
    private int energy;
    private int time;
    private int progress;
    private final Ingredient input;
    private final ItemLike output;

    public DRLearnRecipeBuilder(String recipeName, Ingredient input, ItemLike output) {
        this.recipeID = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "learn/" + DaedalicReplication.MOD_ID + "_learn_" + recipeName);
        this.energy = ENERGY_MEDIUM;
        this.time = TIME_MEDIUM;
        this.progress = 1;
        this.input = input;
        this.output = output;
    }

    public Identifier getRecipeID() {
        return recipeID;
    }

    public static DRLearnRecipeBuilder build(String recipeName, Ingredient input, ItemLike output) {
        return new DRLearnRecipeBuilder(recipeName, input, output);
    }

    public DRLearnRecipeBuilder withEnergy(int energy) {
        this.energy = energy;
        return this;
    }

    public DRLearnRecipeBuilder withTime(int time) {
        this.time = time;
        return this;
    }

    public DRLearnRecipeBuilder withProgress(int progress) {
        this.progress = progress;
        return this;
    }

    public ReplicationLearnRecipe build() {
        return new ReplicationLearnRecipe(input, new ItemStackTemplate(output.asItem()), energy, time, progress);
    }
}
