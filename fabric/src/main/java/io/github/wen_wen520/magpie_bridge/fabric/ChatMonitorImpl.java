package io.github.wen_wen520.magpie_bridge.fabric;

import io.github.wen_wen520.magpie_bridge.GeneralMessage;
import io.github.wen_wen520.magpie_bridge.MessagePipeline;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

import me.shedaniel.autoconfig.AutoConfig;

import io.github.wen_wen520.magpie_bridge.Notifier;
import io.github.wen_wen520.magpie_bridge.settings.MainSettings;
import io.github.wen_wen520.magpie_bridge.utils.SkinResource;

public final class ChatMonitorImpl {

	private static void init() {
		onClientChatReceived();
	}

	public static void onClientChatReceived() {

		ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, _temp) -> {

			MainSettings config = AutoConfig.getConfigHolder(MainSettings.class).getConfig();

			String sender_name;
			String message_body = MessagePipeline.ClearBodyStyle(message.getString());

			if (sender == null) {
				sender_name = "Unknown";
			}
			else {
				sender_name = sender.getName();
			}

		try {
			SkinResource.getPlayerHead(sender_name).thenAccept(path ->
					Notifier.send(GeneralMessage.builder()
						.title(sender_name)
						.body(message_body)
						.icon(path)
						.build())
			);
		}
		catch (Exception e) {
			System.err.println("Failed to fetch player head for " + sender_name + ": " + e.getMessage());
			Notifier.send(GeneralMessage.builder()
				.title(sender_name)
				.body(message_body)
				.build());
		}
		});

		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
					if (overlay){
						return;
					}

					//todo: use message pipeline
				}

		);
	}
}
