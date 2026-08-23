package com.daereplication.client.widgets.rei;

import com.daereplication.DaedalicReplication;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DrawableConsumer;
import me.shedaniel.rei.api.client.gui.compat.GuiGraphics;
//import net.minecraft.client.gui.components.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ArrowWidget implements DrawableConsumer, IDrawable {

    private static final int IMAGE_WIDTH = 22;
    private static final int IMAGE_HEIGHT = 16;

    private final int x;
    private final int y;
    private final int time;

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "/textures/gui/sprites/container/dr_progress.png");

    public ArrowWidget(int x, int y, int time) {
        this.x = x;
        this.y = y;
        this.time = time;
    }

    public Component getTooltipComponent() {
        return Component.translatable("daedalic-replication.gui.time", time);
    }

    public Widget getTooltipWidget() {
        return Widgets.createTooltip(new Rectangle(x, y, IMAGE_WIDTH, IMAGE_HEIGHT), getTooltipComponent());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public int getWidth() {
        return IMAGE_WIDTH;
    }

    @Override
    public int getHeight() {
        return IMAGE_HEIGHT;
    }

    @Override
    public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + xOffset, y + yOffset, 0.0F, 0.0F, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
    }
}
