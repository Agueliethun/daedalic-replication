package com.daereplication.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Function;

public abstract class MachineMenu extends AbstractContainerMenu {

    protected static final int INVENTORY_START_X = 8;
    protected static final int INVENTORY_START_Y = 84;

    protected final Container container;
    protected final ContainerData data;
    protected final Level level;

    protected final int slotCount;

    public MachineMenu(final int containerId, final Inventory inventory, final Container container, final ContainerData data, final Function<MachineMenu, List<Slot>> slotsProvider) {
        super(DRMenuTypes.REPLICATION_MACHINE, containerId);

        List<Slot> slots = slotsProvider.apply(this);
        this.slotCount = slots.size();

        this.container = container;
        this.data = data;
        this.level = inventory.player.level();

        container.startOpen(inventory.player);

        slots.forEach(this::addSlot);

        this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);
        this.addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack clicked = stack.copy();

        if (slotIndex < slotCount) {
            // If the clicked slot is in the container, try moving the item to the player inventory.
            // When moving into the player's inventory, we iterate over slots in a reversed order; starting from the last hotbar slot to the first inventory slot.
            if (!this.moveItemStackTo(stack, slotCount, slotCount + Inventory.INVENTORY_SIZE, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(stack, 0, slotCount, true)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return clicked;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
