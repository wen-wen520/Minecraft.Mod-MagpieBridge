package wen_wen520.magpiebridge.client.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
	// Matches: [87] [vip] sender: msg
	private static final Pattern TAGGED_PATTERN = Pattern.compile(
			"^(?:\\[[^]]*]\\s*)+([\\w\\s*]+)[:>\\s]+(.+)$"
	);
	// Matches: "*** joined the lobby"
	private static final Pattern JOINED_LOBBY = Pattern.compile(
			"^([\\w\\s*]+) joined the lobby$"
	);
	// Matches: name: msg, name >> msg, <name> msg, [name] msg
	private static final Pattern GENERIC_PATTERN = Pattern.compile(
			"^[<\\[]?([\\w\\s*]+)[>\\]]?\\s*[:>]?\\s*(.+)$"
	);

	public static ParsedMessage parse(String input) {
		input = input.replaceAll(".§.", "");
		String trimmed = input.trim();
		int inputLength = input.length();
		// 1. Handle joined lobby
		Matcher joined = JOINED_LOBBY.matcher(trimmed);
		if (joined.matches()) {
			String sender = joined.group(1).trim();
			return new ParsedMessage(sender, "joined the lobby!", inputLength);
		}

		// 2. Handle bracketed tags + sender
		Matcher tagged = TAGGED_PATTERN.matcher(trimmed);
		if (tagged.matches()) {
			String sender = tagged.group(1).trim();
			String msg = tagged.group(2).trim();
			return new ParsedMessage(sender, msg, inputLength);
		}

		// 3. Handle generic formats
		Matcher generic = GENERIC_PATTERN.matcher(trimmed);
		if (generic.matches()) {
			String sender = generic.group(1).trim();
			String msg = generic.group(2).trim();
			return new ParsedMessage(sender, msg, inputLength);
		}
		return new ParsedMessage(input, "", inputLength);
	}

	public static class ParsedMessage {
		public final String sender;
		public final String msg;
		public final int length;
		public ParsedMessage(String sender, String msg,int length) {
			this.sender = sender;
			this.msg = msg;
			this.length = length;
		}
		@Override
		public String toString() {
			return "Sender: " + sender + ", Msg: " + msg;
		}
	}
}