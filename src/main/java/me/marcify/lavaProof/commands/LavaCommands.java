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
        commands.put("lavaproof add [item] [lava | explosion | empty for both]", new CommandInfo("Add an item to the list of items that won't burn in lava or get damaged by explosions.", "lavaproof.add"));
        commands.put("lavaproof add [item] lava", new CommandInfo("Add an item to the list of items that won't burn in lava.", "lavaproof.add.lava"));
        commands.put("lavaproof add [item] explosion", new CommandInfo("Add an item to the list of items that won't get damaged by explosions.", "lavaproof.add.explosion"));
        commands.put("lavaproof remove [item] [lava | explosion | empty for both]", new CommandInfo("Remove an item from the list of items that won't burn in lava or get damaged by explosions.", "lavaproof.remove"));
        commands.put("lavaproof remove [item] lava", new CommandInfo("Remove an item from the list of items that won't burn in lava.", "lavaproof.remove.lava"));
        commands.put("lavaproof remove [item] explosion", new CommandInfo("Remove an item from the list of items that won't get damaged by explosions.", "lavaproof.remove.explosion"));
        commands.put("lavaproof reload", new CommandInfo("Reload the plugin's configuration.", "lavaproof.reload"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            switch(args[0]) {
                case "add":
                    if (sender.hasPermission("lavaproof.add") ||
                            sender.hasPermission("lavaproof.add.lava") ||
                            sender.hasPermission("lavaproof.add.explosion")) {
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
                        String commands = entry.getKey();
                        CommandInfo info = entry.getValue();

                        if (sender.hasPermission(info.getPermission())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.help-commands")
                                    .replace("%command%", commands)
                                    .replace("%description%", info.getDescription())));
                        }
                    }
                    return true;
                default:
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.unknown-command")));
                    return true;
            }
        }

        if (args.length >= 2) {
            switch (args[0]) {
                case "add": {
                    String itemName = args[1].toUpperCase();
                    Material material = Material.getMaterial(itemName);

                    if (material == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.invalid-item")));
                        return true;
                    }

                    String type = args.length > 2 ? args[2].toLowerCase() : "both";
                    boolean hasFullPerm = sender.hasPermission("lavaproof.add");
                    boolean hasLavaPerm = sender.hasPermission("lavaproof.add.lava");
                    boolean hasExplosionPerm = sender.hasPermission("lavaproof.add.explosion");

                    if (type.equals("lava") || type.equals("fire")) {
                        if (!hasFullPerm && !hasLavaPerm) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                            return true;
                        }

                        if (LavaConfig.getNoBurnItems().contains(material.toString())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.already-added")).replace("%item", material.toString()).replace("%list%", "no-burn-items"));
                            return true;
                        }

                        LavaConfig.addNoBurnItem(material.toString());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.added-item").replace("%item%", material.toString()).replace("%list%", "no-burn-items")));
                        return true;
                    } else if (type.equals("explosion")) {
                        if (!hasFullPerm && !hasExplosionPerm) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                            return true;
                        }

                        if (LavaConfig.getNoExplosionItems().contains(material.toString())) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.already-added")).replace("%item", material.toString()).replace("%list%", "no-explosion-items"));
                            return true;
                        }

                        LavaConfig.addNoExplosionItem(material.toString());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.added-item").replace("%item%", material.toString()).replace("%list%", "no-explosion-items")));
                        return true;
                    } else {
                        // "both" case - only lavaproof.add can do this
                        if (!hasFullPerm) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                            return true;
                        }

                        boolean lavaExists = LavaConfig.getNoBurnItems().contains(material.toString());
                        boolean explosionExists = LavaConfig.getNoExplosionItems().contains(material.toString());

                        if (lavaExists && explosionExists) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.already-added")).replace("%item", material.toString()).replace("%list%", "no-burn-items and no-explosion-items"));
                            return true;
                        }

                        if (!lavaExists && !explosionExists) {
                            LavaConfig.addNoBurnItem(material.toString());
                            LavaConfig.addNoExplosionItem(material.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.added-item").replace("%item%", material.toString()).replace("%list%", "no-burn-items and no-explosion-items")));
                            return true;
                        } else if (!lavaExists) {
                            LavaConfig.addNoBurnItem(material.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.added-item").replace("%item%", material.toString()).replace("%list%", "no-burn-items")));
                            return true;
                        } else if (!explosionExists) {
                            LavaConfig.addNoExplosionItem(material.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.added-item").replace("%item%", material.toString()).replace("%list%", "no-explosion-items")));
                            return true;
                        }
                    }
                }
                case "remove": {
                    String removeItemName = args[1].toUpperCase();
                    Material removeMaterial = Material.getMaterial(removeItemName);

                    if (removeMaterial == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.invalid-item")));
                        return true;
                    }

                    String removeType = args.length > 2 ? args[2].toLowerCase() : "both";
                    boolean hasFullPerm = sender.hasPermission("lavaproof.remove");
                    boolean hasLavaPerm = sender.hasPermission("lavaproof.remove.lava");
                    boolean hasExplosionPerm = sender.hasPermission("lavaproof.remove.explosion");

                    if (removeType.equals("lava") || removeType.equals("fire")) {
                        if (!hasFullPerm && !hasLavaPerm) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                            return true;
                        }

                        if (LavaConfig.getNoBurnItems().contains(removeMaterial.toString())) {
                            LavaConfig.removeNoBurnItem(removeMaterial.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.removed-item").replace("%item%", removeMaterial.toString()).replace("%list%", "no-burn-items")));
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.item-not-found").replace("%item%", removeMaterial.toString()).replace("%list%", "no-burn-items")));
                            return true;
                        }
                    } else if (removeType.equals("explosion")) {
                        if (!hasFullPerm && !hasExplosionPerm) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                            return true;
                        }

                        if (LavaConfig.getNoExplosionItems().contains(removeMaterial.toString())) {
                            LavaConfig.removeNoExplosionItem(removeMaterial.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.removed-item").replace("%item%", removeMaterial.toString()).replace("%list%", "no-explosion-items")));
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.item-not-found").replace("%item%", removeMaterial.toString()).replace("%list%", "no-explosion-items")));
                            return true;
                        }
                    } else {
                        // "both" case - only lavaproof.remove can do this
                        if (!hasFullPerm) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.no-permission")));
                            return true;
                        }

                        boolean lavaExists = LavaConfig.getNoBurnItems().contains(removeMaterial.toString());
                        boolean explosionExists = LavaConfig.getNoExplosionItems().contains(removeMaterial.toString());

                        if (!lavaExists && !explosionExists) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.item-not-found").replace("%item%", removeMaterial.toString()).replace("%list%", "no-burn-items and no-explosion-items")));
                            return true;
                        }

                        if (lavaExists && explosionExists) {
                            LavaConfig.removeNoBurnItem(removeMaterial.toString());
                            LavaConfig.removeNoExplosionItem(removeMaterial.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.removed-item").replace("%item%", removeMaterial.toString()).replace("%list%", "no-burn-items and no-explosion-items")));
                            return true;
                        } else if (lavaExists) {
                            LavaConfig.removeNoBurnItem(removeMaterial.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.removed-item").replace("%item%", removeMaterial.toString()).replace("%list%", "no-burn-items")));
                            return true;
                        } else if (explosionExists) {
                            LavaConfig.removeNoExplosionItem(removeMaterial.toString());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', LavaConfig.getInstance().getMessage("messages.removed-item").replace("%item%", removeMaterial.toString()).replace("%list%", "no-explosion-items")));
                            return true;
                        }
                    }
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