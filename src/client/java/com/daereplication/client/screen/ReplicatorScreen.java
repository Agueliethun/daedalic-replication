package com.daereplication.client.screen;

import com.daereplication.DaedalicReplication;
import com.daereplication.blockentities.ReplicatorBlockEntity;
import com.daereplication.menu.ReplicatorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ReplicatorScreen extends AbstractContainerScreen<ReplicatorMenu> {

    private final Identifier CONTAINER_TEXTURE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "/textures/gui/container/replication.png");

    public ReplicatorScreen(ReplicatorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        Slot inputSlot = this.getMenu().slots.get(ReplicatorBlockEntity.SLOT_INPUT);
        graphics.text(this.font, Component.translatable("menu.daedalic-replication.replicator.screen.input"), inputSlot.x, inputSlot.y - this.font.lineHeight - 8, 0xFFFFFFFF, true);
    }

    @Override
    protected void init() {
        super.init();
    }
}
