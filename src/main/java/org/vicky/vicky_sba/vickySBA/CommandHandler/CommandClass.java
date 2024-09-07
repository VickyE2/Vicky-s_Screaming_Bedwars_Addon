package org.vicky.vicky_sba.vickySBA.CommandHandler;


import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.vicky.vicky_sba.vickySBA.VickySBA;
import org.vicky.vicky_sba.vickySBA.config.ConfigManager;

public class CommandClass implements CommandExecutor {

    private final VickySBA plugin;

    public CommandClass(VickySBA plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")){
            ConfigManager configManager = new ConfigManager(plugin);
            if (sender instanceof Player player) {

                player.sendMessage("Reloading Addon Plugin");
                configManager.reloadPluginConfig();
            }
            if (sender instanceof Server) {

                plugin.getLogger().info("Reloading Addon Plugin");
                configManager.reloadPluginConfig();
            }
            return true;
        }

        sender.sendMessage("Unknown command. Use " + ChatColor.GOLD + "/vsba help " + ChatColor.RESET + "for a list of available commands.");
        return false;
    }
}
