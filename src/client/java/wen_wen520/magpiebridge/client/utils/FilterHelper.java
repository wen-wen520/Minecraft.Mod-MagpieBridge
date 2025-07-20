package wen_wen520.magpiebridge.client.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FilterHelper {

    public static class FilterRule {
        public String string;
        public int filter_type;
        public boolean filtered;

        // For regex optimization
        private Pattern pattern;

        public boolean matches(String msg) {
            if (!filtered || string == null || msg == null) return false;
            switch (filter_type) {
                case 0: return msg.equals(string); // equals
                case 1: return msg.contains(string); // contains
                case 2: return msg.startsWith(string); // starts_with
                case 3: return msg.endsWith(string); // ends_with
                case 4: // regex
                    if (pattern == null) pattern = Pattern.compile(string);
                    return pattern.matcher(msg).find();
                default: return false;
            }
        }
    }

    public static class Config {
        public Object infomation; // not used, just for completeness
        public Object guide; // not used, just for completeness
        public List<FilterRule> filter_strings = new ArrayList<>();
    }

    private List<FilterRule> rules;

    public FilterHelper(File configFile, String resourcePath) {
        this.rules = new ArrayList<>();
        loadConfig(configFile, resourcePath);
    }

    private void loadConfig(File configFile, String resourcePath) {
        
        try (Reader reader = new InputStreamReader(new FileInputStream(configFile), "UTF-8")) {
            Gson gson = new Gson();
            Type type = new TypeToken<Config>(){}.getType();
            Config config = gson.fromJson(reader, type);
            if (config != null && config.filter_strings != null) {
                this.rules = config.filter_strings;
            }
        }
        catch (Exception e) {
            rules = new ArrayList<>();
            e.printStackTrace();
        }
    }

    public boolean shouldFilter(String msg) {
        for (FilterRule rule : rules) {
            if (rule.matches(msg)) return true;
        }
        return false;
    }
}