package com.daereplication.menu.slot;

import com.daereplication.items.DRItemIds;
import com.daereplication.menu.ReplicatorMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ReplicatorLearnerSlot extends Slot {

    public ReplicatorLearnerSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(final ItemStack itemStack) {
        return itemStack.is(DRItemIds.REPLICATION_MODEL);
    }

    @Override
    public int getMaxStackSize(final ItemStack itemStack) {
        return 1;
    }
}
