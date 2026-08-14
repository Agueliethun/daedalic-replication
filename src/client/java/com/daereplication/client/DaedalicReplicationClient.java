package com.daereplication.client;

import com.daereplication.client.screen.ReplicatorScreen;
import com.daereplication.menu.DRMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class DaedalicReplicationClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(DRMenuTypes.REPLICATOR, ReplicatorScreen::new);
	}
}