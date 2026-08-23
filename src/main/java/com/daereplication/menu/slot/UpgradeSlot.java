package com.daereplication.menu.slot;

import com.daereplication.component.DRDataComponents;
import com.daereplication.items.DRItemIds;
import com.daereplication.items.UpgradeItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class UpgradeSlot extends Slot {

    final UpgradeItem.SlotType type;

    public UpgradeSlot(Container container, int slot, int x, int y, UpgradeItem.SlotType type) {
        super(container, slot, x, y);
        this.type = type;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        Item item = itemStack.typeHolder().value();
        if (item instanceof UpgradeItem upgradeItem) {
            return upgradeItem.getSlotType() == type;
        }

        return false;
    }
}
