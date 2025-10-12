package wen_wen520.magpiebridge.client.utils;

import com.google.gson.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import static wen_wen520.magpiebridge.client.utils.Initializer.dir_head;

public class PlayerHeadExporter {

	// [Async] Fetch and save player head image
	public static CompletableFuture<String> fetchAndSavePlayerHeadAsync(String username) {

		return CompletableFuture.supplyAsync(() -> {

			try {
				if (!dir_head.exists()) {
					dir_head.mkdirs();
				}

				File outputFile = new File(dir_head, username + "_head.png");
				String outputPath = outputFile.getAbsolutePath();

				if (outputFile.exists()) {
					return outputPath;
				}

				String uuid = getUUID(username);
				String skinUrl = getSkinUrl(uuid);
				BufferedImage skin = downloadSkin(skinUrl);
				BufferedImage head = extractAndScaleHead(skin);
				saveHead(head, outputPath);

				return outputPath;
			}

			catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to fetch or save player head for " + username + ": " + e.getMessage());
				return null;
			}

		});
	}

	// Fetch UUID
	private static String getUUID(String username) throws IOException {
		URL url = URI.create("https://api.mojang.com/users/profiles/minecraft/" + username).toURL();
		JsonObject json = JsonParser.parseReader(new InputStreamReader(url.openStream())).getAsJsonObject();
		return json.get("id").getAsString();
	}

	// Fetch skin URL
	private static String getSkinUrl(String uuid) throws IOException {

		URL url = URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid).toURL();
		JsonObject json = JsonParser.parseReader(new InputStreamReader(url.openStream())).getAsJsonObject();
		JsonArray properties = json.getAsJsonArray("properties");

		for (JsonElement propElement : properties) {

			JsonObject prop = propElement.getAsJsonObject();

			if ("textures".equals(prop.get("name").getAsString())) {
				String value = prop.get("value").getAsString();
				String decoded = new String(Base64.getDecoder().decode(value));
				JsonObject textures = JsonParser.parseString(decoded).getAsJsonObject();

				return textures.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
			}
		}

		throw new IOException("Skin URL not found");
	}

	// Download skin image
	private static BufferedImage downloadSkin(String skinUrl) throws IOException {
		URL url = URI.create(skinUrl).toURL();
		return ImageIO.read(url);
	}

	// Extract and scale head from skin image
	private static BufferedImage extractAndScaleHead(BufferedImage skin) {

		BufferedImage headBase = skin.getSubimage(8, 8, 8, 8);
		BufferedImage headOverlay = skin.getSubimage(40, 8, 8, 8);

		BufferedImage head = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = head.createGraphics();
		g.drawImage(headBase, 0, 0, null);
		g.drawImage(headOverlay, 0, 0, null);
		g.dispose();

		BufferedImage scaled = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = scaled.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2.drawImage(head, 0, 0, 128, 128, null);
		g2.dispose();

		return scaled;
	}

	// Save head image
	private static void saveHead(BufferedImage head, String path) throws IOException {
		ImageIO.write(head, "PNG", new File(path));
	}
}