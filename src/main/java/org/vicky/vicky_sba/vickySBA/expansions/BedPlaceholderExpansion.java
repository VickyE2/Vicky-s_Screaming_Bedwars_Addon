package org.vicky.vicky_sba.vickySBA.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;
import org.vicky.vicky_sba.vickySBA.SB_extension.BedPresenceVerifier;
import org.vicky.vicky_sba.vickySBA.VickySBA;

public final class BedPlaceholderExpansion extends PlaceholderExpansion {
    private final VickySBA plugin;
    private BedPresenceVerifier bedPresenceVerifier;

    public BedPlaceholderExpansion(VickySBA plugin) {
        this.plugin = plugin;
    }


    // Return the identifier that you'll use in the placeholder (e.g., %myeffects_<placeholder>%)
    @Override
    public @NotNull String getIdentifier() {
        return "vickysba";
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
        BedwarsAPI bedwarsAPI = BedwarsAPI.getInstance();
        Game game = bedwarsAPI.getGameOfPlayer(player);

        if (player == null) {
            return "";
        }

        // Initialize the BedPresenceVerifier with BedwarsAPI and Game
        if (bedPresenceVerifier == null) {
            bedPresenceVerifier = new BedPresenceVerifier(bedwarsAPI, game);
        }

        // Check for bed status placeholder requests
        if (identifier.startsWith("bedpresent_")) {
            String team = identifier.replace("bedpresent_", "");
            Boolean bedStatus = bedPresenceVerifier.getBedStatus().get(team);
            return bedStatus != null ? bedStatus.toString() : "false";
        }

        // Check for team status placeholder requests
        if (identifier.startsWith("teamstatus_")) {
            String team = identifier.replace("teamstatus_", "");
            Boolean teamStatus = bedPresenceVerifier.getTeamStatus().get(team);
            return teamStatus != null ? teamStatus.toString() : "false";
        }

        // Return null if the placeholder is not handled
        return null;
    }
}
