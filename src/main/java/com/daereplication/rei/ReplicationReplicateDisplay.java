package com.daereplication.rei;

import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.recipe.ReplicationReplicateRecipe;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ReplicationReplicateDisplay extends BasicDisplay {
    public static final DisplaySerializer<ReplicationReplicateDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(ReplicationReplicateDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(ReplicationReplicateDisplay::getOutputEntries),
                    Codec.INT.fieldOf("energy").forGetter(ReplicationReplicateDisplay::getEnergy),
                    Codec.INT.fieldOf("time").forGetter(ReplicationReplicateDisplay::getTime),
                    Codec.INT.fieldOf("learnFactor").forGetter(ReplicationReplicateDisplay::getTime)
            ).apply(instance, ReplicationReplicateDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    ReplicationReplicateDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    ReplicationReplicateDisplay::getOutputEntries,
                    ByteBufCodecs.INT,
                    ReplicationReplicateDisplay::getEnergy,
                    ByteBufCodecs.INT,
                    ReplicationReplicateDisplay::getTime,
                    ByteBufCodecs.INT,
                    ReplicationReplicateDisplay::getLearnFactor,
                    ReplicationReplicateDisplay::new
            ));

    private final int energy;
    private final int time;
    private final int learnFactor;

    public ReplicationReplicateDisplay(RecipeHolder<ReplicationReplicateRecipe> holder) {
        ReplicationReplicateRecipe recipe = holder.value();

        DataComponentPatch patch = DataComponentPatch.builder().set(DRDataComponents.REPLICATION_BLOCK_STORAGE, new ReplicationBlockStorage(1, 1, recipe.getInput())).build();
        ItemStack learnerStack = new ItemStack(DRItemIds.REPLICATION_MODEL);
        learnerStack.applyComponents(patch);
        EntryIngredient learner = EntryIngredients.of(learnerStack);
        this(List.of(learner), List.of(EntryIngredients.of(recipe.getInput().value())), recipe.getTime(), recipe.getEnergy(), recipe.getLearnFactor());
    }

    public ReplicationReplicateDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int energy, int time, int learnFactor) {
        super(inputs, outputs);
        this.energy = energy;
        this.time = time;
        this.learnFactor = learnFactor;
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
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return DRREICommonPlugin.REPLICATE;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}
