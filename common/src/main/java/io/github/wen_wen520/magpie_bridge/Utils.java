package io.github.wen_wen520.magpie_bridge;

import java.io.*;
import java.nio.file.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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

}
