package io.github.wen_wen520.magpie_bridge.configs;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import io.github.wen_wen520.magpie_bridge.Main;
import net.minecraft.resources.ResourceLocation;


public class GeneralConfig {
	public static ConfigClassHandler<GeneralConfig> HANDLER = ConfigClassHandler.createBuilder(GeneralConfig.class)
			.id(new ResourceLocation("magpie_bridge", "general_config"))
			.serializer(config -> GsonConfigSerializerBuilder.create(config)
					.setPath(Main.CONFIG_DIR.resolve("general_config.json5"))
					.setJson5(true)
					.build())
			.build();

	@SerialEntry
	public boolean main_toggle = true;

	@SerialEntry
	public boolean only_others = true;

	@SerialEntry
	public boolean only_background = false;

	@SerialEntry
	public boolean include_system = true;

}
