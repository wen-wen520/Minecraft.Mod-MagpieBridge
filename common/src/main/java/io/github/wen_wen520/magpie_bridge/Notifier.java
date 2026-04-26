package io.github.wen_wen520.magpie_bridge;

import net.minecraft.Util;

import java.io.IOException;

import static io.github.wen_wen520.magpie_bridge.Main.BRIDGE_WIN;

public final class Notifier {

    public static void send(GeneralMessage msg){
        var os = Util.getPlatform();

        if (os == Util.OS.WINDOWS) {
            sendWindows(msg);
        }
        else if (os == Util.OS.OSX) {
            sendMac(msg);
        }
    }

    public static void sendWindows(GeneralMessage message) {
        // Use Windows Bridge CLI to send message
        ProcessBuilder builder = new ProcessBuilder(
                BRIDGE_WIN.toAbsolutePath().toString(),
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
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    public static void sendMac(GeneralMessage message) {
        return;
    }

}
