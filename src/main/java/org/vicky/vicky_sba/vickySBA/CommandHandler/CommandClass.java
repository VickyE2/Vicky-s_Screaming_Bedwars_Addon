package org.vicky.vicky_sba.vickySBA.CommandHandler;


import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.v_utls.utilities.PlaceholderStorer;
import org.vicky.vicky_sba.vickySBA.VickySBA;
import org.vicky.vicky_sba.vickySBA.config.ConfigManager;

public class CommandClass implements CommandExecutor {

    private final VickySBA plugin;

    public CommandClass(VickySBA plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vsba")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                ConfigManager configManager = new ConfigManager(plugin);
                if (args.length > 1 && args[1].equalsIgnoreCase("config")) {
                    if (sender instanceof Player player) {

                        player.sendMessage("Reloading Addon Plugin");
                        configManager.reloadPluginConfig();
                    }
                    if (sender instanceof Server) {

                        plugin.getLogger().info("Reloading Addon Plugin");
                        configManager.reloadPluginConfig();
                    }
                }else{
                    sender.sendMessage(ChatColor.RED + "That argument is not valid");
                    sender.sendMessage("valid arguments are: " + ChatColor.GOLD + "config");
                }
                return true;
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("placeholders")){
                if (sender instanceof Player player) {

                    player.sendMessage("Reloading Addon Plugin");
                    PlaceholderStorer  placeholderStorer = new PlaceholderStorer();
                    placeholderStorer.listToPlayer(player.getName(), player);
                }
                if (sender instanceof Server) {

                    plugin.getLogger().info("Listing Registered Placeholders: ");
                    PlaceholderStorer  placeholderStorer = new PlaceholderStorer();
                    placeholderStorer.listPlaceholders(plugin.getName(), plugin);
                }
                return true;
            }
        }

        sender.sendMessage("Unknown command. Use " + ChatColor.GOLD + "/" + command.getName() + " help " + ChatColor.RESET + "for a list of available commands.");
        return false;
    }
}
