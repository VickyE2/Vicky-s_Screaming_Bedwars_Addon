package org.vicky.vicky_sba.vickySBA;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.v_utls.utilities.PlaceholderStorer;
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

            PlaceholderStorer placeholderStorer = new PlaceholderStorer();
            placeholderStorer.storePlaceholder("vsba", "bed-present_<team_color>", plugin.getName(), "Returns weather or not the Bed of the specified team color exists as a "+ChatColor.GOLD+ "<boolean>");
            placeholderStorer.storePlaceholder("vsba", "team-status_<team_color>", plugin.getName(), "Returns weather on not the Team specified by its color is active or un-eliminated as a"+ChatColor.GOLD+" <boolean>");
            placeholderStorer.storePlaceholder("vsba", "player_team", plugin.getName(), "Returns the team of the player as a string "+ChatColor.GOLD+"<color>");
            placeholderStorer.storePlaceholder("vsba", "player_team_formatted", plugin.getName(), "Returns the players team as a string "+ChatColor.GOLD+"<Color>");
            placeholderStorer.storePlaceholder("vsba", "max_time", plugin.getName(), "Returns the max time of the game the player is currently in as seconds"+ChatColor.GOLD+" <integer>");
            placeholderStorer.storePlaceholder("vsba", "remaining_time", plugin.getName(), "Returns the remaining time of the game the player is currently in as seconds"+ChatColor.GOLD+" <integer>");
            placeholderStorer.storePlaceholder("vsba", "max_time_formatted", plugin.getName(), "Returns the max time of the game the player is currently in with the format "+ChatColor.GOLD+"<min:seconds>");
            placeholderStorer.storePlaceholder("vsba", "remaining_time_formatted", plugin.getName(), "Returns the remaining time of the game the player is currently in with the format "+ChatColor.GOLD+"<min:seconds>");
            placeholderStorer.storePlaceholder("vsba", "available-teams", plugin.getName(), "Returns the number of available teams in the game as an integer. Note available such that the number of total teams with or without players in it." +ChatColor.GOLD+" <integer>");
            placeholderStorer.storePlaceholder("vsba", "running-teams", plugin.getName(), "Returns the number of remaining teams in the game as an "+ChatColor.GOLD+"<integer>.");
            placeholderStorer.storePlaceholder("vsba", "color_<team>", plugin.getName(), "Returns the string <team>. This was made to just return the parameter given as it is. If i put color_blahblah it returns blahblah"+ChatColor.GOLD+" <string>");
            placeholderStorer.storePlaceholder("vsba", "team-member_<integer>", plugin.getName(), "Returns the name of the team member at that current index <integer> as a string, so for a team of four for each of the players teammate we have " + ChatColor.GOLD + "team-member_1 as teammate one, team-member_2 as teammate 2, team-member_3 as teammate 3");
            placeholderStorer.storePlaceholder("vsba", "team-size", plugin.getName(), "Returns the size of the team as an "+ChatColor.GOLD+"<integer>");
            placeholderStorer.storePlaceholder("vsba", "team-member-health_<integer>", plugin.getName(), "Returns the current health of the team member at that current index "+ChatColor.GOLD+"<integer>"+ChatColor.RESET+" as an integer, so for a team of four for each of the players teammate we have " + ChatColor.GOLD + "team-member_1 as teammate one, team-member_2 as teammate 2, team-member_3 as teammate 3");
            placeholderStorer.storePlaceholder("vsba", "team-member-max-health_<integer>", plugin.getName(), "Returns the maximum health of the team member at that current index "+ChatColor.GOLD+"<integer>"+ChatColor.RESET+" as an integer, so for a team of four for each of the players teammate we have " + ChatColor.GOLD + "team-member_1 as teammate one, team-member_2 as teammate 2, team-member_3 as teammate 3");

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
