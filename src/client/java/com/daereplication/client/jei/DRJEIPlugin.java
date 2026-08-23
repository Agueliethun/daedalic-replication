package com.daereplication.client.jei;

import com.daereplication.DaedalicReplication;
import com.daereplication.blocks.DRBlocks;
import com.daereplication.client.DaedalicReplicationClient;
import com.daereplication.client.screen.ReplicatorScreen;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.recipe.ReplicationReplicateRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.fabricmc.fabric.impl.recipe.sync.SynchronizedRecipesImpl;
import net.minecraft.resources.Identifier;

import java.util.List;

@JeiPlugin
public class DRJEIPlugin implements IModPlugin {

    public static IJeiRuntime runtime;

    public static SynchronizedRecipes recipeMap = SynchronizedRecipesImpl.EMPTY;

    public static final IRecipeHolderType<ReplicationLearnRecipe> LEARN = IRecipeHolderType.create(DRRecipeTypes.REPLICATION_LEARN_RECIPE_TYPE);
    public static final IRecipeHolderType<ReplicationReplicateRecipe> REPLICATE = IRecipeHolderType.create(DRRecipeTypes.REPLICATION_REPLICATE_RECIPE_TYPE);

    public DRJEIPlugin() {
        ClientRecipeSynchronizedEvent.EVENT.register((client, recipes) -> recipeMap = recipes);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    @Override
    public void onRuntimeUnavailable() {
        runtime = null;
    }

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        
        registration.addRecipeCategories(new ReplicationLearnCategory(guiHelper, LEARN, DaedalicReplicationClient.LEARN_HINT_WIDTH, DaedalicReplicationClient.LEARN_HINT_HEIGHT));
        registration.addRecipeCategories(new ReplicationReplicateCategory(guiHelper, REPLICATE, DaedalicReplicationClient.REPLICATE_HINT_WIDTH, DaedalicReplicationClient.REPLICATE_HINT_HEIGHT));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(LEARN, List.copyOf(recipeMap.getAllOfType(DRRecipeTypes.REPLICATION_LEARN_RECIPE_TYPE)));
        registration.addRecipes(REPLICATE, List.copyOf(recipeMap.getAllOfType(DRRecipeTypes.REPLICATION_REPLICATE_RECIPE_TYPE)));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
                ReplicatorScreen.class,
                48,
                27,
                22,
                16,
                LEARN
        );

        registration.addRecipeClickArea(
                ReplicatorScreen.class,
                106,
                27,
                22,
                16,
                REPLICATE
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(LEARN, DRBlocks.REPLICATOR);
        registration.addCraftingStation(REPLICATE, DRBlocks.REPLICATOR);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(DRItemIds.REPLICATION_MODEL, new LearnerItemInterpreter());
    }
}
