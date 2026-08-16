package com.daereplication.recipe;

import com.daereplication.DaedalicReplication;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ReplicationReplicateRecipe implements Recipe<ReplicationReplicateRecipeInput> {

    public static final MapCodec<ReplicationReplicateRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Item.CODEC.fieldOf("input").forGetter(ReplicationReplicateRecipe::getInput),
            Codec.INT.fieldOf("energy").forGetter(ReplicationReplicateRecipe::getEnergy),
            Codec.INT.fieldOf("time").forGetter(ReplicationReplicateRecipe::getTime),
            Codec.INT.fieldOf("learnFactor").forGetter(ReplicationReplicateRecipe::getLearnFactor)
        ).apply(instance, ReplicationReplicateRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ReplicationReplicateRecipe> STREAM_CODEC = StreamCodec.composite(
        Item.STREAM_CODEC,
        ReplicationReplicateRecipe::getInput,
        ByteBufCodecs.INT,
        ReplicationReplicateRecipe::getEnergy,
        ByteBufCodecs.INT,
        ReplicationReplicateRecipe::getTime,
            ByteBufCodecs.INT,
            ReplicationReplicateRecipe::getLearnFactor,
        ReplicationReplicateRecipe::new
    );

    public static final RecipeSerializer<ReplicationReplicateRecipe> SERIALIZER = Registry.register(
        BuiltInRegistries.RECIPE_SERIALIZER,
        Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, DRRecipeTypes.REPLICATION_REPLICATE_PATH),
        new RecipeSerializer<>(ReplicationReplicateRecipe.MAP_CODEC, ReplicationReplicateRecipe.STREAM_CODEC)
    );

    private final Holder<Item> input;
    private final int energy;
    private final int time;
    private final int learnFactor;

    public static void init() {}

    public ReplicationReplicateRecipe(Holder<Item> input, int energy, int time, int learnFactor) {
        this.input = input;
        this.energy = energy;
        this.time = time;
        this.learnFactor = learnFactor;
    }

    public Holder<Item> getInput() {
        return input;
    }

    public int getEnergy() {
        return energy;
    }

    public int getTime() {
        return time;
    }

    public int getLearnFactor() {
        return learnFactor;
    }

    @Override
    public boolean matches(ReplicationReplicateRecipeInput input, Level level) {
        ReplicationBlockStorage storage = input.learner().get(DRDataComponents.REPLICATION_BLOCK_STORAGE);
        if (storage == null || storage.numberIngested() <= 0) {
            return false;
        }

        try {
            Identifier outputID = level.registryAccess().lookupOrThrow(Registries.ITEM).getKey(storage.output().value());

            if (outputID == null) {
                return false;
            }

            return getInput().is(outputID);
        } catch (IllegalStateException e) {
            DaedalicReplication.LOGGER.warn("Error accessing item registry for recipe input: {}", input);
            return false;
        }
    }

    @Override
    public ItemStack assemble(ReplicationReplicateRecipeInput input) {
        return new ItemStack(getInput());
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
    public RecipeSerializer<? extends Recipe<ReplicationReplicateRecipeInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<ReplicationReplicateRecipeInput>> getType() {
        return DRRecipeTypes.REPLICATION_REPLICATE_RECIPE_TYPE;
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
