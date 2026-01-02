package wen_wen520.magpiebridge.client.filter;

import java.io.File;
import wen_wen520.magpiebridge.client.utils.Initializer;
import wen_wen520.magpiebridge.client.utils.MessageFilter;

public class SkyBlockBazaarFilter {

    private static final File filterFile = new File(Initializer.dir_config, "Filters/SkyBlock/bazaar_whitelist.json");
    private static MessageFilter white_list;

    public static String filterMessage(String msg) {

        white_list = new MessageFilter(filterFile);

        if (white_list.isNotFiltered(msg)) {
            return msg;
        }
        return null;
    }
}