package wen_wen520.magpiebridge.client.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyBlockMsgFilter {

	public static String[] filterMessage(String original) {
		if (original == null || original.isEmpty()) return null;

		Matcher m1 = Pattern.compile("^\\[\\d+\\][^\\[]*\\[[^]]+\\]\\s*([^:]+):\\s*(.+)$").matcher(original);
		if (m1.find()) return new String[] { "SkyBlock Player", m1.group(1).trim(), m1.group(2).trim() };

		// Pattern 2: [number] icon Name: message (no [RANK])
		Matcher m2 = Pattern.compile("^\\[\\d+\\][^\\w\\[]*([\\w_]+):\\s*(.+)$").matcher(original);
		if (m2.find()) return new String[] { "SkyBlock Player", m2.group(1).trim(), m2.group(2).trim() };

		// Bazaar or sell/system messages
		if (original.startsWith("[Bazaar]") && !(original.startsWith("[Bazaar] Putting goods in escrow...") || original.startsWith("[Bazaar] Submitting sell offer") || original.startsWith("[Bazaar] Claiming order") || original.startsWith("[Bazaar] Claiming orders") || original.startsWith("[Bazaar] Submitting buy orders..."))) return new String[] { "SkyBlock Bazaar", "Bazaar", original.replace("[Bazaar]", "").trim() };
		if (original.startsWith("You sold")) return new String[] { "SkyBlock Bazaar", "Bazaar", original.trim() };

		// Ignore if not matched
		return null;
	}
}
