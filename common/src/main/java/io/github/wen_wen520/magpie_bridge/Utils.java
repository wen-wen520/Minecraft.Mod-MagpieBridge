package io.github.wen_wen520.magpie_bridge;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;
import dev.architectury.injectables.annotations.ExpectPlatform;

public final class Utils {

	// Game Environment
	@ExpectPlatform
	public static Path getGameDir() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Path getConfigDir() {
		throw new AssertionError();
	}

	public static String getPlayerName() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null) {
			return player.getGameProfile().getName();
		}
		return "Unknown";
	}

	public static boolean isForeground() {
		return Minecraft.getInstance().isWindowActive();
	}

	public static boolean isNotificationOn() {
		if (!Main.Settings.main_toggle) {
			return false;
		}

		if (Main.Settings.only_background && Utils.isForeground()) {
			return false;
		}

		return true;
	}

	// Files System
	public static Path loadFiles(String source, Path target) throws IOException {

		// Test if file is existed
		if (Files.exists(target)) {
			return target;
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

		return target;
	}

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
