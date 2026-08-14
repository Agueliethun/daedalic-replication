package com.daereplication.menu.slot;

import com.daereplication.menu.ReplicatorMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ReplicatorInputSlot extends Slot {

    private final ReplicatorMenu menu;

    public ReplicatorInputSlot(final ReplicatorMenu menu, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(final ItemStack itemStack) {
        return this.menu.isInput(itemStack);
    }

    @Override
    public int getMaxStackSize(final ItemStack itemStack) {
        return 2048;
    }
}