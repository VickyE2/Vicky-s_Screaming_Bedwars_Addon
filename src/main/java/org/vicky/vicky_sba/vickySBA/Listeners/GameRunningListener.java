package org.vicky.vicky_sba.vickySBA.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.BedWarsGameDisabledEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.v_utls.utilities.TimeDifference;
import org.vicky.vicky_sba.vickySBA.SB_extension.BedPresenceVerifier;
import org.vicky.vicky_sba.vickySBA.VickySBA;
import org.vicky.vicky_sba.vickySBA.config.ConfigManager;
import org.vicky.vicky_sba.vickySBA.expansions.PlaceholderUtil;
import org.vicky.vicky_sba.vickySBA.global.Global;

import java.util.List;

public class GameRunningListener implements Listener {

    private final VickySBA plugin;
    private final PlaceholderUtil placeholderUtil;
    private ConfigManager configManager;
    private final BedwarsAPI bedwarsAPI;
    private int taskId = -1;
    private int taskId2 = -2;// Store the task ID to cancel later

    public GameRunningListener(VickySBA plugin, BedwarsAPI bedwarsAPI) {
        this.plugin = plugin;
        this.bedwarsAPI = bedwarsAPI;
        this.placeholderUtil = new PlaceholderUtil();
    }

    @EventHandler
    public void onGameStarts(BedwarsGameStartedEvent event) {
        Game game = event.getGame();
        long startTime = System.currentTimeMillis();

        // Start the task
        taskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            BedPresenceVerifier verifier = new BedPresenceVerifier(bedwarsAPI, game);
            verifier.updateTeamStatus(game);
            verifier.updateBedStatus(game);
            long currentTime = System.currentTimeMillis();
            Global.timeDiff = new TimeDifference(startTime, currentTime);

        }, 0L, 20L).getTaskId();// 0L is the delay before starting, 40L is the interval (2 seconds)
        taskId2 = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            var availableTeams = game.getAvailableTeams();
            var runningTeams = game.getRunningTeams();
            configManager = new ConfigManager(plugin);
            BedPresenceVerifier verifier = new BedPresenceVerifier(bedwarsAPI, game);
            boolean isEnabled = configManager.getConfig().getBoolean("Debug.isEnabled");

            if(isEnabled) {
                // Additional debug logging
                Bukkit.getLogger().info("Available teams: " + availableTeams.size());
                Bukkit.getLogger().info("Running teams: " + runningTeams.size());
                Bukkit.getLogger().info("Config 'Default.isEnabled' is: " + isEnabled);
                Bukkit.getLogger().info("ArenaTime: " + game.getArenaTime().getTime());
                Bukkit.getLogger().info("ArenaTime2: " + game.getArenaTime().time);
                Bukkit.getLogger().info("GameTime: " + game.getGameTime());
                String combinedStatus = verifier.getCombinedStatus();
                Bukkit.getLogger().info(combinedStatus);
                /*
                // Log players from running teams
                for (Team team : runningTeams) {
                                        String teamName = team.getName();
                    List<Player> teamPlayers = team.getGame().getConnectedPlayers();
                    Bukkit.getLogger().info("Players in team " + teamName + ":");
                    for (Player player : teamPlayers) {
                        var playerTeam = game.getTeamOfPlayer(player).getName().toLowerCase();
                        Bukkit.getLogger().info("- " + player.getName());
                        player.sendMessage("Your Teams is: " + playerTeam);
                        player.sendMessage("=======[Team-Status]======");
                        sendStatusMessage(player, "%vsba_team-status_" + playerTeam + "%");
                        boolean teamP = verifier.isTeamPresent(playerTeam);
                        player.sendMessage(Boolean.toString(teamP));
                        player.sendMessage("=======[Bed-Present]======");
                        sendStatusMessage(player, "%vsba_bed-present_" + playerTeam + "%");
                        boolean bedP = verifier.isTeamPresent(playerTeam);
                        player.sendMessage(Boolean.toString(bedP));
                    }
                }
            }

                 */
            }

        }, 0L, 120L).getTaskId();
    }

    @EventHandler
    public void onGameEnds(BedwarsGameEndEvent event) {
        // Cancel the scheduled task when the game ends
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            Bukkit.getLogger().info("Game ended, task cancelled.");
        }
        if (taskId2 != -2) {
            Bukkit.getScheduler().cancelTask(taskId2);
            Bukkit.getLogger().info("Game ended, task cancelled.");
        }
    }

    @EventHandler
    public void onGameStopped(BedWarsGameDisabledEvent event) {
        // Cancel the scheduled task when the game ends
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            Bukkit.getLogger().info("Game disabled, task cancelled.");
        }
        if (taskId2 != -2) {
            Bukkit.getScheduler().cancelTask(taskId2);
            Bukkit.getLogger().info("Game disabled, task cancelled.");
        }
    }

    public void sendStatusMessage(Player player, String placeholder) {
        placeholderUtil.sendPlaceholderMessage(player, placeholder);
    }
}
