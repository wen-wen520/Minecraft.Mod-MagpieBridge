package io.github.wen_wen520.magpie_bridge.utils;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.UUID;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import static io.github.wen_wen520.magpie_bridge.Main.CACHE_HEADS_DIR;
import static io.github.wen_wen520.magpie_bridge.Main.DEFAULT_HEAD;

public final class SkinResource {

	// [Async] Fetch and save player head image with Name
	public static CompletableFuture<Path> getPlayerHead(String player_name) {

		return CompletableFuture.supplyAsync(() -> {

			try {

				Path player_head_path = CACHE_HEADS_DIR.toAbsolutePath().resolve(player_name + ".png");

				if (player_head_path.toFile().exists()) {
					return player_head_path;
				}

				URL skinUrl = getSkinUrl(getUUIDFromName(player_name));
				BufferedImage skin = downloadSkin(skinUrl);
				BufferedImage head = extractAndScaleHead(skin);
				saveHead(head, player_head_path);

				return player_head_path;
			}

			catch (Exception e) {
				return DEFAULT_HEAD;
			}

		});
	}

	// [Async] Fetch and save player head image with UUID
	public static CompletableFuture<Path> getPlayerHead(UUID player_uuid) {
		return CompletableFuture.supplyAsync(() -> {
			String player_name = getNameFromUUID(player_uuid);
			try {

				Path player_head_path = CACHE_HEADS_DIR.toAbsolutePath().resolve(player_name + ".png");

				if (player_head_path.toFile().exists()) {
					return player_head_path;
				}

				URL skinUrl = getSkinUrl(player_uuid);
				BufferedImage skin = downloadSkin(skinUrl);
				BufferedImage head = extractAndScaleHead(skin);
				saveHead(head, player_head_path);

				return player_head_path;
			}

			catch (Exception e) {
				return DEFAULT_HEAD;
			}

		});
	}

	// Fetch UUID - deprecated, use Utils.getUUIDFromName instead
	private static String getUUIDold(String player_name) throws IOException {
		URL url = URI.create("https://api.mojang.com/users/profiles/minecraft/" + player_name).toURL();
		JsonObject json = JsonParser.parseReader(new InputStreamReader(url.openStream())).getAsJsonObject();
		return json.get("id").getAsString();
	}

	/**
	 * Gets a Name from a UUID.
	 * Checks local cache first, then hits Mojang API.
	 */
	private static String getNameFromUUID(UUID uuid) {

		String name = "Unknown";

		if (uuid == null){
			return name;
		}

		// 1. Try local cache
		PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(uuid);
		if (info != null) {
			return info.getProfile().getName();
		}

		// 2. Fallback to Mojang API
		try {
			URL url = URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "")).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			if (connection.getResponseCode() == 200) {
				try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
					JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
					return json.get("name").getAsString();
				}
			}
		}
		catch (Exception e) {
			return name;
		}
		return name;
	}

	/**
	 * Gets a UUID from a Name.
	 * Checks local cache first, then hits Mojang API.
	 */
	private static UUID getUUIDFromName(String name) {

		UUID uuid = null;

		if (name == null || name.isEmpty()){
			return uuid;
		}

		for (PlayerInfo info : Minecraft.getInstance().getConnection().getOnlinePlayers()) {
			if (info.getProfile().getName().equalsIgnoreCase(name)) {
				return info.getProfile().getId();
			}
		}

		// 2. Fallback to Mojang API
		try {
			URL url = URI.create("https://api.mojang.com/users/profiles/minecraft/" + name).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			if (connection.getResponseCode() == 200) {
				try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
					JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
					String id = json.get("id").getAsString();

					// Format undashed String from Mojang back to UUID object
					return UUID.fromString(id.replaceFirst(
							"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})",
							"$1-$2-$3-$4-$5"
					));
				}
			}
		} catch (Exception e) {
			return uuid;
		}
		return uuid;
	}

	// Fetch skin URL
	private static URL getSkinUrl(UUID player_uuid) throws IOException {

		URL url = URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + player_uuid).toURL();
		JsonObject json = JsonParser.parseReader(new InputStreamReader(url.openStream())).getAsJsonObject();
		JsonArray properties = json.getAsJsonArray("properties");

		for (JsonElement propElement : properties) {

			JsonObject prop = propElement.getAsJsonObject();

			if ("textures".equals(prop.get("name").getAsString())) {
				String value = prop.get("value").getAsString();
				String decoded = new String(Base64.getDecoder().decode(value));
				JsonObject textures = JsonParser.parseString(decoded).getAsJsonObject();

				return URI.create(textures.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString()).toURL();
			}
		}

		throw new IOException("Skin URL not found");
	}

	// Download skin image
	private static BufferedImage downloadSkin(URL skinUrl) throws IOException {
		return ImageIO.read(skinUrl);
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
	private static void saveHead(BufferedImage head, Path path) throws IOException {
		ImageIO.write(head, "PNG", path.toFile());
	}
}
