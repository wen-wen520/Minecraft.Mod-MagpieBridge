package io.github.wen_wen520.magpie_bridge;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import net.minecraft.client.Minecraft;

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

	public static boolean isForeground() {
		return Minecraft.getInstance().isWindowActive();
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
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

			// Go through files first
			@Override
			@NotNull
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			// Then go through directories
			@Override
			@NotNull
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if (exc != null) throw exc;
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

			// Handle errors
			@Override
			@NotNull
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				System.err.println("Not able to delete: " + file + ", due to" + exc.getMessage());
				return FileVisitResult.TERMINATE;
			}
		});
	}
}
