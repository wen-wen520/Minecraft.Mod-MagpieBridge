package wen_wen520.magpiebridge.client.utils;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import wen_wen520.magpiebridge.client.core.Command;

import java.io.*;

public class Initializer implements ClientModInitializer {

	// Global Attributes
	public static final File dir_game = FabricLoader.getInstance().getGameDir().toFile();
	public static final File dir_config = new File(dir_game, "config/magpiebridge");
	public static final File dir_bridge = new File(dir_config, "Bridge/toast.exe");
	public static final File dir_head = new File(dir_config, "Cache Heads");
	public static final File dir_defaultHead = new File(dir_config, "Default Heads/default_player_head.png");

	// Initialize Resources
	@Override
	public void onInitializeClient() {
		// Register Commands
		Command.register();

		// Load Bridge
		LoadResource("/assets/magpiebridge/client/toast.exe", dir_bridge);

		// Load Default Player Head
		LoadResource("/assets/magpiebridge/client/default_player_head.png", dir_defaultHead);

		//Load Filter String Lists
		LoadResource("/assets/magpiebridge/client/filter/general/black_list.json", new File(dir_config, "Filters/General/black_list.json"));

		// Hook for deleting old heads
		Runtime.getRuntime().addShutdownHook(new Thread(Initializer::ClearHeadsCache));
	}


	// Helper Methods

	// Clear Config Directory
	public static void ClearConfigDirectory() {
		if (dir_config.exists() && dir_config.isDirectory())
		{
			DeleteRecursively(dir_config);
		}
		dir_config.mkdirs();
	}

	// Clear Heads Cache Directory
	public static void ClearHeadsCache() {
		if (dir_head.exists() && dir_head.isDirectory())
		{
			DeleteRecursively(dir_head, false);
		}
		else
		{
			dir_head.mkdirs();
		}
	}

	// Delete target recursively.
	private static void DeleteRecursively(File target) {
		DeleteRecursively(target, true);
	}

	private static void DeleteRecursively(File target, boolean deleteSelf) {
		if (target == null || !target.exists())
		{
			return;
		}

		if (target.isDirectory())
		{
			File[] files = target.listFiles();
			if (files != null)
			{
				for (File file : files)
				{
					DeleteRecursively(file, true);
				}
			}
		}

		if (deleteSelf)
		{
			target.delete();
		}
	}

	// Load resource from JAR to file system
	private void LoadResource(String resourcePath, File targetFile) {

		if (!targetFile.exists())
		{
			targetFile.getParentFile().mkdirs();

			try (InputStream in = getClass().getResourceAsStream(resourcePath); OutputStream out = new FileOutputStream(targetFile))
			{
				if (in == null)
				{
					throw new FileNotFoundException("Resource not found: " + resourcePath);
				}
				byte[] buffer = new byte[4096];
				int bytesRead;

				while ((bytesRead = in.read(buffer)) != -1)
				{
					out.write(buffer, 0, bytesRead);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}