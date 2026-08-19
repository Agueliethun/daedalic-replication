package com.daereplication.client.jei;

import com.daereplication.blockitems.DRBlockItemIds;
import com.daereplication.blocks.DRBlocks;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.recipe.ReplicationReplicateRecipe;
import com.mojang.serialization.Codec;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
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

        builder.addInputSlot(80, 27)
                .setStandardSlotBackground()
                .add(learner);

        builder.addOutputSlot(138, 27)
                .setOutputSlotBackground()
                .add(recipe.getInput().value());

        builder.addInvisibleIngredients(RecipeIngredientRole.CRAFTING_STATION)
                .add(DRBlocks.REPLICATOR);
    }

    @Override
    public final Codec<RecipeHolder<ReplicationReplicateRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

}
