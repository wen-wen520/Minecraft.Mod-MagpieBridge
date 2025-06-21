package wen_wen520.magpiebridge.client.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import wen_wen520.magpiebridge.client.utils.MessageParser;
import wen_wen520.magpiebridge.client.utils.PlayerHeadExporter;

public class Monitor implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Listen for chat messages
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, _temp) -> {

            String chatText = message.getString();
            MessageParser.ParsedMessage parsed = MessageParser.parse(chatText);
            String senderName = sender.getName();
            String headPath = "\"D:\\Program Low\\toast.png\"";

            try{
                PlayerHeadExporter.fetchAndSavePlayerHeadAsync(senderName).thenAccept(path -> {
                    WindowsNotificationHelper.sendNotification("[C]" + senderName, parsed.msg, path);
                });
            }
            catch (Exception e) {
                System.err.println("Failed to fetch player head for " + senderName + ": " + e.getMessage());
                WindowsNotificationHelper.sendNotification("[CE]" + senderName, parsed.msg, headPath);
            }

        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) ->{

            String chatText = message.getString();
            MessageParser.ParsedMessage parsed = MessageParser.parse(chatText);
            String senderName = parsed.sender;
            String headPath = "\"D:\\Program Low\\toast.png\"";

            if (parsed.length > 5){
                try{
                    PlayerHeadExporter.fetchAndSavePlayerHeadAsync(senderName).thenAccept(path -> {
                        WindowsNotificationHelper.sendNotification("[G]" + senderName, parsed.msg, path);
                    });
                }
                catch (Exception e) {
                    System.err.println("Failed to fetch player head for " + senderName + ": " + e.getMessage());
                    WindowsNotificationHelper.sendNotification("[GE]" + senderName, parsed.msg, headPath);
                }
            }
        });
    }
}