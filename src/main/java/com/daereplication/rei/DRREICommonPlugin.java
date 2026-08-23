package com.daereplication.rei;

import com.daereplication.DaedalicReplication;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.recipe.ReplicationReplicateRecipe;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

public class DRREICommonPlugin implements REICommonPlugin {

    private static final Identifier LEARN_ID = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, DRRecipeTypes.REPLICATION_LEARN_PATH);
    private static final Identifier REPLICATE_ID = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, DRRecipeTypes.REPLICATION_REPLICATE_PATH);

    public static final CategoryIdentifier<ReplicationLearnDisplay> LEARN = CategoryIdentifier.of(LEARN_ID);
    public static final CategoryIdentifier<ReplicationReplicateDisplay> REPLICATE = CategoryIdentifier.of(REPLICATE_ID);

    @Override
    public double getPriority() {
        return 1;
    }

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.register((context, stack) -> {
            ReplicationBlockStorage storage = stack.get(DRDataComponents.REPLICATION_BLOCK_STORAGE);
            if (storage != null && storage.numberIngested() > 0) {
                return (storage.output().getRegisteredName()).hashCode();
            }

            return 0;
        }, DRItemIds.REPLICATION_MODEL);
    }

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(ReplicationLearnRecipe.class).fill(ReplicationLearnDisplay::new);
        registry.beginRecipeFiller(ReplicationReplicateRecipe.class).fill(ReplicationReplicateDisplay::new);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, DRRecipeTypes.REPLICATION_LEARN_PATH), ReplicationLearnDisplay.SERIALIZER);
        registry.register(Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, DRRecipeTypes.REPLICATION_REPLICATE_PATH), ReplicationReplicateDisplay.SERIALIZER);
    }
}
