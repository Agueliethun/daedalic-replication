package com.daereplication.items;

import net.minecraft.world.item.Item;

public class UpgradeItem extends Item {

    public static final int STACK_SIZE = 16;

    public enum SlotType {
        SLOT_NONE,
        SLOT_MAIN,
        SLOT_SECONDARY
    }

    private final SlotType type;

    public UpgradeItem(Properties properties, SlotType type) {
        super(properties.stacksTo(STACK_SIZE));
        this.type = type;
    }

    @Override
    public int getDefaultMaxStackSize() {
        return STACK_SIZE;
    }

    public SlotType getSlotType() {
        return this.type;
    }
}
