package wen_wen520.magpiebridge.client.filter;

import java.io.File;
import wen_wen520.magpiebridge.client.utils.*;

public class GeneralFilter {

    private static final File filterFile = new File(Initializer.dir_config, "Filters/General/black_list.json");
    private static MessageFilter blackList;

    public static String filterMessage(String msg) {

        blackList = new MessageFilter(filterFile);

        if (msg == null || msg.isEmpty()){ 
            return null;
        }
        msg = msg.replaceAll("§.", "");
        if (msg.isEmpty()) {
            return null;
        }
        if (blackList.isFiltered(msg)) {
            return null;
        }
        return msg;
    }
}