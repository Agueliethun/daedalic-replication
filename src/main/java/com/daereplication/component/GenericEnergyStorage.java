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
public record GenericEnergyStorage(long power) implements TooltipProvider {
    public static final Codec<GenericEnergyStorage> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.LONG.fieldOf("power").forGetter(GenericEnergyStorage::power)
    ).apply(builder, GenericEnergyStorage::new));

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if (this.power() > 0L) {
            consumer.accept(Component.translatable("item.daedalic-replication.generic_energy.info", this.power()).withStyle(ChatFormatting.RED));
        }
    }
}

