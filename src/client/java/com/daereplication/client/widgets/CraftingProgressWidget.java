package com.daereplication.client.widgets;

import com.daereplication.client.jei.DRJEIPlugin;
import me.shedaniel.rei.api.client.REIRuntime;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public abstract class CraftingProgressWidget extends AbstractWidget {

    private final Identifier spriteID;
    private float progress;
    private final IRecipeType<?> recipeType;

    public CraftingProgressWidget(int x, int y, int width, int height, Identifier spriteID, IRecipeType<?> recipeType) {
        super(x, y, width, height, Component.empty());
        this.spriteID = spriteID;
        this.recipeType = recipeType;
    }

    public void updateProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    protected void extractTooltipForNextRenderPass(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltipForNextRenderPass(graphics, mouseX, mouseY);

        this.setTooltip(Tooltip.create(Component.translatable("deadalic-replication.gui.recipes.seeAll")));
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, spriteID, getWidth(), getHeight(), 0, 0, getX(), getY(), (int)Math.ceil(width * progress), height);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        return;
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        showRecipesJEI();
    }

    public void showRecipesJEI() {
        if (DRJEIPlugin.runtime != null) {
            DRJEIPlugin.runtime.getRecipesGui().showTypes(List.of(recipeType));;
        }

        REIRuntime runtime = REIRuntime.getInstance();
        if (runtime != null) {
//            runtime.
        }
    }
}
