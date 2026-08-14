package com.daereplication.blockitems;

import com.daereplication.DaedalicReplication;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public class DRBlockItemIds {
    private static BlockItemId create(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, name);
        return BlockItemId.create(id, id);
    }

    public static final BlockItemId REPLICATOR = create("replicator");
}
