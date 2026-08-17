package com.daereplication.datagen.recipes;

import com.daereplication.DaedalicReplication;
import com.daereplication.recipe.ReplicationReplicateRecipe;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class DRReplicateRecipeBuilder implements DRRecipeBuilder<ReplicationReplicateRecipe> {

    public static final int ENERGY_MEDIUM = 20;

    public static final int TIME_LOW = 20;
    public static final int TIME_MEDIUM = 80;
    public static final int TIME_HIGH = 160;

    private final Identifier recipeID;
    private int energy;
    private int time;
    private int learnFactor;
    private final Holder<Item> input;

    public DRReplicateRecipeBuilder(String recipeName, Holder<Item> input) {
        this.recipeID = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "replicate/" + DaedalicReplication.MOD_ID + "_replicate_" + recipeName);
        this.energy = ENERGY_MEDIUM;
        this.time = TIME_MEDIUM;
        this.learnFactor = 16;
        this.input = input;
    }

    public Identifier getRecipeID() {
        return recipeID;
    }

    public static DRReplicateRecipeBuilder build(String recipeName, Holder<Item> input) {
        return new DRReplicateRecipeBuilder(recipeName, input);
    }

    public DRReplicateRecipeBuilder withEnergy(int energy) {
        this.energy = energy;
        return this;
    }

    public DRReplicateRecipeBuilder withTime(int time) {
        this.time = time;
        return this;
    }

    public DRReplicateRecipeBuilder withLearnFactor(int learnFactor) {
        this.learnFactor = learnFactor;
        return this;
    }

    public ReplicationReplicateRecipe build() {
        return new ReplicationReplicateRecipe(input, energy, time, learnFactor);
    }
}
