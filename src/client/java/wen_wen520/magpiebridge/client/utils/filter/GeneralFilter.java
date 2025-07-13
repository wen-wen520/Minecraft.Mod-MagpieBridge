package wen_wen520.magpiebridge.client.utils.filter;

public class GeneralFilter {

	public static String filterMessage(String msg) {
		if (msg == null || msg.isEmpty()) return null;

		msg =  msg.replaceAll("§.", "");
		if (msg.isEmpty()) return null;

		if (msg.contains("joined the lobby!") || msg.contains("unclaimed") || msg.startsWith("Warping you to") || msg.startsWith("You have") || msg.startsWith("Sending to ") || msg.startsWith("Welcome to ") || msg.startsWith("Request join for "))
		{
			return null;
		}
		else {
			return msg;
		}
	}
}
