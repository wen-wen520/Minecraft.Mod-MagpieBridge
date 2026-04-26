package io.github.wen_wen520.magpie_bridge.resource;

import java.io.IOException;
import java.nio.file.Files;

import net.minecraft.Util;

import io.github.wen_wen520.magpie_bridge.Main;
import io.github.wen_wen520.magpie_bridge.Utils;


public final class LoadResource {

	// Initialization
	public static void init() {
		try {
			Files.createDirectories(Main.CONFIG_DIR);
			Files.createDirectories(Main.CACHE_HEADS_DIR);
			Files.createDirectories(Main.DEFAULT_HEAD_DIR);
			Files.createDirectories(Main.BRIDGE_DIR);
			Utils.loadFiles("/assets/default_player_head.png", Main.DEFAULT_HEAD);
			initBridge();
		} catch (IOException e) {
			throw new RuntimeException("[Error] Failed to initiate resources" + e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread(LoadResource::clearCache));
	}

	// Clear up whole config dictionary
	public static void clear(){
		try {
			Utils.deleteFiles(Main.CONFIG_DIR);
			Files.createDirectories(Main.CONFIG_DIR);
		}
		catch (IOException e) {
			throw new RuntimeException("[Error] Failed to clear resources" + e);
		}
	}

	public static void reload(){
		clear();
		init();
	}

	// Clear cached head icons
	public static void clearCache() {
		try {
			Utils.deleteFiles(Main.CACHE_HEADS_DIR);
			Files.createDirectories(Main.CACHE_HEADS_DIR);
		} catch (IOException e) {
			throw new RuntimeException("[Error] Failed to clear cache" + e);
		}
	}

	// Helpers

	// Load the bridge to targeted platform
	private static void initBridge() throws IOException, RuntimeException {
		if (Util.getPlatform() == Util.OS.WINDOWS) {
			Utils.loadFiles("/assets/toast.exe", Main.BRIDGE_WIN);
		}
		else if (Util.getPlatform() == Util.OS.OSX) {
			throw new RuntimeException("[Error] Mac OS is not supported");
		}
		else if (Util.getPlatform() == Util.OS.LINUX) {
			throw new RuntimeException("[Error] LINUX is not supported");
		}
		else {
			throw new RuntimeException("[Error] Current operating system is not supported");
		}
	}
}
