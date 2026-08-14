package com.daereplication.menu;

import com.daereplication.DaedalicReplication;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class DRMenuTypes {

    public static MenuType<ReplicatorMenu> REPLICATOR = register("replicator", ReplicatorMenu::new);

    private static <T extends AbstractContainerMenu> MenuType<T> register(final String name, final MenuType.MenuSupplier<T> constructor) {
        return Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, name), new MenuType<>(constructor, FeatureFlagSet.of()));
    }
}
