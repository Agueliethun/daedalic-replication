package com.daereplication;

import com.daereplication.blockentities.DRBlockEntities;
import com.daereplication.component.DRDataComponents;
import com.daereplication.blocks.DRBlocks;
import com.daereplication.items.DRItemIds;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.recipe.ReplicationReplicateRecipe;
import com.daereplication.registry.DRRegistries;
import com.daereplication.util.ReplicatorUtil;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaedalicReplication implements ModInitializer {
	public static final String MOD_ID = "daedalic-replication";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info(DaedalicReplication.MOD_ID + " onInitialize");

		ReplicationLearnRecipe.init();
		ReplicationReplicateRecipe.init();

		RecipeSynchronization.synchronizeRecipeSerializer(ReplicationLearnRecipe.SERIALIZER);
		RecipeSynchronization.synchronizeRecipeSerializer(ReplicationReplicateRecipe.SERIALIZER);

		DRDataComponents.init();
		DRItemIds.init();
		DRBlockEntities.init();
		DRBlocks.init();
		DRRecipeTypes.init();

		ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, DRDataComponents.REPLICATION_BLOCK_STORAGE);
		ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, DRDataComponents.GENERIC_ENERGY_STORAGE);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
