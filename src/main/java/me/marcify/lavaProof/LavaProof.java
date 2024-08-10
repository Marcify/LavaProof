package me.marcify.lavaProof;

import me.marcify.lavaProof.commands.LavaCommands;
import me.marcify.lavaProof.commands.LavaTab;
import me.marcify.lavaProof.config.LavaConfig;
import me.marcify.lavaProof.events.BurnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LavaProof extends JavaPlugin {

    private static LavaProof instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        LavaConfig.getInstance().load();
        getServer().getPluginManager().registerEvents(new BurnEvent(this), this);
        getCommand("lavaproof").setExecutor(new LavaCommands());
        getCommand("lavaproof").setTabCompleter(new LavaTab());
        getLogger().info("LavaProof has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("LavaProof has been disabled!");
    }

    public static LavaProof getInstance() {
        return instance;
    }
}
