package org.vicky.vicky_sba.vickySBA.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.vicky.vicky_sba.vickySBA.SB_extension.BedPresenceVerifier;
import org.vicky.vicky_sba.vickySBA.VickySBA;
import org.vicky.vicky_sba.vickySBA.config.ConfigManager;

import java.util.Map;

public class GameRunningListener implements Listener {

    private final VickySBA plugin;
    private ConfigManager configManager;
    private final BedwarsAPI bedwarsAPI;// Reference to your main plugin class

    public GameRunningListener(VickySBA plugin, BedwarsAPI bedwarsAPI) {
        this.plugin = plugin;
        this.bedwarsAPI = bedwarsAPI;// Pass the plugin instance to schedule tasks
    }

    @EventHandler
    public void onGameStarts(BedwarsGameStartedEvent event) {
        Game game = event.getGame();
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Check available teams and running teams here
            var availableTeams = game.getAvailableTeams();
            var runningTeams = game.getRunningTeams();
            configManager = new ConfigManager(plugin);

            // Iterate over available teams and check if they're still running
            for (Team team : availableTeams) {
                String teamName = team.getName();
                boolean isRunning = runningTeams.contains(team);
                new BedPresenceVerifier(bedwarsAPI, game);
                // Log the result or perform any action
                if (configManager.getConfig().getBoolean("Default.isEnabled")) {
                    Bukkit.getLogger().info("Team: " + teamName + " is running: " + isRunning);
                }
            }

        }, 0L, 40L); // 0L is the delay before starting, 40L is the interval in ticks (2 seconds)
    }

}
