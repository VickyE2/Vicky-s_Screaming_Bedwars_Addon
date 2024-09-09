package org.vicky.vicky_sba.vickySBA.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;
import org.vicky.vicky_sba.vickySBA.SB_extension.BedPresenceVerifier;
import org.vicky.vicky_sba.vickySBA.VickySBA;

import java.util.List;

public final class BedPlaceholderExpansion extends PlaceholderExpansion {
    private final VickySBA plugin;
    private BedPresenceVerifier bedPresenceVerifier;

    public BedPlaceholderExpansion(VickySBA plugin) {
        this.plugin = plugin;
    }


    // Return the identifier that you'll use in the placeholder (e.g., %myeffects_<placeholder>%)
    @Override
    public @NotNull String getIdentifier() {
        return "vsba";
    }

    // Return the author of the placeholder expansion
    @Override
    public @NotNull String getAuthor() {
        return plugin.getPluginMeta().getAuthors().toString();
    }

    // Return the version of the placeholder expansion
    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true; //
    }

    // This method is called when the placeholder is requested
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }

        BedwarsAPI bedwarsAPI = BedwarsAPI.getInstance();
        Game game = bedwarsAPI.getGameOfPlayer(player);
        BedPresenceVerifier verifier = new BedPresenceVerifier(bedwarsAPI, game);

        if (game == null) {
            return "No game found!";
        }

        // Handle the "bed-present_" placeholders
        if (identifier.startsWith("bed-present_")) {
            String team = identifier.replace("bed-present_", "");
            boolean bedStatus = verifier.isBedPresent(team);
            return Boolean.toString(bedStatus);
        }

        // Handle the "team-status_" placeholders
        if (identifier.startsWith("team-status_")) {
            String team = identifier.replace("team-status_", "").toLowerCase();
            boolean teamStatus = verifier.isTeamPresent(team);
            return Boolean.toString(teamStatus);
        }

        // Handle the "available-teams" placeholder
        if (identifier.startsWith("available-teams")){
            int available_teams = game.getAvailableTeams().toArray().length;
            return Integer.toString(available_teams);
        }

        if (identifier.startsWith("running-teams")){
            int running_teams = game.getRunningTeams().toArray().length;
            return Integer.toString(running_teams);
        }

        // Handle the "team-member_x" placeholders where x is the member's position
        if (identifier.startsWith("team-member_")) {
            String indexString = identifier.replace("team-member_", "");
            int memberIndex;

            try {
                memberIndex = Integer.parseInt(indexString);  // Convert x to an integer
            } catch (NumberFormatException e) {
                return "Invalid index!";
            }

            var team = game.getTeamOfPlayer(player);
            if (team == null) {
                return "null";
            }

            List<Player> teamMembers = team.getConnectedPlayers();
            int teamSize = team.countConnectedPlayers();
            if (memberIndex < 1 || memberIndex > teamSize) {
                return "null";
            }

            // Get the player's name at the given index
            return teamMembers.get(memberIndex - 1).getName();
        }

        return null; // Return null if the placeholder is not handled
    }

}
