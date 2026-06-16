package io.github.wen_wen520.magpie_bridge.forge;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import io.github.wen_wen520.magpie_bridge.Main;
import io.github.wen_wen520.magpie_bridge.configs.GeneralConfigScreen;

@Mod(Main.MOD_ID)
public final class MainForge {

	public MainForge() {

		Main.init();

		// Register Settings Entry Point
		ModLoadingContext.get().registerExtensionPoint(
				ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) ->
						GeneralConfigScreen.create(parent)
				)
		);
	}

	public static void LoadResource() {}

}
