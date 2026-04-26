package io.github.wen_wen520.magpie_bridge;


import dev.architectury.injectables.annotations.ExpectPlatform;

public final class ChatMonitor {

	@ExpectPlatform
	public static void init() {
	}

	private static boolean isAllowToSend(int type){
		if (!Main.Settings.main_toggle) {
			return false;
		}

		if (Main.Settings.only_background && Utils.isForeground()) {
			return false;
		}

		return true;
	}
}
