package me.marcify.lavaProof.config;

import me.marcify.lavaProof.LavaProof;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LavaConfig {

    private final static LavaConfig instance = new LavaConfig();

    private File file;
    private static YamlConfiguration config;

    public void load() {
        file = new File(LavaProof.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            LavaProof.getInstance().saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
        config.options().parseComments(true);

        InputStream defConfigStream = LavaProof.getInstance().getResource("config.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
            config.options().copyDefaults(true);
        }

        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            LavaProof.getInstance().getLogger().severe("Could not save config.yml!");
        }
    }

    public static void reloadConfig() {
        LavaConfig.getInstance().load();
    }

    public static LavaConfig getInstance() {
        return instance;
    }

    public String getMessage(String message) {
        return config.getString(message);
    }

    public List<String> getNoBurnItems() {
        return config.getStringList("no-burn-items");
    }
}
