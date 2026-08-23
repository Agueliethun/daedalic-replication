package com.daereplication.client.widgets;

import com.daereplication.DaedalicReplication;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class PowerWidget extends AbstractWidget {

    private static final int IMAGE_WIDTH = 122;
    private static final int IMAGE_HEIGHT = 21;

    private static final float LERP_SPEED = 0.25F;

    private float lastPowerLerp;

    private long power;
    private long max;

    private final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "/textures/gui/sprites/container/power_background.png");
    private final Identifier FOREGROUND_SPRITE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "container/power_foreground");

    public PowerWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
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

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        float percent = power / (float)max;
        float newPower = lastPowerLerp == 0.0F ? percent : LERP_SPEED * lastPowerLerp + (1 - LERP_SPEED) * percent;

        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, getX(), getY(), 0.0F, 0.0F, getWidth(), getHeight(), IMAGE_WIDTH, IMAGE_HEIGHT);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FOREGROUND_SPRITE, getWidth(), getHeight(), 0, 0, getX(), getY(), (int)Math.ceil(IMAGE_WIDTH * newPower), IMAGE_HEIGHT);

        this.lastPowerLerp = newPower;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        return;
    }
}
