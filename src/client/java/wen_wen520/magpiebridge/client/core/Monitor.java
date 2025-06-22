package wen_wen520.magpiebridge.client.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import wen_wen520.magpiebridge.client.utils.MessageParser;
import wen_wen520.magpiebridge.client.utils.PlayerHeadExporter;
import wen_wen520.magpiebridge.client.utils.filter.GeneralFilter;
import wen_wen520.magpiebridge.client.utils.filter.SkyBlockMsgFilter;
import wen_wen520.magpiebridge.client.utils.filter.ZombieMsgFilter;

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
                    WindowsNotificationHelper.sendNotification("Minecraft", "[C]" + senderName, parsed.msg, path);
                });
            }
            catch (Exception e) {
                System.err.println("Failed to fetch player head for " + senderName + ": " + e.getMessage());
                WindowsNotificationHelper.sendNotification("Minecraft", "[CE]" + senderName, parsed.msg, headPath);
            }

        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) ->{

            if (overlay) {
                return;
            }

            String headPath = "\"D:\\Program Low\\toast.png\"";
            String chatText = GeneralFilter.filterMessage(message.getString());
            String[] NotificationMsg = null;

            // Skyblock
            if (NotificationMsg == null) {
                NotificationMsg = SkyBlockMsgFilter.filterMessage(chatText);
            }
            // Zombie
            if (NotificationMsg == null){
                NotificationMsg = ZombieMsgFilter.filterMessage(chatText);
            }
            if (NotificationMsg == null) {
                return;
            }

            try{
                String[] finalNotificationMsg = NotificationMsg;
                PlayerHeadExporter.fetchAndSavePlayerHeadAsync(NotificationMsg[1]).thenAccept(path -> {
                    WindowsNotificationHelper.sendNotification(finalNotificationMsg[0],"[G]" + finalNotificationMsg[1], finalNotificationMsg[2], path);
                });
            }
            catch (Exception e) {
                System.err.println("Failed to fetch player head for " + NotificationMsg[1] + ": " + e.getMessage());
                WindowsNotificationHelper.sendNotification(NotificationMsg[0],"[GE]" + NotificationMsg[1], NotificationMsg[2], headPath);
            }

        });
    }
}