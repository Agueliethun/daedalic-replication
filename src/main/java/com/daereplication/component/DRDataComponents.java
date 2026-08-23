package com.daereplication.component;

import com.daereplication.DaedalicReplication;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class DRDataComponents {
    public static void init() {
        DaedalicReplication.LOGGER.info("Registering {} components", DaedalicReplication.MOD_ID);
    }

    public static final DataComponentType<ReplicationBlockStorage> REPLICATION_BLOCK_STORAGE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "replication_block_storage"),
            DataComponentType.<ReplicationBlockStorage>builder().persistent(ReplicationBlockStorage.CODEC).build()
    );

    public static final DataComponentType<GenericEnergyStorage> GENERIC_ENERGY_STORAGE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "generic_energy_storage"),
            DataComponentType.<GenericEnergyStorage>builder().persistent(GenericEnergyStorage.CODEC).build()
    );

    public static final DataComponentType<UpgradeStorage> UPGRADE_STORAGE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "upgrade_storage"),
            DataComponentType.<UpgradeStorage>builder().persistent(UpgradeStorage.CODEC).build()
    );
}
