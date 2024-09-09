package org.vicky.vicky_sba.vickySBA.expansions;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderUtil {

    /**
     * Sends a message with a placeholder value to a player.
     *
     * @param player The player to send the message to.
     * @param placeholder The placeholder string (e.g., "%example_placeholder%").
     */
    public void sendPlaceholderMessage(Player player, String placeholder) {
        // Resolve the placeholder
        String resolvedMessage = PlaceholderAPI.setPlaceholders(player, placeholder);

        // Send the resolved message to the player
        player.sendMessage(resolvedMessage);
    }
}
