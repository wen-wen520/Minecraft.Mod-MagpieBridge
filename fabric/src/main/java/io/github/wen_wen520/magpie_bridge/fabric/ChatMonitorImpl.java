package io.github.wen_wen520.magpie_bridge.fabric;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import io.github.wen_wen520.magpie_bridge.*;

public final class ChatMonitorImpl {

	private static void init() {
		onClientChatReceived();
		Main.LOGGER.info("[Fabric] ChatMonitorImpl initialized.");
	}

	public static void onClientChatReceived() {

		// Received Player Chat Message
		ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, _temp) -> {

			if (!Utils.isNotificationOn()) {
				return;
			}

			String rawName = sender.getName();
			String rawBody = message.getString();

			if (rawName.isEmpty()){
				rawName = "Unknown";
			}

			final String senderName = MessagePipeline.ClearNameStyle(rawName);
			final String messageBody = MessagePipeline.ClearBodyStyle(rawBody);

			SkinResource.getPlayerHead(senderName).whenComplete((path, throwable) -> {
				GeneralMessage.Builder builder = GeneralMessage.builder()
						.title(senderName)
						.body(messageBody)
						.icon(path);

				try {
					Notifier.send(builder.build());
				}
				catch (Exception notifyException) {
					Main.LOGGER.error("Failed to send notification for chat message from {}: {}", senderName, notifyException.getMessage());
				}
			});
		});

		// Received System Message
		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {

			if (overlay){
				return;
			}

			if (Utils.isNotificationOn() && Main.Settings.include_system) {
				return;
			}

			String rawText = message.getString();
			MessagePipeline.ProcessSystemMessage(rawText);

		});
	}
}
