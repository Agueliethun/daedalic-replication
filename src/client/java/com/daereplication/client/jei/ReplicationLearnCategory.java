package com.daereplication.client.jei;

import com.daereplication.blockentities.DRBlockEntities;
import com.daereplication.blockitems.DRBlockItemIds;
import com.daereplication.blocks.DRBlocks;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.util.ReplicatorUtil;
import com.mojang.serialization.Codec;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ReplicationLearnCategory extends AbstractRecipeCategory<RecipeHolder<ReplicationLearnRecipe>> {

    public ReplicationLearnCategory(IGuiHelper guiHelper, IRecipeHolderType<ReplicationLearnRecipe> recipeType, int width, int height) {
        super(
                recipeType,
                Component.translatable("daedalic-replication.gui.learn.title"),
                guiHelper.createDrawableItemLike(DRItemIds.REPLICATION_MODEL),
                width,
                height
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ReplicationLearnRecipe> holder, IFocusGroup focuses) {
        ReplicationLearnRecipe recipe = holder.value();

        builder.addInputSlot(22, 27)
                .setStandardSlotBackground()
                .add(recipe.getInput());

        builder.addInputSlot(80, 27)
                .setStandardSlotBackground()
                .add(DRItemIds.REPLICATION_MODEL);

        ReplicationBlockStorage storage = new ReplicationBlockStorage(1, 1.0, recipe.getOutput().typeHolder());
        builder.addOutputSlot(138, 27)
                .setOutputSlotBackground()
                .add(new ItemStackTemplate(DRItemIds.REPLICATION_MODEL, DataComponentPatch.builder().set(DRDataComponents.REPLICATION_BLOCK_STORAGE, storage).build()));

        builder.addInvisibleIngredients(RecipeIngredientRole.CRAFTING_STATION)
                .add(DRBlocks.REPLICATOR);
    }

    @Override
    public final Codec<RecipeHolder<ReplicationLearnRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

}
