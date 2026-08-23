package com.daereplication.client.rei;

import com.daereplication.client.DaedalicReplicationClient;
import com.daereplication.client.widgets.HintPowerWidget;
import com.daereplication.client.widgets.PowerWidget;
import com.daereplication.client.widgets.rei.ArrowWidget;
import com.daereplication.items.DRItemIds;
import com.daereplication.rei.DRREICommonPlugin;
import com.daereplication.rei.ReplicationLearnDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.DrawableConsumer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.SimpleDisplayRenderer;
import me.shedaniel.rei.api.client.gui.compat.GuiGraphics;
import me.shedaniel.rei.api.client.gui.widgets.Arrow;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ReplicationLearnCategory implements DisplayCategory<ReplicationLearnDisplay> {

    @Override
    public CategoryIdentifier<ReplicationLearnDisplay> getCategoryIdentifier() {
        return DRREICommonPlugin.LEARN;
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

        widgets.add(Widgets.createSlot(new Point(bounds.x + 6, bounds.y + 6)).markInput().entries(inputs.get(0)));
        widgets.add(Widgets.createSlot(new Point(bounds.x + 27, bounds.y + 6)).markInput().entries(inputs.get(1)));
        widgets.add(Widgets.createSlot(new Point(bounds.x + 73, bounds.y + 6)).markOutput().entries(display.getOutputEntries().getFirst()));

        HintPowerWidget hintPowerWidget = new HintPowerWidget(bounds.x + 17, bounds.y + 25);
        hintPowerWidget.updatePower(display.getEnergy(), Math.max(display.getEnergy(), 500));
        widgets.add(Widgets.wrapVanillaWidget(hintPowerWidget));
        widgets.add(hintPowerWidget.getTooltipWidget());

        ArrowWidget arrowWidget = new ArrowWidget(bounds.x + 48, bounds.y + 7, display.getTime());
        widgets.add(Widgets.createDrawableWidget(arrowWidget));
        widgets.add(arrowWidget.getTooltipWidget());

        return widgets;
    }

    @Override
    public DisplayRenderer getDisplayRenderer(ReplicationLearnDisplay display) {
        return SimpleDisplayRenderer.from(display.getInputEntries(), display.getOutputEntries());
    }

    @Override
    public int getDisplayHeight() {
        return DaedalicReplicationClient.LEARN_HINT_HEIGHT;
    }

    @Override
    public int getDisplayWidth(ReplicationLearnDisplay display) {
        return DaedalicReplicationClient.LEARN_HINT_WIDTH;
    }
}