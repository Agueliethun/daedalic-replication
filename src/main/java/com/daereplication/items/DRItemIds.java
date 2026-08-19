package com.daereplication.items;

import com.daereplication.DaedalicReplication;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.function.Function;

public class DRItemIds {
    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> creativeTab.accept(REPLICATION_MODEL));
    }

    public static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, name));
    }

    public static Item register(ResourceKey<Item> itemResourceKey, Function<Item.Properties, Item> itemFactory, Item.Properties itemProperties) {
        Item item = itemFactory.apply(itemProperties.setId(itemResourceKey));
        Registry.register(BuiltInRegistries.ITEM, itemResourceKey, item);
        return item;
    }

    public static final ResourceKey<Item> REPLICATION_MODEL_RESOURCE_KEY = create("replication_model");

    private static final Function<Item.Properties, Item> REPLICATION_MODEL_FACTORY = (properties ->
            new ReplicationModel(
                    properties
                            .delayedComponent(DRDataComponents.REPLICATION_BLOCK_STORAGE, a ->
                                    new ReplicationBlockStorage(0, 0.0, null))
                            .delayedComponent(DataComponents.ENCHANTABLE, a -> new Enchantable(100))
                            .delayedComponent(DataComponents.ENCHANTMENTS, a -> ItemEnchantments.EMPTY)
            )
    );

    public static final Item REPLICATION_MODEL = register(REPLICATION_MODEL_RESOURCE_KEY, REPLICATION_MODEL_FACTORY, new Item.Properties());
}
