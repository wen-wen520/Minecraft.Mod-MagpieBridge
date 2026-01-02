package wen_wen520.magpiebridge;

import net.fabricmc.api.ModInitializer;

public class Magpiebridge implements ModInitializer {

    public static final String MOD_ID = "magpiebridge";

    @Override
    public void onInitialize() {
        MagpieBridgeConfig.get();
    }
}
