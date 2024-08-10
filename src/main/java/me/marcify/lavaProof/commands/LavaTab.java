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
            if (sender.hasPermission("lavaproof.add")) {
                subcommands.add("add");
            }
            if (sender.hasPermission("lavaproof.remove")) {
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

            if (subcommand.equals("add") && sender.hasPermission("lavaproof.add")) {
                List<String> items = new ArrayList<>();
                for (Material material : Material.values()) {
                    if (material.isItem() && !LavaConfig.getNoBurnItems().contains(material.toString())) {
                        items.add(material.toString());
                    }
                }
                return filterStartingWith(items, args[1].toUpperCase());
            } else if (subcommand.equals("remove") && sender.hasPermission("lavaproof.remove")) {
                return filterStartingWith(LavaConfig.getNoBurnItems(), args[1].toUpperCase());
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
