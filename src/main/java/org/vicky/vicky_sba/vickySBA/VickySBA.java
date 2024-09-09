package org.vicky.vicky_sba.vickySBA;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.vicky.vicky_sba.vickySBA.CommandHandler.CommandClass;
import org.vicky.vicky_sba.vickySBA.Listeners.GameRunningListener;
import org.vicky.vicky_sba.vickySBA.config.ConfigManager;
import org.vicky.vicky_sba.vickySBA.expansions.BedPlaceholderExpansion;

import javax.xml.transform.Source;
import java.util.Objects;


public final class VickySBA extends JavaPlugin {

    public static VickySBA plugin;
    public static VickySBA getPlugin() {
        return plugin;
    }
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().getPlugin("BedWars") != null) {
            // Initialize the ConfigManager
            configManager = new ConfigManager(this);

            // Load configuration values
            configManager.loadConfigValues();


            try {
                if (getCommand("vsba") == null) {
                    getLogger().severe("The command /vsba is not registered! Check your plugin.yml");
                    return;
                }else {
                    Objects.requireNonNull(getCommand("vsba")).setExecutor(new CommandClass(this));
                    getLogger().info("/vsba command registered.");
                }
            } catch (Exception e) {
                getLogger().severe("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }

            BedwarsAPI api = BedwarsAPI.getInstance();
            if (api == null) {
                getLogger().severe("Could not find BedWars API. Make sure Screaming Bedwars is installed.");
                getServer().getPluginManager().disablePlugin(this); // Disable your plugin if BedWars API is not found
                return;
            }

            getLogger().info("Enabling Vicky's Screaming Bedwars Addon");

            getLogger().info("Hooking Placeholders");
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                new BedPlaceholderExpansion(this).register();
            }

            getServer().getPluginManager().registerEvents(new GameRunningListener(this, api), this);
            getLogger().info("VSBA successfully loaded");

        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Shutting Down BedWar's Addon.");
    }

    public void reloadConfigManager() {
        configManager.reloadPluginConfig();
    }

    // Method to expose config updates
    public void updateConfig(String path, Object value) {
        configManager.updateConfigValue(path, value);
    }

}
