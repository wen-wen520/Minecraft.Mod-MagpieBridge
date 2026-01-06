package wen_wen520.magpiebridge.client.core;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import wen_wen520.magpiebridge.MagpieBridgeConfig;
import wen_wen520.magpiebridge.Magpiebridge;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class Command {

	private static final String ARG_ENABLED = "enabled";

	private Command() {
	}

	public static void register() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(createNamespacedConfigCommand());
			dispatcher.register(ClientCommandManager.literal(Magpiebridge.MOD_ID)
				.then(createConfigLiteral()));
		});
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> createNamespacedConfigCommand() {
		LiteralArgumentBuilder<FabricClientCommandSource> root = ClientCommandManager.literal(Magpiebridge.MOD_ID + ":config");
		attachConfigSubCommands(root);
		return root;
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> createConfigLiteral() {
		LiteralArgumentBuilder<FabricClientCommandSource> configLiteral = ClientCommandManager.literal("config");
		attachConfigSubCommands(configLiteral);
		return configLiteral;
	}

	private static void attachConfigSubCommands(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
		builder.then(createGeneralNode());
		builder.then(createNotificationsNode());
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> createGeneralNode() {
		LiteralArgumentBuilder<FabricClientCommandSource> general = ClientCommandManager.literal("general");
		general.then(createToggleNode(
			"keepConfigDir",
			config -> config.keepConfigDir,
			(config, value) -> config.keepConfigDir = value,
			"command.magpiebridge.label.keepConfigDir"
		));
		general.then(createToggleNode(
			"desktopNotifications",
			config -> config.notificationsEnabled,
			(config, value) -> config.notificationsEnabled = value,
			"command.magpiebridge.label.desktopNotifications"
		));
		return general;
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> createNotificationsNode() {
		LiteralArgumentBuilder<FabricClientCommandSource> notifications = ClientCommandManager.literal("notifications");
		LiteralArgumentBuilder<FabricClientCommandSource> skyblock = ClientCommandManager.literal("skyblock");

		skyblock.then(createToggleNode(
			"player",
			config -> config.playerNotifications,
			(config, value) -> config.playerNotifications = value,
			"command.magpiebridge.label.skyblock.player"
		));
		skyblock.then(createToggleNode(
			"bazaar",
			config -> config.bazaarNotifications,
			(config, value) -> config.bazaarNotifications = value,
			"command.magpiebridge.label.skyblock.bazaar"
		));
		skyblock.then(createToggleNode(
			"auction",
			config -> config.auctionNotifications,
			(config, value) -> config.auctionNotifications = value,
			"command.magpiebridge.label.skyblock.auction"
		));

		notifications.then(skyblock);
		return notifications;
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> createToggleNode(
			String literal,
			Function<MagpieBridgeConfig, Boolean> getter,
			BiConsumer<MagpieBridgeConfig, Boolean> setter,
			String labelTranslationKey
	) {
		LiteralArgumentBuilder<FabricClientCommandSource> node = ClientCommandManager.literal(literal);

		node.executes(ctx -> {
			boolean current = getter.apply(MagpieBridgeConfig.get());
			ctx.getSource().sendFeedback(buildStatusMessage(labelTranslationKey, current));
			return current ? 1 : 0;
		});

		node.then(ClientCommandManager.argument(ARG_ENABLED, BoolArgumentType.bool())
			.executes(ctx -> {
				boolean enabled = BoolArgumentType.getBool(ctx, ARG_ENABLED);
				MagpieBridgeConfig config = MagpieBridgeConfig.get();
				setter.accept(config, enabled);
				MagpieBridgeConfig.save();
				ctx.getSource().sendFeedback(buildUpdateMessage(labelTranslationKey, enabled));
				return 1;
			}));

		return node;
	}

	private static Text buildStatusMessage(String labelTranslationKey, boolean value) {
		String key = value
			? "command.magpiebridge.message.status.enabled"
			: "command.magpiebridge.message.status.disabled";
		return Text.translatable(key, Text.translatable(labelTranslationKey));
	}

	private static Text buildUpdateMessage(String labelTranslationKey, boolean value) {
		String key = value
			? "command.magpiebridge.message.update.enabled"
			: "command.magpiebridge.message.update.disabled";
		return Text.translatable(key, Text.translatable(labelTranslationKey));
	}
}
