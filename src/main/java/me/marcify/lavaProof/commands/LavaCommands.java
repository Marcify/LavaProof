package me.marcify.lavaProof.commands;

import me.marcify.lavaProof.config.CommandInfo;
import me.marcify.lavaProof.config.LavaConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class LavaCommands implements CommandExecutor {

    public static final Map<String, CommandInfo> commands = new LinkedHashMap<>();

    public LavaCommands() {
        commands.put("lavaproof add", new CommandInfo("Add an item to the list of items that won't burn in lava.", "lavaproof.add"));
        commands.put("lavaproof remove", new CommandInfo("Remove an item from the list of items that won't burn in lava.", "lavaproof.remove"));
        commands.put("lavaproof reload", new CommandInfo("Reload the plugin's configuration.", "lavaproof.reload"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            switch(args[0]) {
                case "add":
                    if (sender.hasPermission("lavaproof.add")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.add-usage")));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                        return true;
                    }
                case "remove":
                    if (sender.hasPermission("lavaproof.remove")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.remove-usage")));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                        return true;
                    }
                case "reload":
                    if (sender.hasPermission("lavaproof.reload")) {
                        LavaConfig.reloadConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.reload-message")));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                        return true;
                    }
                case "help":
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.help-header")));
                    for (Map.Entry<String, CommandInfo> entry : commands.entrySet()) {
                        String commanda = entry.getKey();
                        CommandInfo info = entry.getValue();

                        if (sender.hasPermission(info.getPermission())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.help-commands")
                                    .replace("%command%", commanda)
                                    .replace("%description%", info.getDescription())));
                        }
                    }
                    return true;
                default:
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.unknown-command")));
                    return true;
            }
        }

        if (args.length > 1) {
            switch (args[0]) {
                case "add":
                    if (sender.hasPermission("lavaproof.add")) {
                        String itemName = args[1].toUpperCase();
                        Material material = Material.getMaterial(itemName);
                        if (material == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.invalid-item")));
                            return true;
                        }

                        if (LavaConfig.getNoBurnItems().contains(material.toString())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.already-added")));
                            return true;
                        } else {
                            LavaConfig.addNoBurnItem(material.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.added-item").replace("%item%", material.toString())));
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                        return true;
                    }
                case "remove":
                    if (sender.hasPermission("lavaproof.remove")) {
                        String itemName = args[1].toUpperCase();
                        Material material = Material.getMaterial(itemName);
                        if (material == null) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.invalid-item")));
                            return true;
                        }

                        if (LavaConfig.getNoBurnItems().contains(material.toString())) {
                            LavaConfig.removeNoBurnItem(material.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.removed-item").replace("%item%", material.toString())));
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.item-not-found").replace("%item%", material.toString())));
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                        return true;
                    }
                default:
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.unknown-command")));
                    return true;
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.unknown-command")));
        return true;
    }
}
