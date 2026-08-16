package com.daereplication.menu;

import com.daereplication.blockentities.ReplicatorBlockEntity;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.menu.slot.ReplicatorInputSlot;
import com.daereplication.menu.slot.ReplicatorLearnerSlot;
import com.daereplication.menu.slot.ReplicatorOutputSlot;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.recipe.ReplicationLearnRecipeInput;
import com.daereplication.util.ReplicatorUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

import static com.daereplication.blockentities.ReplicatorBlockEntity.SLOT_INPUT;
import static com.daereplication.blockentities.ReplicatorBlockEntity.SLOT_LEARNER;

public class ReplicatorMenu extends AbstractContainerMenu {

    private final static int SLOTS_COUNT = 3;
    private final static int DATA_COUNT = 6;

    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 84;

    private static final int CONTAINER_START = 0;
    private static final int CONTAINER_END = SLOTS_COUNT;
    private static final int INVENTORY_START = CONTAINER_END;
    private static final int INVENTORY_END = INVENTORY_START + Inventory.INVENTORY_SIZE;

    private final Container container;
    private final ContainerData data;
    protected final Level level;

    public ReplicatorMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOTS_COUNT), new SimpleContainerData(DATA_COUNT));
    }

    public ReplicatorMenu(final int containerId, final Inventory inventory, final Container container, final ContainerData data) {
        super(DRMenuTypes.REPLICATOR, containerId);

        checkContainerSize(container, SLOTS_COUNT);
        checkContainerDataCount(data, DATA_COUNT);

        this.container = container;
        this.data = data;
        this.level = inventory.player.level();

        container.startOpen(inventory.player);

        this.addSlot(new ReplicatorInputSlot(this, container, SLOT_INPUT, 22, 27));
        this.addSlot(new ReplicatorLearnerSlot(container, SLOT_LEARNER, 80, 27));
        this.addSlot(new ReplicatorOutputSlot(container, ReplicatorBlockEntity.SLOT_OUTPUT, 138, 27));

        this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);
        this.addDataSlots(data);
    }

    public boolean isInput(ItemStack itemStack) {
        return !itemStack.is(DRItemIds.REPLICATION_MODEL);
    }

    public float getLearnProgress() {
        int progress = data.get(ReplicatorBlockEntity.DATA_LEARN_PROGRESS);
        int totalTime = data.get(ReplicatorBlockEntity.DATA_LEARN_TOTAL_TIME);
        return totalTime == 0 ? 0 : progress / (float)totalTime;
    }

    public float getReplicateProgress() {
        int progress = data.get(ReplicatorBlockEntity.DATA_REPLICATE_PROGRESS);
        int totalTime = data.get(ReplicatorBlockEntity.DATA_REPLICATE_TOTAL_TIME);
        return totalTime == 0 ? 0 : progress / (float)totalTime;
    }

    public float getPowerPercent() {
        int power = data.get(ReplicatorBlockEntity.DATA_POWER_AMOUNT);
        int powerCapacity = data.get(ReplicatorBlockEntity.DATA_POWER_MAX);
        return powerCapacity == 0 ? 0 : power / (float)powerCapacity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack clicked = stack.copy();

        if (slotIndex < CONTAINER_END) {
            // If the clicked slot is in the container, try moving the item to the player inventory.
            // When moving into the player's inventory, we iterate over slots in a reversed order; starting from the last hotbar slot to the first inventory slot.
            if (!this.moveItemStackTo(stack, INVENTORY_START, INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(stack, CONTAINER_START, CONTAINER_END, false)) {
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
