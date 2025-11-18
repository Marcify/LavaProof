package me.marcify.lavaProof.commands;

import me.marcify.lavaProof.config.LavaConfig;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LavaTab implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> subcommands = new ArrayList<>();
            if (sender.hasPermission("lavaproof.add") ||
                    sender.hasPermission("lavaproof.add.lava") ||
                    sender.hasPermission("lavaproof.add.explosion")) {
                subcommands.add("add");
            }
            if (sender.hasPermission("lavaproof.remove") ||
                    sender.hasPermission("lavaproof.remove.lava") ||
                    sender.hasPermission("lavaproof.remove.explosion")) {
                subcommands.add("remove");
            }
            if (sender.hasPermission("lavaproof.reload")) {
                subcommands.add("reload");
            }
            subcommands.add("help");
            return filterStartingWith(subcommands, args[0].toLowerCase());
        }

        if (args.length == 2) {
            String subcommand = args[0].toLowerCase();

            if (subcommand.equals("add")) {
                if (sender.hasPermission("lavaproof.add") ||
                        sender.hasPermission("lavaproof.add.lava") ||
                        sender.hasPermission("lavaproof.add.explosion")) {
                    List<String> items = new ArrayList<>();
                    for (Material material : Material.values()) {
                        if (material.isItem() && !LavaConfig.getNoBurnItems().contains(material.toString()) && !LavaConfig.getNoExplosionItems().contains(material.toString())) {
                            items.add(material.toString());
                        }
                    }
                    return filterStartingWith(items, args[1].toUpperCase());
                }
            } else if (subcommand.equals("remove")) {
                if (sender.hasPermission("lavaproof.remove") ||
                        sender.hasPermission("lavaproof.remove.lava") ||
                        sender.hasPermission("lavaproof.remove.explosion")) {
                    List<String> items = new ArrayList<>();
                    items.addAll(LavaConfig.getNoBurnItems());
                    items.addAll(LavaConfig.getNoExplosionItems());
                    return filterStartingWith(items, args[1].toUpperCase());
                }
            }
        }

        if (args.length == 3) {
            String subcommand = args[0].toLowerCase();
            List<String> types = new ArrayList<>();

            if (subcommand.equals("add")) {
                boolean hasFullPerm = sender.hasPermission("lavaproof.add");
                boolean hasLavaPerm = sender.hasPermission("lavaproof.add.lava");
                boolean hasExplosionPerm = sender.hasPermission("lavaproof.add.explosion");

                if (hasFullPerm) {
                    types.add("lava");
                    types.add("explosion");
                } else {
                    if (hasLavaPerm) {
                        types.add("lava");
                    }
                    if (hasExplosionPerm) {
                        types.add("explosion");
                    }
                }
                return filterStartingWith(types, args[2].toLowerCase());
            } else if (subcommand.equals("remove")) {
                boolean hasFullPerm = sender.hasPermission("lavaproof.remove");
                boolean hasLavaPerm = sender.hasPermission("lavaproof.remove.lava");
                boolean hasExplosionPerm = sender.hasPermission("lavaproof.remove.explosion");

                if (hasFullPerm) {
                    types.add("lava");
                    types.add("explosion");
                } else {
                    if (hasLavaPerm) {
                        types.add("lava");
                    }
                    if (hasExplosionPerm) {
                        types.add("explosion");
                    }
                }
                return filterStartingWith(types, args[2].toLowerCase());
            }
        }

        return null;
    }

    private List<String> filterStartingWith(List<String> list, String prefix) {
        List<String> filteredList = new ArrayList<>();
        for (String item : list) {
            if (item.startsWith(prefix)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }
}