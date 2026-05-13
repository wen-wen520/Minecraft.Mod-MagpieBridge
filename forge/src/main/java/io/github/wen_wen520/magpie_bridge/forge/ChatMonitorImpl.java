package io.github.wen_wen520.magpie_bridge.forge;

import java.util.UUID;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import io.github.wen_wen520.magpie_bridge.*;

@Mod.EventBusSubscriber(modid = "magpie_bridge", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChatMonitorImpl {

	public static void init() {
		Main.LOGGER.info("[Forge] ChatMonitorImpl initialized.");
	}

	@SubscribeEvent
	public static void onClientChatReceived(ClientChatReceivedEvent event) {

		if (!Utils.isNotificationOn()) {
			return;
		}

		if (event instanceof ClientChatReceivedEvent.Player playerEvent) {
			handlePlayerChat(playerEvent);
		}
		else if (Main.Settings.include_system && event instanceof ClientChatReceivedEvent.System systemEvent && !event.isCanceled()) {
			handleSystemChat(systemEvent);
		}
	}

	// Received Player Chat Message
	private static void handlePlayerChat(ClientChatReceivedEvent.Player event) {

		String rawName = event.getBoundChatType().name().getString();
		String rawBody = event.getMessage().getString();

		if (rawName.isEmpty()){
			rawName = "Unknown";
		}

		final UUID senderUUID = event.getPlayerChatMessage().sender();
		final String senderName = MessagePipeline.ClearNameStyle(rawName);
		final String messageBody = MessagePipeline.ClearBodyStyle(rawBody);

		SkinResource.getPlayerHead(senderUUID).whenComplete((path, throwable) -> {
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
	}

	// Received System Message
	private static void handleSystemChat(ClientChatReceivedEvent.System event) {

		String rawText = event.getMessage().getString();
		MessagePipeline.ProcessSystemMessage(rawText);

	}
}
