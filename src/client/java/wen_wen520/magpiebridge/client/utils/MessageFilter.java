package wen_wen520.magpiebridge.client.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageFilter class for filtering messages based on rules defined in a JSON config file.
 */

public class MessageFilter {

    // Attributes
    private List<MessageFilterRule> rules;
    private boolean isWhiteList = false;


    // Constructor
    public MessageFilter(File configFile) {
        this.rules = new ArrayList<>();
        loadConfig(configFile);
    }


    // Main Methods

    // Reload
    public void reloadFrom(File configFile) {
        loadConfig(configFile);
    }

    // Check if message is filtered
    public boolean isFiltered(String msg) {
        if (isWhiteList){
            return false;
        }
        for (MessageFilterRule rule : rules) {
            if (rule.enabled && rule.matches(msg)) {
                return true;
            }
        }
        return false;
    }

    // Check if message is not filtered
    public boolean isNotFiltered(String msg) {
        if (isWhiteList){
            return true;
        }
        for (MessageFilterRule rule : rules) {
            if (rule.enabled && rule.matches(msg)) {
                return false;
            }
        }
        return true;
    }

    // Helper Methods

    // Load filter config from file
    private void loadConfig(File configFile) {

        if (configFile == null) {
            this.rules = new ArrayList<>();
            return;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            Type type = new TypeToken<MessageFilterConfig>(){}.getType();
            MessageFilterConfig config = gson.fromJson(reader, type);

            if (config != null) {
                this.isWhiteList = config.white_list;
                if (config.filter_rules != null) {
                    this.rules = new ArrayList<>(config.filter_rules);
                }
                else {
                    this.rules = new ArrayList<>();
                }
            }
            else {
                this.rules = new ArrayList<>();
            }

        }
        catch (Exception e) {
            this.rules = new ArrayList<>();
            e.printStackTrace();
        }
    }
}
