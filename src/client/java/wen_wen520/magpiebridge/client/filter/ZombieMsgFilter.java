package wen_wen520.magpiebridge.client.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZombieMsgFilter {

	public static String[] filterMessage(String original) {
		if (original == null || original.isEmpty()) return null;

		// Gold message: "+15 Gold (Critical Hit)" or "+10 Gold"
		Matcher gold = Pattern.compile("^\\+(\\d+) Gold ( \\(Critical Hit\\))?$").matcher(original);
		if (gold.find()) {
			String value = gold.group(1);
			String critical = (gold.group(2) != null) ? "✨" : "";
			return new String[] { "Zombie Shot", value + critical, "" };
		}

		// Player chat: "[VIP] Name: Msg" or "Name: Msg"
		Matcher chat = Pattern.compile("^(?:\\[[^\\]]+\\] )?([\\w_]+): ([\\w\\W]+)$").matcher(original);
		if (chat.find()) {
			String name = chat.group(1);
			String content = chat.group(2);
			return new String[] { "Minecraft Players", name, content };
		}

		if (original.startsWith("You have fully repaired this window!")){
			return new String[] { "Zombie System", "✅ Window fully repaired!", "" };
		}
		// Otherwise, ignore (return null)
		return null;
	}
}
