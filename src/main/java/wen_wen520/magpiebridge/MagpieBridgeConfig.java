package wen_wen520.magpiebridge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public final class MagpieBridgeConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("magpiebridge")
            .resolve("magpiebridge.json");

    private static MagpieBridgeConfig INSTANCE;

    public String modVersion;
    public boolean keepConfigDir;
    public boolean notificationsEnabled;
    public boolean playerNotifications;
    public boolean bazaarNotifications;

    private MagpieBridgeConfig() {
        this.modVersion = detectCurrentVersion();
        this.keepConfigDir = false;
        this.notificationsEnabled = true;
        this.playerNotifications = true;
        this.bazaarNotifications = true;
    }

    public static synchronized MagpieBridgeConfig get() {
        if (INSTANCE == null) {
            INSTANCE = loadInternal();
        }
        return INSTANCE;
    }

    public static synchronized void reload() {
        INSTANCE = loadInternal();
    }

    public static synchronized void save() {
        if (INSTANCE != null) {
            writeToDisk(INSTANCE);
        }
    }

    private static MagpieBridgeConfig loadInternal() {
        MagpieBridgeConfig defaults = new MagpieBridgeConfig();
        boolean requiresReset = false;
        boolean keepConfigDirRequested = false;
        MagpieBridgeConfig lastKnownConfig = null;

        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            if (Files.exists(CONFIG_PATH)) {
                try (BufferedReader reader = Files.newBufferedReader(CONFIG_PATH)) {
                    MagpieBridgeConfig read = GSON.fromJson(reader, MagpieBridgeConfig.class);
                    if (read != null) {
                        lastKnownConfig = read;
                        keepConfigDirRequested = read.keepConfigDir;
                        read.ensureDefaults();
                        if (!defaults.modVersion.equals(read.modVersion)) {
                            requiresReset = true;
                        } else {
                            read.modVersion = defaults.modVersion;
                            writeToDisk(read);
                            return read;
                        }
                    } else {
                        requiresReset = true;
                    }
                } catch (IOException | JsonParseException e) {
                    System.err.println("[MagpieBridge] Failed to read config, resetting: " + e.getMessage());
                    requiresReset = true;
                }
            } else {
                requiresReset = true;
            }
        } catch (IOException e) {
            System.err.println("[MagpieBridge] Failed to prepare config directory: " + e.getMessage());
            requiresReset = true;
        }

        if (lastKnownConfig != null) {
            defaults.keepConfigDir = lastKnownConfig.keepConfigDir;
        }

        if (requiresReset) {
            if (!keepConfigDirRequested) {
                clearConfigDirectory();
            }
        }

        writeToDisk(defaults);
        return defaults;
    }

    private void ensureDefaults() {
        if (modVersion == null || modVersion.isBlank()) {
            modVersion = detectCurrentVersion();
        }
    }

    private static String detectCurrentVersion() {
        return FabricLoader.getInstance()
                .getModContainer("magpiebridge")
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");
    }

    private static void writeToDisk(MagpieBridgeConfig config) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
        } catch (IOException e) {
            System.err.println("[MagpieBridge] Failed to create config directories: " + e.getMessage());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            System.err.println("[MagpieBridge] Failed to save config: " + e.getMessage());
        }
    }

    private static void clearConfigDirectory() {
        Path configDir = CONFIG_PATH.getParent();
        if (configDir == null) {
            return;
        }

        if (Files.exists(configDir)) {
            try (Stream<Path> paths = Files.walk(configDir)) {
                paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        System.err.println("[MagpieBridge] Failed to delete " + path + ": " + e.getMessage());
                    }
                });
            } catch (IOException e) {
                System.err.println("[MagpieBridge] Failed to clear config directory: " + e.getMessage());
            }
        }

        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            System.err.println("[MagpieBridge] Failed to recreate config directory: " + e.getMessage());
        }
    }
}
