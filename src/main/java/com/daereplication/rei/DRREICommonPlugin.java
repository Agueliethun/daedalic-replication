package com.daereplication.rei;

import com.daereplication.DaedalicReplication;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

public class DRREICommonPlugin implements REICommonPlugin {

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.register(new EntryComparator<ItemStack>() {
            @Override
            public long hash(ComparisonContext context, ItemStack stack) {
                ReplicationBlockStorage storage = stack.get(DRDataComponents.REPLICATION_BLOCK_STORAGE);
                if (storage != null && storage.numberIngested() > 0) {
                    return (storage.output().getRegisteredName()).hashCode();
                }

                return 0;
            }
        }, DRItemIds.REPLICATION_MODEL);
    }
}
