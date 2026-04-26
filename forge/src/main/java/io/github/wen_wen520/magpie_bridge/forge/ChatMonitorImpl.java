package io.github.wen_wen520.magpie_bridge.forge;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import io.github.wen_wen520.magpie_bridge.GeneralMessage;
import io.github.wen_wen520.magpie_bridge.MessagePipeline;
import io.github.wen_wen520.magpie_bridge.Notifier;
import io.github.wen_wen520.magpie_bridge.utils.SkinResource;


@Mod.EventBusSubscriber(modid = "magpie_bridge", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChatMonitorImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger("MagpieBridge");

	public static void init() {
		LOGGER.info("ChatMonitorImpl initialized.");
	}

	@SubscribeEvent
	public static void onClientChatReceived(ClientChatReceivedEvent event) {
		if (event instanceof ClientChatReceivedEvent.Player playerEvent) {
			LOGGER.info("Received player chat: {}", playerEvent.getMessage().getString());
			handlePlayerChat(playerEvent);
		}
		else if (event instanceof ClientChatReceivedEvent.System systemEvent) {
			LOGGER.info("Received System chat: {}", systemEvent.getMessage());
			handleSystemChat(systemEvent);
		}
	}

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
				LOGGER.info("{}: {} has been sent to the server.", senderName, messageBody);
			}
			catch (Exception notifyException) {
				LOGGER.error("Failed to send notification for {}: {}", senderName, messageBody, notifyException);
			}
		});
	}

	private static void handleSystemChat(ClientChatReceivedEvent.System event) {

		if (event.isOverlay()) {
			return;
		}

		String rawText = event.getMessage().getString();

		// TODO: handle non-overlay system messages.
		LOGGER.debug("Received system chat (currently ignored): {}", event.getMessage().getString());
	}
}
