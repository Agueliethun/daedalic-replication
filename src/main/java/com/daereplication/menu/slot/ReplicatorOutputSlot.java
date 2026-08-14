package com.daereplication.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ReplicatorOutputSlot extends Slot {

    public ReplicatorOutputSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(final ItemStack itemStack) {
        return false;
    }
}
