package com.daereplication.client.widgets;

import com.daereplication.DaedalicReplication;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class HintPowerWidget extends AbstractWidget implements IDrawable {

    private static final int IMAGE_WIDTH = 61;
    private static final int IMAGE_HEIGHT = 7;

    private long power;
    private long max;

    private final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "/textures/gui/sprites/container/hint_power_background.png");
    private final Identifier FOREGROUND_SPRITE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "container/hint_power_foreground");

    public HintPowerWidget(int x, int y) {
        super(x, y, IMAGE_WIDTH, IMAGE_HEIGHT, Component.empty());
    }

    public void updatePower(long power, long max) {
        this.power = power;
        this.max = max;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    protected void extractTooltipForNextRenderPass(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltipForNextRenderPass(graphics, mouseX, mouseY);

        this.setTooltip(Tooltip.create(Component.translatable("deadalic-replication.gui.power", power, max)));
    }

    public Component getTooltipComponent() {
        return Component.translatable("deadalic-replication.gui.power.hint", power);
    }

    public Widget getTooltipWidget() {
        return Widgets.createTooltip(new Rectangle(this.getX(), this.getY(), IMAGE_WIDTH, IMAGE_HEIGHT), getTooltipComponent());
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        float percent = power / (float)max;

        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, getX(), getY(), 0.0F, 0.0F, getWidth(), getHeight(), IMAGE_WIDTH, IMAGE_HEIGHT);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FOREGROUND_SPRITE, getWidth(), getHeight(), 0, 0, getX(), getY(), (int)Math.ceil(IMAGE_WIDTH * percent), IMAGE_HEIGHT);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        return;
    }

    @Override
    public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        float percent = power / (float)max;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, xOffset + getX(), yOffset + getY(), 0.0F, 0.0F, getWidth(), getHeight(), IMAGE_WIDTH, IMAGE_HEIGHT);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, FOREGROUND_SPRITE, getWidth(), getHeight(), 0, 0, xOffset + getX(), yOffset + getY(), (int)Math.ceil(IMAGE_WIDTH * percent), IMAGE_HEIGHT);
    }
}
