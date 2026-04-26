package io.github.wen_wen520.magpie_bridge.fabric;

import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class UtilsImpl {
	public static Path getGameDir() {
		return FabricLoader.getInstance().getGameDir();
	}

	public static Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}
}
