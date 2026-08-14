package com.daereplication.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

// Here, efficiency is just used as a display for the user in the tooltip
public record ReplicationBlockStorage(int numberIngested, double efficiency, Holder<Item> output) implements TooltipProvider {
    public static final Codec<ReplicationBlockStorage> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.optionalFieldOf("stored", 0).forGetter(ReplicationBlockStorage::numberIngested),
            Codec.DOUBLE.optionalFieldOf("efficiency", 0.0).forGetter(ReplicationBlockStorage::efficiency),
            Item.CODEC.fieldOf("type").forGetter(ReplicationBlockStorage::output)
    ).apply(builder, ReplicationBlockStorage::new));

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if (this.numberIngested > 0 && this.output() != null) {
            Component itemNameComponent = this.output().components().get(DataComponents.ITEM_NAME);
            Component finalComponent = itemNameComponent == null ? Component.empty() : itemNameComponent;

            String efficiencyString = String.format("%.2f", efficiency);

            consumer.accept(Component.translatable("item.daedalic-replication.replication_model.info.number", finalComponent, this.numberIngested).withStyle(ChatFormatting.GOLD));
            consumer.accept(Component.translatable("item.daedalic-replication.replication_model.info.efficiency", efficiencyString).withStyle(ChatFormatting.GOLD));
            consumer.accept(Component.translatable("item.daedalic-replication.replication_model.info.erase").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            consumer.accept(Component.translatable("item.daedalic-replication.replication_model.info.empty").withStyle(ChatFormatting.GRAY));
        }
    }
}

