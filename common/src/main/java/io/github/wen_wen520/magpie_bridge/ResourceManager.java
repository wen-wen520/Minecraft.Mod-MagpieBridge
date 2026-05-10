package io.github.wen_wen520.magpie_bridge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import org.jetbrains.annotations.NotNull;
import net.minecraft.Util;

public final class ResourceManager {

	// Main
	// Initialization
	public static void init() {
		try {
			Files.createDirectories(Main.CONFIG_DIR);
			Files.createDirectories(Main.CACHE_HEADS_DIR);
			Files.createDirectories(Main.DEFAULT_HEAD_DIR);
			Files.createDirectories(Main.BRIDGE_DIR);
			loadFiles("/assets/magpie_bridge/default_player_head.png", Main.DEFAULT_HEAD);
			initBridge();
		} catch (IOException e) {
			throw new RuntimeException("[Error] Failed to initiate resources" + e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread(ResourceManager::clearCache));
	}

	// Reload Configs
	public static void reload(){
		clearConfig();
		init();
	}

	// Clear cached head icons
	public static void clearCache() {
		try {
			deleteFiles(Main.CACHE_HEADS_DIR);
			Files.createDirectories(Main.CACHE_HEADS_DIR);
		} catch (IOException e) {
			throw new RuntimeException("[Error] Failed to clear cache" + e);
		}
	}

	// Clear up whole config dictionary
	public static void clearConfig(){
		try {
			deleteFiles(Main.CONFIG_DIR);
			Files.createDirectories(Main.CONFIG_DIR);
		}
		catch (IOException e) {
			throw new RuntimeException("[Error] Failed to clear resources" + e);
		}
	}


	// Helpers
	// Load the bridge to targeted platform
	private static void initBridge() throws IOException, RuntimeException {
		if (Main.OS == Util.OS.WINDOWS) {
			loadFiles("/assets/magpie_bridge/toast.exe", Main.BRIDGE_WIN);
		}
		else if (Main.OS == Util.OS.OSX) {
			throw new RuntimeException("[Error] Mac OS is not supported");
		}
		else if (Main.OS == Util.OS.LINUX) {
			throw new RuntimeException("[Error] LINUX is not supported");
		}
		else {
			throw new RuntimeException("[Error] Current operating system is not supported");
		}
	}


	// Files System
	// Load files from .jar pack
	public static void loadFiles(String source, Path target) throws IOException {

		// Test if file is existed
		if (Files.exists(target)) {
			return;
		}

		// Auto create root dir
		if (target.getParent() != null) {
			Files.createDirectories(target.getParent());
		}

		// Get input stream from Jar resource, and check if it's null (not found)
		try (InputStream in = Utils.class.getResourceAsStream(source)) {
			if (in == null) {
				throw new FileNotFoundException("[Error] Unable to find the resource" + source);
			}

			Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
		}

	}

	// Delete files
	public static void deleteFiles(Path root) throws IOException {
		if (Files.notExists(root)) return;

		// Initialize file visitor
		Files.walkFileTree(root, new SimpleFileVisitor<>() {

			// Go through files first
			@Override
			@NotNull
			public FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			// Then go through directories
			@Override
			@NotNull
			public FileVisitResult postVisitDirectory(@NotNull Path dir, IOException exc) throws IOException {
				if (exc != null) throw exc;
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

			// Handle errors
			@Override
			@NotNull
			public FileVisitResult visitFileFailed(@NotNull Path file, @NotNull IOException exc) {
				System.err.println("Not able to delete: " + file + ", due to" + exc.getMessage());
				return FileVisitResult.TERMINATE;
			}
		});
	}
}
