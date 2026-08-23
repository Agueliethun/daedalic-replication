package com.daereplication.client.rei;

import com.daereplication.DaedalicReplication;
import com.daereplication.blockitems.DRBlockItemIds;
import com.daereplication.blocks.DRBlocks;
import com.daereplication.client.screen.ReplicatorScreen;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.rei.DRREICommonPlugin;
import com.daereplication.rei.ReplicationLearnDisplay;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.function.Function;

public class DRREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ReplicationLearnCategory());
        registry.add(new ReplicationReplicateCategory());
        registry.addWorkstations(DRREICommonPlugin.LEARN, EntryStacks.of(DRItemIds.REPLICATION_MODEL));
        registry.addWorkstations(DRREICommonPlugin.REPLICATE, EntryStacks.of(DRBlocks.REPLICATOR.asItem()));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(48, 27, 22, 16), ReplicatorScreen.class, DRREICommonPlugin.LEARN);
        registry.registerContainerClickArea(new Rectangle(106, 27, 22, 16), ReplicatorScreen.class, DRREICommonPlugin.REPLICATE);
    }
}
