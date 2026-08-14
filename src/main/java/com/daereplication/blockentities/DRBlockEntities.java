package com.daereplication.blockentities;

import com.daereplication.DaedalicReplication;
import com.daereplication.blocks.DRBlocks;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;

public class DRBlockEntities {
    public static final BlockEntityType<ReplicatorBlockEntity> REPLICATOR_BLOCK_ENTITY =
            register("replicator", ReplicatorBlockEntity::new, DRBlocks.REPLICATOR);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void init() {
        EnergyStorage.SIDED.registerForBlockEntity((be, dir) -> be.energyStorage, REPLICATOR_BLOCK_ENTITY);
    }
}
