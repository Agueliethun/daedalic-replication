package com.daereplication.recipe;

import com.daereplication.DaedalicReplication;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class ReplicationLearnRecipe implements Recipe<ReplicationLearnRecipeInput> {

    public static final MapCodec<ReplicationLearnRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Ingredient.CODEC.fieldOf("input").forGetter(ReplicationLearnRecipe::getInput),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(ReplicationLearnRecipe::getOutput),
            Codec.INT.fieldOf("energy").forGetter(ReplicationLearnRecipe::getEnergy),
            Codec.INT.fieldOf("time").forGetter(ReplicationLearnRecipe::getTime),
            Codec.INT.fieldOf("progress").forGetter(ReplicationLearnRecipe::getProgress)
        ).apply(instance, ReplicationLearnRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ReplicationLearnRecipe> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC,
        ReplicationLearnRecipe::getInput,
        ItemStackTemplate.STREAM_CODEC,
        ReplicationLearnRecipe::getOutput,
        ByteBufCodecs.INT,
        ReplicationLearnRecipe::getEnergy,
        ByteBufCodecs.INT,
        ReplicationLearnRecipe::getTime,
        ByteBufCodecs.INT,
        ReplicationLearnRecipe::getProgress,
        ReplicationLearnRecipe::new
    );

    public static final RecipeSerializer<ReplicationLearnRecipe> SERIALIZER = Registry.register(
        BuiltInRegistries.RECIPE_SERIALIZER,
        Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, DRRecipeTypes.REPLICATION_LEARN_PATH),
        new RecipeSerializer<>(ReplicationLearnRecipe.MAP_CODEC, ReplicationLearnRecipe.STREAM_CODEC)
    );

    private final Ingredient input;
    private final ItemStackTemplate output;
    private final int energy;
    private final int time;
    private final int progress;

    public static void init() {}

    public ReplicationLearnRecipe(Ingredient input, ItemStackTemplate output, int energy, int time, int progress) {
        this.input = input;
        this.output = output;
        this.energy = energy;
        this.time = time;
        this.progress = progress;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStackTemplate getOutput() {
        return output;
    }

    public int getEnergy() {
        return energy;
    }

    public int getTime() {
        return time;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public boolean matches(ReplicationLearnRecipeInput input, Level level) {
        return this.input.test(input.input());
    }

    @Override
    public ItemStack assemble(ReplicationLearnRecipeInput input) {
        return new ItemStack(this.output.item());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<ReplicationLearnRecipeInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<ReplicationLearnRecipeInput>> getType() {
        return DRRecipeTypes.REPLICATION_LEARN_RECIPE_TYPE;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }
}
