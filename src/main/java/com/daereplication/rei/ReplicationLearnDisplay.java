package com.daereplication.rei;

import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ReplicationLearnDisplay extends BasicDisplay {
    public static final DisplaySerializer<ReplicationLearnDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(ReplicationLearnDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(ReplicationLearnDisplay::getOutputEntries),
                    Codec.INT.fieldOf("energy").forGetter(ReplicationLearnDisplay::getEnergy),
                    Codec.INT.fieldOf("time").forGetter(ReplicationLearnDisplay::getTime)
            ).apply(instance, ReplicationLearnDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    ReplicationLearnDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    ReplicationLearnDisplay::getOutputEntries,
                    ByteBufCodecs.INT,
                    ReplicationLearnDisplay::getEnergy,
                    ByteBufCodecs.INT,
                    ReplicationLearnDisplay::getTime,
                    ReplicationLearnDisplay::new
            ));

    private final int energy;
    private final int time;

    public ReplicationLearnDisplay(RecipeHolder<ReplicationLearnRecipe> holder) {
        ReplicationLearnRecipe recipe = holder.value();

        EntryIngredient mainIngredient = EntryIngredients.ofIngredient(recipe.getInput());
        EntryIngredient learner = EntryIngredients.of(DRItemIds.REPLICATION_MODEL);

        ReplicationBlockStorage storage = new ReplicationBlockStorage(1, 1.0, recipe.getOutput().typeHolder());
        ItemStackTemplate template = new ItemStackTemplate(DRItemIds.REPLICATION_MODEL, DataComponentPatch.builder().set(DRDataComponents.REPLICATION_BLOCK_STORAGE, storage).build());
        EntryIngredient resultLearner = EntryIngredients.of(template);

        this(List.of(mainIngredient, learner), Collections.singletonList(resultLearner), recipe.getTime(), recipe.getEnergy());
    }

    public ReplicationLearnDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int energy, int time) {
        super(inputs, outputs);
        this.energy = energy;
        this.time = time;
    }

    public int getEnergy() {
        return energy;
    }

    public int getTime() {
        return time;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return DRREICommonPlugin.LEARN;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}
