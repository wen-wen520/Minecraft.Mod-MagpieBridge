package wen_wen520.magpiebridge.client.filter;

import java.io.File;
import wen_wen520.magpiebridge.client.utils.Initializer;
import wen_wen520.magpiebridge.client.utils.FilterHelper;

public class GeneralFilter {

    private static final File filterFile = new File(Initializer.dir_config, "Filter/General/BlackList.json");
    private static final String resourcePath = "/assets/magpiebridge/client/BlackList.json";
    private static FilterHelper blackList;

    static {
        blackList = new FilterHelper(filterFile, resourcePath);
    }

    public static String filterMessage(String msg) {
        if (msg == null || msg.isEmpty()) return null;
        msg = msg.replaceAll("§.", "");
        if (msg.isEmpty()) return null;
        if (blackList.shouldFilter(msg)) return null;
        return msg;
    }
}