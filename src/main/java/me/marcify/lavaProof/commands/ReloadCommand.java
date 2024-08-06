package me.marcify.lavaProof.commands;

import me.marcify.lavaProof.config.LavaConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("lavaproof.reload")) {
                LavaConfig.reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.reload-message")));
                return true;
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                return true;
            }
        }
        return true;
    }
}
