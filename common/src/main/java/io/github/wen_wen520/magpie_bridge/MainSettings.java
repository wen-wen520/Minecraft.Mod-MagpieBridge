package io.github.wen_wen520.magpie_bridge;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.*;

@Config(name = "magpie_bridge/main_settings")
public class MainSettings implements ConfigData {

    public boolean main_toggle = true;
    public boolean send_desktop_notification = true;
    public boolean only_background = false;

    @ConfigEntry.Gui.CollapsibleObject
    Hypixel hypixel = new Hypixel();

    public static class Hypixel {
        boolean main_toggle = true;
        boolean skyblock_toggle = true;
        boolean skyblock_player =  true;
        boolean skyblock_bazaar =  true;
        boolean skyblock_auction =  true;
    }

}
