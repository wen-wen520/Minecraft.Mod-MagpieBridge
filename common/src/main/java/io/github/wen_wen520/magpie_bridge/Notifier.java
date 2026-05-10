package io.github.wen_wen520.magpie_bridge;

import java.io.IOException;
import net.minecraft.Util;

public final class Notifier {

	private static final String playerName = Utils.getPlayerName();

	public static void send(GeneralMessage msg){

		if (Main.Settings.only_others && msg.title.equals(playerName)) {
			return;
		}

		if (Main.OS == Util.OS.WINDOWS) {
			sendWindows(msg);
		}
		else if (Main.OS == Util.OS.OSX) {
			sendMac(msg);
		}
		else if (Main.OS == Util.OS.LINUX){
			sendLinux(msg);
		}
		else {
			Main.LOGGER.error("Failed to send notification: Unsupported OS");
		}
	}

	private static void sendWindows(GeneralMessage message) {

		// Use Windows Bridge CLI to send message
		ProcessBuilder builder = new ProcessBuilder(
				Main.BRIDGE_WIN.toAbsolutePath().toString(),
				"-app-id", message.app,
				"-title", message.title,
				"-message", message.body,
				"-icon", message.icon.toAbsolutePath().toString(),
				"-audio", "sms",
				"-duration", "short"
		);

		try {
			builder.start();
		}
		catch (IOException e) {
			Main.LOGGER.error("[Windows] Failed to send notification {}: {}", message.title, e.getMessage());
		}
	}

	private static void sendMac(GeneralMessage message) {
		Main.LOGGER.error("[Mac] Failed to send notification {}: {}", message.title, message.body);
	}

	private static void sendLinux(GeneralMessage message) {
		Main.LOGGER.error("[Linux] Failed to send notification {}: {}", message.title, message.body);
	}
}
