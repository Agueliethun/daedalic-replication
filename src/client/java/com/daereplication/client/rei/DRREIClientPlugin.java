package com.daereplication.client.rei;

import com.daereplication.DaedalicReplication;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public class DRREIClientPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<ReplicationLearnDisplay> LEARN = CategoryIdentifier.of(Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, DRRecipeTypes.REPLICATION_LEARN_PATH));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ReplicationLearnCategory());
        registry.addWorkstations(LEARN, EntryStacks.of(DRItemIds.REPLICATION_MODEL));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.beginFiller(ReplicationLearnRecipe.class).fill(ReplicationLearnDisplay::new);
    }
}
