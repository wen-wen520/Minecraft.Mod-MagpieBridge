package wen_wen520.magpiebridge.client.utils;

import java.util.regex.Pattern;

/**
 * Single message filter rule. Fields are public to preserve Gson compatibility.
 */
public class MessageFilterRule {

    // Keep the original field names to preserve JSON compatibility
    public String string;
    public int type;
    public boolean enabled;

    // transient/internal compiled regex cache
    private transient Pattern pattern;

    /**
     * Check whether this rule matches the provided message.
     * Returns false if the rule is disabled or input is null.
     */
    public boolean matches(String msg) {
        if (!enabled || string == null || msg == null) return false;
        switch (type) {
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
