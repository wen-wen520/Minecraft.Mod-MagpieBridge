package wen_wen520.magpiebridge.client.core;

import java.io.IOException;

public class WindowsNotificationHelper {
    private static final String TOAST_PATH = "D:\\Program Low\\toast.exe";

    public static void sendNotification(String app, String title, String message, String iconPath) {
        ProcessBuilder pb = new ProcessBuilder(
                TOAST_PATH,
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