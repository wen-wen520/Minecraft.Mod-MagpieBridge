package wen_wen520.magpiebridge.client.core;

import java.io.IOException;

import static wen_wen520.magpiebridge.client.utils.Initializer.dir_bridge;

public class WindowsNotificationHelper {


	public static void sendNotification(String app, String title, String message, String iconPath) {
		ProcessBuilder pb = new ProcessBuilder(
				dir_bridge.getAbsolutePath(),
				"-app-id", app,
				"-t", title,
				"-m", message,
				"-icon", iconPath,
				"-audio", "sms",
				"duration", "short"
		);
		try {
			pb.start();
		} catch (IOException e) {
			System.err.println("Failed to send notification: " + e.getMessage());
		}
	}
}