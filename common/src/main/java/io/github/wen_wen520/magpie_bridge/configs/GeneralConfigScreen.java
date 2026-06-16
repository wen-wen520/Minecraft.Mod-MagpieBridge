package io.github.wen_wen520.magpie_bridge.configs;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GeneralConfigScreen {

	public static Screen create(Screen parent) {
		return YetAnotherConfigLib.create(GeneralConfig.HANDLER, (defaults, config, builder) -> builder
				.title(Component.translatable("wen_wen520.magpie_bridge.config.general.title"))
				.category(ConfigCategory.createBuilder()
						.name(Component.translatable("wen_wen520.magpie_bridge.config.general.category"))

						// Option 1: main_toggle
						.option(Option.<Boolean>createBuilder()
								.name(Component.translatable("wen_wen520.magpie_bridge.config.general.main_toggle.name"))
								.description(OptionDescription.of(Component.translatable("wen_wen520.magpie_bridge.config.general.main_toggle.description")))
								.binding(defaults.main_toggle, () -> config.main_toggle, v -> config.main_toggle = v)
								.controller(TickBoxControllerBuilder::create)
								.build())

						// Option 2: only_others
						.option(Option.<Boolean>createBuilder()
								.name(Component.translatable("wen_wen520.magpie_bridge.config.general.only_others.name"))
								.description(OptionDescription.of(Component.translatable("wen_wen520.magpie_bridge.config.general.only_others.description")))
								.binding(defaults.only_others, () -> config.only_others, v -> config.only_others = v)
								.controller(TickBoxControllerBuilder::create)
								.build())

						// Option 3: only_background
						.option(Option.<Boolean>createBuilder()
								.name(Component.translatable("wen_wen520.magpie_bridge.config.general.only_background.name"))
								.description(OptionDescription.of(Component.translatable("wen_wen520.magpie_bridge.config.general.only_background.description")))
								.binding(defaults.only_background, () -> config.only_background, v -> config.only_background = v)
								.controller(TickBoxControllerBuilder::create)
								.build())

						// Option 4: include_system
						.option(Option.<Boolean>createBuilder()
								.name(Component.translatable("wen_wen520.magpie_bridge.config.general.include_system.name"))
								.description(OptionDescription.of(Component.translatable("wen_wen520.magpie_bridge.config.general.include_system.description")))
								.binding(defaults.include_system, () -> config.include_system, v -> config.include_system = v)
								.controller(TickBoxControllerBuilder::create)
								.build())

						.build())
		).generateScreen(parent);
	}
}
