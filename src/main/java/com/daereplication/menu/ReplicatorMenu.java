package com.daereplication.menu;

import com.daereplication.blockentities.ReplicatorBlockEntity;
import com.daereplication.items.DRItemIds;
import com.daereplication.items.UpgradeItem;
import com.daereplication.menu.slot.ReplicatorInputSlot;
import com.daereplication.menu.slot.ReplicatorLearnerSlot;
import com.daereplication.menu.slot.ReplicatorOutputSlot;
import com.daereplication.menu.slot.UpgradeSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ReplicatorMenu extends MachineMenu {
    private static final int SLOTS_COUNT = 6;
    private static final int DATA_COUNT = 6;

    private static Function<MachineMenu, List<Slot>> getSlotProvider(Container container) {
        return menu -> {
            List<Slot> slots = new ArrayList<>();

            slots.add(new ReplicatorInputSlot(menu, container, ReplicatorBlockEntity.SLOT_INPUT, 22, 27));
            slots.add(new ReplicatorLearnerSlot(container, ReplicatorBlockEntity.SLOT_LEARNER, 80, 27));
            slots.add(new ReplicatorOutputSlot(container, ReplicatorBlockEntity.SLOT_OUTPUT, 138, 27));

            slots.add(new UpgradeSlot(container, ReplicatorBlockEntity.SLOT_UPGRADE_MAIN_1, 183, 8, UpgradeItem.SlotType.SLOT_MAIN));
            slots.add(new UpgradeSlot(container, ReplicatorBlockEntity.SLOT_UPGRADE_SECONDARY_1, 183, 26, UpgradeItem.SlotType.SLOT_SECONDARY));
            slots.add(new UpgradeSlot(container, ReplicatorBlockEntity.SLOT_UPGRADE_SECONDARY_2, 183, 44, UpgradeItem.SlotType.SLOT_SECONDARY));

            return slots;
        };
    }

    public ReplicatorMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOTS_COUNT), new SimpleContainerData(DATA_COUNT));
    }

    public ReplicatorMenu(final int containerId, final Inventory inventory, final Container container, final ContainerData data) {
        super(containerId, inventory, container, data, getSlotProvider(container));
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

    public long getDisplayPower() {
        return (long)Math.ceil(data.get(ReplicatorBlockEntity.DATA_POWER_AMOUNT) / ReplicatorBlockEntity.ENERGY_DISPLAY_FACTOR);
    }

    public long getDisplayPowerMax() {
        return (long)Math.ceil(data.get(ReplicatorBlockEntity.DATA_POWER_MAX) / ReplicatorBlockEntity.ENERGY_DISPLAY_FACTOR);
    }

    public float getPowerPercent() {
        int power = data.get(ReplicatorBlockEntity.DATA_POWER_AMOUNT);
        int powerCapacity = data.get(ReplicatorBlockEntity.DATA_POWER_MAX);
        return powerCapacity == 0 ? 0 : power / (float)powerCapacity;
    }
}
