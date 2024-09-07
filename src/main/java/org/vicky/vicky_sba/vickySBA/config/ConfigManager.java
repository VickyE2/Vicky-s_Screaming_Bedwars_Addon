package org.vicky.vicky_sba.vickySBA.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.vicky.vicky_sba.vickySBA.VickySBA;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final VickySBA plugin;
    private FileConfiguration config;

    public ConfigManager(VickySBA plugin) {
        this.plugin = plugin;
        createConfig();
    }

    // Create the config if it doesn't exist
    public void createConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
        loadConfigValues();
    }
    public void loadConfigValues() {

        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));

        // Get boolean value
        boolean debugEnabled = config.getBoolean("Debug.isEnabled");
        plugin.getLogger().info("DebugIsEnabled " + debugEnabled);

        // Get string value
        boolean debugAdvanced = config.getBoolean("Debug.Advanced");
        plugin.getLogger().info("AdvancedDebugging " + debugAdvanced);
    }

    // Reload the configuration
    public void reloadPluginConfig() {
        loadConfigValues();
        plugin.reloadConfig();
    }

    // Update a specific value in the config
    public void updateConfigValue(String path, Object value) {
        config.set(path, value);
        saveConfig();
    }

    // Save the config to disk
    private void saveConfig() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config.yml!");
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
