package io.github.wen_wen520.magpie_bridge.settings;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public final class LoadSettings {

	public static void init() {
		AutoConfig.register(MainSettings.class, GsonConfigSerializer::new);
	}
}
