package org.vicky.vicky_sba.vickySBA.SB_extension;

import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.HashMap;
import java.util.Map;

public final class BedPresenceVerifier{
    private final BedwarsAPI bedwarsAPI;
    private final Map<String, Boolean> teamStatusMap = new HashMap<>();
    private final Map<String, Boolean> bedStatusMap = new HashMap<>();


    public BedPresenceVerifier(BedwarsAPI bedwarsAPI, Game game) {
        this.bedwarsAPI = bedwarsAPI;
        updateBedStatus(game);
        updateTeamStatus(game);
    }

    public void updateTeamStatus(Game currentgame) {
        // Get the game of the player
        var game = bedwarsAPI.getGameByName(currentgame.getName());

        // Get available and running teams
        var availableTeams = game.getAvailableTeams();
        var runningTeams = game.getRunningTeams();

        // Clear the previous team status
        teamStatusMap.clear();

        // Check each available team and update the status in the map
        for (var team : availableTeams) {
            String teamName = team.getName().toLowerCase();
            boolean isContainedR = runningTeams.stream().anyMatch(t -> t.getName().equals(teamName));
            if (isContainedR) {
                // Team is still running, put it as true
                teamStatusMap.put(teamName, true);
            } else {
                // Team is not running, put it as false
                teamStatusMap.put(teamName, false);
            }
        }
    }

    public void updateBedStatus(Game player) {
        var game = bedwarsAPI.getGameByName(player.getName());
        var runningTeams = game.getRunningTeams();

        // Clear the previous team status
        bedStatusMap.clear();

        // Check each available team and update the status in the map
        for (var team : runningTeams) {
            String teamName = team.getName().toLowerCase();
            boolean bed = team.isTargetBlockExists();
            if (bed) {
                // Team is still running, put it as true
                bedStatusMap.put(teamName, true);
            } else {
                // Team is not running, put it as false
                bedStatusMap.put(teamName, false);
            }
        }
    }

    public boolean isTeamPresent(String teamColor) {
        return teamStatusMap.get(teamColor);
    }
    public Map<String, Boolean> getTeamStatus() {
        return new HashMap<>(teamStatusMap);
    }

    public boolean isBedPresent(String teamName) {
        return bedStatusMap.getOrDefault(teamName, false);
    }
    public Map<String, Boolean> getBedStatus() {
        return new HashMap<>(bedStatusMap);
    }

    // Method to return combined status of teams and beds
    public String getCombinedStatus() {
        StringBuilder statusBuilder = new StringBuilder();
        statusBuilder.append("\n");
        statusBuilder.append("=======================[VSBA]=======================");
        statusBuilder.append("\n");
        for (String team : teamStatusMap.keySet()) {
            boolean isRunning = teamStatusMap.get(team);
            boolean bedPresent = bedStatusMap.getOrDefault(team, false); // Default to false if no entry
            statusBuilder.append("Team ").append(team)
                    .append(": Running: ").append(isRunning)
                    .append(", Bed Present: ").append(bedPresent)
                    .append("\n");
        }
        statusBuilder.append("+=====================[CONFIG]====================+");
        return statusBuilder.toString();
    }
}
