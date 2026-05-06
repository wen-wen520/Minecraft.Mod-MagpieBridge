package io.github.wen_wen520.magpie_bridge;

import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.Util;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public final class Main {

	public static final String MOD_ID = "magpie_bridge";
	public static final Logger LOGGER = LoggerFactory.getLogger("MagpieBridge");
	public static final Util.OS OS = Util.getPlatform();

	public static final Path CONFIG_DIR = Utils.getConfigDir().resolve(MOD_ID);
	public static final Path CACHE_HEADS_DIR = CONFIG_DIR.resolve("head icons").resolve("cached");
	public static final Path DEFAULT_HEAD_DIR = CONFIG_DIR.resolve("head icons").resolve("default");
	public static final Path DEFAULT_HEAD = DEFAULT_HEAD_DIR.resolve("default_player_head.png");
	public static final Path BRIDGE_DIR = CONFIG_DIR.resolve("bridge");
	public static final Path BRIDGE_WIN = BRIDGE_DIR.resolve("toast.exe");

	public static MainSettings Settings;

	public static void init() {

		AutoConfig.register(MainSettings.class, GsonConfigSerializer::new);
		Settings = AutoConfig.getConfigHolder(MainSettings.class).getConfig();

		LoadResource.init();
		ChatMonitor.init();

		GeneralMessage finish_setup = GeneralMessage.builder()
				.title("Magpie Bridge Mod")
				.body("Magpie Bridge Notification setup complete!")
				.build();
		Notifier.send(finish_setup);

		LOGGER.info("MagpieBridge has been loaded!");
	}

}
