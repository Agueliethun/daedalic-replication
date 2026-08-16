package com.daereplication.client.widgets;

import com.daereplication.DaedalicReplication;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class PowerWidget extends AbstractWidget {

    private final static int IMAGE_WIDTH = 122;
    private final static int IMAGE_HEIGHT = 21;

    private final static float LERP_SPEED = 0.25F;

    private float lastPower;
    private float power;

    private final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "/textures/gui/sprites/container/power_background.png");
    private final Identifier FOREGROUND_TEXTURE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "container/power_foreground");

    public PowerWidget(int x, int y, int width, int height, float power) {
        super(x, y, width, height, Component.empty());
        this.power = power;
        this.lastPower = power;
    }

    public void setPower(float powerPercent) {
        this.power = powerPercent;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    protected void extractTooltipForNextRenderPass(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltipForNextRenderPass(graphics, mouseX, mouseY);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        float newPower = lastPower == 0.0F ? power : LERP_SPEED * lastPower + (1 - LERP_SPEED) * power;

        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, getX(), getY(), 0.0F, 0.0F, getWidth(), getHeight(), IMAGE_WIDTH, IMAGE_HEIGHT);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FOREGROUND_TEXTURE, getWidth(), getHeight(), 0, 0, getX(), getY(), (int)Math.ceil(IMAGE_WIDTH * newPower), IMAGE_HEIGHT);

        this.lastPower = newPower;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        return;
    }
}
