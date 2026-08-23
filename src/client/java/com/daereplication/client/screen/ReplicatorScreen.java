package com.daereplication.client.screen;

import com.daereplication.DaedalicReplication;
import com.daereplication.blockentities.ReplicatorBlockEntity;
import com.daereplication.client.jei.DRJEIPlugin;
import com.daereplication.client.widgets.PowerWidget;
import com.daereplication.client.widgets.StandardProgressWidget;
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

    private final Identifier CONTAINER_TEXTURE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "/textures/gui/container/replication.png");
    private final PowerWidget powerWidget;
    private final StandardProgressWidget learnProgressWidget;
    private final StandardProgressWidget replicateProgressWidget;

    public ReplicatorScreen(ReplicatorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title.copy().withStyle(ChatFormatting.WHITE), 207, 166);
        powerWidget = new PowerWidget(this.leftPos + 27, this.topPos + 53, 122, 21);
        learnProgressWidget = new StandardProgressWidget(this.leftPos + 48, this.topPos + 27, DRJEIPlugin.LEARN);
        replicateProgressWidget = new StandardProgressWidget(this.leftPos + 106, this.topPos + 27, DRJEIPlugin.REPLICATE);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);

        this.powerWidget.updatePower(this.menu.getDisplayPower(), this.menu.getDisplayPowerMax());
        this.learnProgressWidget.updateProgress(this.menu.getLearnProgress());
        this.replicateProgressWidget.updateProgress(this.menu.getReplicateProgress());

        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }

    @Override
    protected void init() {
        super.init();

        powerWidget.setX(this.leftPos + 27);
        powerWidget.setY(this.topPos + 53);
        this.addRenderableWidget(powerWidget);

        learnProgressWidget.setX(this.leftPos + 48);
        learnProgressWidget.setY(this.topPos + 27);
        this.addRenderableWidget(learnProgressWidget);

        replicateProgressWidget.setX(this.leftPos + 106);
        replicateProgressWidget.setY(this.topPos + 27);
        this.addRenderableWidget(replicateProgressWidget);
    }
}
