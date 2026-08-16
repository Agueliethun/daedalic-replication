package com.daereplication.client.screen;

import com.daereplication.DaedalicReplication;
import com.daereplication.blockentities.ReplicatorBlockEntity;
import com.daereplication.client.widgets.PowerWidget;
import com.daereplication.menu.ReplicatorMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ReplicatorScreen extends AbstractContainerScreen<ReplicatorMenu> {

    private final Identifier PROGRESS_SPRITE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "container/dr_progress");
    private final Identifier CONTAINER_TEXTURE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "/textures/gui/container/replication.png");
    private final PowerWidget powerWidget;

    public ReplicatorScreen(ReplicatorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title.copy().withStyle(ChatFormatting.WHITE));

        powerWidget = new PowerWidget(this.leftPos + 27, this.topPos + 53, 122, 21, this.menu.getPowerPercent());
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);

        this.powerWidget.setPower(this.menu.getPowerPercent());

        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        float learnProgress = this.menu.getLearnProgress();
        float replicateProgress = this.menu.getReplicateProgress();

        if (learnProgress > 0.01) {
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    PROGRESS_SPRITE,
                    22,
                    16,
                    0,
                    0,
                    this.leftPos + 48,
                    this.topPos + 27,
                    Math.round(22 * learnProgress),
                    16
            );
        }

        if (replicateProgress > 0.01) {
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    PROGRESS_SPRITE,
                    22,
                    16,
                    0,
                    0,
                    this.leftPos + 106,
                    this.topPos + 27,
                    Math.round(22 * replicateProgress),
                    16
            );
        }
    }

    @Override
    protected void init() {
        super.init();

        powerWidget.setX(this.leftPos + 27);
        powerWidget.setY(this.topPos + 53);

        this.addRenderableWidget(powerWidget);
    }
}
