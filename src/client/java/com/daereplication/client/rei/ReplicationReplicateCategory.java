package com.daereplication.client.rei;

import com.daereplication.blocks.DRBlocks;
import com.daereplication.client.DaedalicReplicationClient;
import com.daereplication.client.widgets.HintPowerWidget;
import com.daereplication.client.widgets.rei.ArrowWidget;
import com.daereplication.items.DRItemIds;
import com.daereplication.rei.DRREICommonPlugin;
import com.daereplication.rei.ReplicationLearnDisplay;
import com.daereplication.rei.ReplicationReplicateDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.SimpleDisplayRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ReplicationReplicateCategory implements DisplayCategory<ReplicationReplicateDisplay> {
    @Override
    public CategoryIdentifier<ReplicationReplicateDisplay> getCategoryIdentifier() {
        return DRREICommonPlugin.REPLICATE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("daedalic-replication.gui.replicate.title");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(DRBlocks.REPLICATOR.asItem());
    }

    @Override
    public List<Widget> setupDisplay(ReplicationReplicateDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        List<EntryIngredient> inputs = display.getInputEntries();

        widgets.add(Widgets.createSlot(new Point(bounds.x + 6, bounds.y + 6)).markInput().entries(inputs.getFirst()));
        widgets.add(Widgets.createSlot(new Point(bounds.x + 52, bounds.y + 6)).markOutput().entries(display.getOutputEntries().getFirst()));

        HintPowerWidget hintPowerWidget = new HintPowerWidget(bounds.x + 7, bounds.y + 27);
        hintPowerWidget.updatePower(display.getEnergy(), Math.max(display.getEnergy(), 500));
        widgets.add(Widgets.wrapVanillaWidget(hintPowerWidget));
        widgets.add(hintPowerWidget.getTooltipWidget());

        ArrowWidget arrowWidget = new ArrowWidget(bounds.x + 27, bounds.y + 7, display.getTime());
        widgets.add(Widgets.createDrawableWidget(arrowWidget));
        widgets.add(arrowWidget.getTooltipWidget());

        return widgets;
    }

    @Override
    public DisplayRenderer getDisplayRenderer(ReplicationReplicateDisplay display) {
        return SimpleDisplayRenderer.from(display.getInputEntries(), display.getOutputEntries());
    }

    @Override
    public int getDisplayHeight() {
        return DaedalicReplicationClient.REPLICATE_HINT_HEIGHT;
    }

    @Override
    public int getDisplayWidth(ReplicationReplicateDisplay display) {
        return DaedalicReplicationClient.REPLICATE_HINT_WIDTH;
    }
}
