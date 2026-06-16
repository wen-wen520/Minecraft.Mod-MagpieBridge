package io.github.wen_wen520.magpie_bridge.fabric;

import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import io.github.wen_wen520.magpie_bridge.configs.GeneralConfigScreen;

public class ModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return GeneralConfigScreen::create;
	}

}
