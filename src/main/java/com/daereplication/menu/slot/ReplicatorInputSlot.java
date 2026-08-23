package com.daereplication.menu.slot;

import com.daereplication.items.DRItemIds;
import com.daereplication.menu.MachineMenu;
import com.daereplication.menu.ReplicatorMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ReplicatorInputSlot extends Slot {

    private final MachineMenu menu;

    public ReplicatorInputSlot(final MachineMenu menu, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(final ItemStack itemStack) {
        return !itemStack.is(DRItemIds.REPLICATION_MODEL);
    }
}