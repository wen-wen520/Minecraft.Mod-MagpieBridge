package io.github.wen_wen520.magpie_bridge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.StringUtil;

import io.github.wen_wen520.magpie_bridge.GeneralMessage;


public final class MessagePipeline {

	public static String ClearNameStyle(String text) {
		text = StringUtil.stripColor(text);
		return text;
	}

	public static String ClearBodyStyle(String text) {
		String cleanText = StringUtil.stripColor(text).trim();

		// 1. Matches <[title]name> msg or <name> msg
		// Looks for anything inside < > followed by an optional separator
		Pattern angleBrackets = Pattern.compile("^<.*?>\\s*(?:[:>]\\s*)?(.+)$");

		// 2. Matches [87] [vip] sender: msg
		// Consumes all leading [tags] then finds the sender name and colon
		Pattern multiTags = Pattern.compile("^(?:\\[.*?\\]\\s*)*[\\w\\s*]+[:>]+\\s*(.+)$");

		// 3. Matches [name] msg
		Pattern squareBrackets = Pattern.compile("^\\[.*?\\]\\s*(?:[:>]\\s*)?(.+)$");

		Matcher m1 = angleBrackets.matcher(cleanText);
		if (m1.matches()) return m1.group(1).trim();

		Matcher m2 = multiTags.matcher(cleanText);
		if (m2.matches()) return m2.group(1).trim();

		Matcher m3 = squareBrackets.matcher(cleanText);
		if (m3.matches()) return m3.group(1).trim();

		return cleanText;
	}

	public static GeneralMessage ProcessPlayerMessage(GeneralMessage message) {
		return message;
	}

	public static GeneralMessage ProcessSystemMessage(GeneralMessage message) {
		return message;
	}

}
