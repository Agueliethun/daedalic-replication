package com.daereplication.client.rei;

import com.daereplication.DaedalicReplication;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ReplicationLearnCategory implements DisplayCategory<ReplicationLearnDisplay> {
    @Override
    public CategoryIdentifier<ReplicationLearnDisplay> getCategoryIdentifier() {
        return DRREIClientPlugin.LEARN;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("daedalic-replication.gui.learn.title");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(DRItemIds.REPLICATION_MODEL);
    }

    @Override
    public List<Widget> setupDisplay(ReplicationLearnDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        List<EntryIngredient> inputs = display.getInputEntries();

        Widgets.createSlot(new Point(22, 27)).markInput().entries(inputs.get(0));
        Widgets.createSlot(new Point(80, 27)).markInput().entries(inputs.get(1));
        Widgets.createSlot(new Point(138, 27)).markOutput().entries(display.getOutputEntries().getFirst());

        return widgets;
    }
}
