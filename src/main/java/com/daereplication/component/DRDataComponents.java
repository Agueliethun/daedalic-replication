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
}
