package wen_wen520.magpiebridge.client.utils;

import java.util.ArrayList;
import java.util.List;

public class MessageFilterConfig {

    // Informational fields, not used in logic
    public Object information; // Information about the config
    public Object guide; // Guide for users on how to use the config

    // Actual configuration fields
    public boolean white_list = false; // false = blacklist, true = whitelist
    public List<MessageFilterRule> filter_rules = new ArrayList<>(); // list of filter rules
}
