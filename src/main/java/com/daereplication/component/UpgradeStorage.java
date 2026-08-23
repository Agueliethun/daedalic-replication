package com.daereplication.component;

import com.daereplication.items.UpgradeItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

// Just store the values so we can display them in the tooltip
public record UpgradeStorage(int learnBonus, int efficiency, int speed) implements TooltipProvider {
    public static final Codec<UpgradeStorage> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.optionalFieldOf("learnBonus", 0).forGetter(UpgradeStorage::learnBonus),
            Codec.INT.optionalFieldOf("efficiency", 0).forGetter(UpgradeStorage::efficiency),
            Codec.INT.optionalFieldOf("speed", 0).forGetter(UpgradeStorage::speed)
    ).apply(builder, UpgradeStorage::new));

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if (learnBonus > 0) {
            consumer.accept(Component.translatable("item.upgrade.storage.learn", learnBonus));
        }

        if (speed > 0 && efficiency > 0) {
            consumer.accept(Component.translatable("item.upgrade.storage.master", speed, efficiency));
        } else if (speed > 0 && efficiency == 0) {
            consumer.accept(Component.translatable("item.upgrade.storage.speed.small", speed));
        } else if (speed > 0) {
            consumer.accept(Component.translatable("item.upgrade.storage.speed", speed, efficiency));
        } else if (speed == 0 && efficiency > 0) {
            consumer.accept(Component.translatable("item.upgrade.storage.efficiency.small", efficiency));
        } else if (speed < 0 && efficiency > 0) {
            consumer.accept(Component.translatable("item.upgrade.storage.efficiency", efficiency, speed));
        }

        if (learnBonus != 0 || speed != 0 || efficiency != 0) {
            consumer.accept(Component.translatable("item.upgrade.stacks.max", UpgradeItem.STACK_SIZE));
        }
    }
}

