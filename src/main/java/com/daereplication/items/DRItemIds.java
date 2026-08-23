package com.daereplication.items;

import com.daereplication.DaedalicReplication;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.component.UpgradeStorage;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.function.Function;

public class DRItemIds {
    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> {
                    creativeTab.accept(REPLICATION_MODEL);
                    creativeTab.accept(REPLICATION_CORE);
                    creativeTab.accept(UPGRADE);
                    creativeTab.accept(UPGRADE_MASTER);
                    creativeTab.accept(UPGRADE_EFFICIENCY);
                    creativeTab.accept(UPGRADE_EFFICIENCY_SMALL);
                    creativeTab.accept(UPGRADE_SPEED);
                    creativeTab.accept(UPGRADE_SPEED_SMALL);
                    creativeTab.accept(UPGRADE_LEARN);
                });
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

    private static Function<Item.Properties, Item> makeUpgradeFactory(UpgradeItem.SlotType type, int learn, int efficiency, int speed) {
        return properties -> new UpgradeItem(
                properties.delayedComponent(DRDataComponents.UPGRADE_STORAGE, a -> new UpgradeStorage(learn, efficiency, speed)),
                type
        );
    }

    public static final ResourceKey<Item> REPLICATION_CORE_RESOURCE_KEY = create("replication_core");
    public static final Item REPLICATION_CORE = register(REPLICATION_CORE_RESOURCE_KEY, Item::new, new Item.Properties());

    public static final ResourceKey<Item> UPGRADE_RESOURCE_KEY = create("upgrade");
    public static final Item UPGRADE = register(UPGRADE_RESOURCE_KEY, makeUpgradeFactory(UpgradeItem.SlotType.SLOT_NONE, 0, 0, 0), new Item.Properties());

    public static final ResourceKey<Item> UPGRADE_MASTER_RESOURCE_KEY = create("upgrade_master");
    public static final Item UPGRADE_MASTER = register(UPGRADE_MASTER_RESOURCE_KEY, makeUpgradeFactory(UpgradeItem.SlotType.SLOT_MAIN, 0, 1, 1), new Item.Properties());

    public static final ResourceKey<Item> UPGRADE_SPEED_SMALL_RESOURCE_KEY = create("upgrade_speed_small");
    public static final Item UPGRADE_SPEED_SMALL = register(UPGRADE_SPEED_SMALL_RESOURCE_KEY, makeUpgradeFactory(UpgradeItem.SlotType.SLOT_SECONDARY, 0, 0, 1), new Item.Properties());

    public static final ResourceKey<Item> UPGRADE_SPEED_RESOURCE_KEY = create("upgrade_speed");
    public static final Item UPGRADE_SPEED = register(UPGRADE_SPEED_RESOURCE_KEY, makeUpgradeFactory(UpgradeItem.SlotType.SLOT_SECONDARY, 0, -1, 2), new Item.Properties());

    public static final ResourceKey<Item> UPGRADE_EFFICIENCY_SMALL_RESOURCE_KEY = create("upgrade_efficiency_small");
    public static final Item UPGRADE_EFFICIENCY_SMALL = register(UPGRADE_EFFICIENCY_SMALL_RESOURCE_KEY, makeUpgradeFactory(UpgradeItem.SlotType.SLOT_SECONDARY, 0, 1, 0), new Item.Properties());

    public static final ResourceKey<Item> UPGRADE_EFFICIENCY_RESOURCE_KEY = create("upgrade_efficiency");
    public static final Item UPGRADE_EFFICIENCY = register(UPGRADE_EFFICIENCY_RESOURCE_KEY, makeUpgradeFactory(UpgradeItem.SlotType.SLOT_SECONDARY, 0, 2, -1), new Item.Properties());

    public static final ResourceKey<Item> UPGRADE_LEARN_RESOURCE_KEY = create("upgrade_learn");
    public static final Item UPGRADE_LEARN = register(UPGRADE_LEARN_RESOURCE_KEY, makeUpgradeFactory(UpgradeItem.SlotType.SLOT_SECONDARY, 1, 0, 0), new Item.Properties());
}
