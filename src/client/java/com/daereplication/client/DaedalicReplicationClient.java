package com.daereplication.client;

import com.daereplication.DaedalicReplication;
import com.daereplication.client.screen.ReplicatorScreen;
import com.daereplication.menu.DRMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.Identifier;

public class DaedalicReplicationClient implements ClientModInitializer {

	public static final int LEARN_HINT_WIDTH = 96;
	public static final int LEARN_HINT_HEIGHT = 40;

	public static final int REPLICATE_HINT_WIDTH = 75;
	public static final int REPLICATE_HINT_HEIGHT = 39;

	@Override
	public void onInitializeClient() {
		MenuScreens.register(DRMenuTypes.REPLICATION_MACHINE, ReplicatorScreen::new);
	}
}