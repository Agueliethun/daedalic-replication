package com.daereplication.client.jei;

import com.daereplication.blockitems.DRBlockItemIds;
import com.daereplication.blocks.DRBlocks;
import com.daereplication.client.widgets.HintPowerWidget;
import com.daereplication.client.widgets.rei.ArrowWidget;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.recipe.ReplicationReplicateRecipe;
import com.mojang.serialization.Codec;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ReplicationReplicateCategory extends AbstractRecipeCategory<RecipeHolder<ReplicationReplicateRecipe>> {

    private HintPowerWidget powerWidget;
    private ArrowWidget arrowWidget;

    public ReplicationReplicateCategory(IGuiHelper guiHelper, IRecipeHolderType<ReplicationReplicateRecipe> recipeType, int width, int height) {
        super(
                recipeType,
                Component.translatable("daedalic-replication.gui.replicate.title"),
                guiHelper.createDrawableItemLike(DRBlocks.REPLICATOR.asItem()),
                width,
                height
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ReplicationReplicateRecipe> holder, IFocusGroup focuses) {
        ReplicationReplicateRecipe recipe = holder.value();

        ReplicationBlockStorage storage = new ReplicationBlockStorage(1, 1.0, recipe.getInput());
        ItemStackTemplate learner = new ItemStackTemplate(DRItemIds.REPLICATION_MODEL, DataComponentPatch.builder().set(DRDataComponents.REPLICATION_BLOCK_STORAGE, storage).build());

        builder.addInputSlot(2, 6)
                .setStandardSlotBackground()
                .add(learner);

        builder.addOutputSlot(53, 6)
                .setOutputSlotBackground()
                .add(recipe.getInput().value());

        builder.addInvisibleIngredients(RecipeIngredientRole.CRAFTING_STATION)
                .add(DRBlocks.REPLICATOR);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<ReplicationReplicateRecipe> recipe, IFocusGroup focuses) {
        powerWidget = new HintPowerWidget(7, 29);
        powerWidget.updatePower(recipe.value().getEnergy(), Math.max(recipe.value().getEnergy(), 500));
        builder.addDrawable(powerWidget);
        arrowWidget = new ArrowWidget(23, 7, recipe.value().getTime());
        builder.addDrawable(arrowWidget);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<ReplicationReplicateRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (powerWidget != null) {
            if (powerWidget.getTooltipWidget().containsMouse(mouseX, mouseY)) {
                tooltip.add(powerWidget.getTooltipComponent());
                return;
            }
        }

        if (arrowWidget != null) {
            if (arrowWidget.getTooltipWidget().containsMouse(mouseX, mouseY)) {
                tooltip.add(arrowWidget.getTooltipComponent());
            }
        }
    }

    @Override
    public final Codec<RecipeHolder<ReplicationReplicateRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

}
