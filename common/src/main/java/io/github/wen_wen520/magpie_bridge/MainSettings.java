package io.github.wen_wen520.magpie_bridge;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.*;

@Config(name = "magpie_bridge/main_settings")
public class MainSettings implements ConfigData {

	@ConfigEntry.Category("general")
	public boolean main_toggle = true;

	@ConfigEntry.Category("general")
	public boolean only_others = false;

	@ConfigEntry.Category("general")
	public boolean only_background = false;

	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	public boolean include_system = false;

	@ConfigEntry.Category("hypixel")
	public boolean hypixel_main_toggle = true;

	@ConfigEntry.Category("hypixel")
	@ConfigEntry.Gui.Excluded
	public boolean isInHypixel = false;

	@ConfigEntry.Category("hypixel")
	@ConfigEntry.Gui.CollapsibleObject
	Skyblock skyblock = new Skyblock();

	public static class Skyblock {
		boolean main_toggle = true;
		boolean player =  true;
		boolean bazaar =  true;
		boolean auction =  true;
	}

}
