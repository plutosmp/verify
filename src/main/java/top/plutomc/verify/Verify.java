package top.plutomc.verify;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class Verify extends JavaPlugin {

    private static JavaPlugin instance;

    private static File dataFile;

    private static FileConfiguration data;

    public static FileConfiguration data() {
        return data;
    }

    public static JavaPlugin instance() {
        return instance;
    }

    public static void saveData() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reloadData() {
        try {
            data.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reload() {
        reloadData();
    }

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        data = YamlConfiguration.loadConfiguration(dataFile);

        if (!data().contains("generatedCodes")) {
            data().set("generatedCodes", new ArrayList<Integer>());
            saveData();
        }

        if (!data().contains("dontCreateCode")) {
            data().set("dontCreateCode", new ArrayList<String>());
            saveData();
        }

        getServer().getPluginCommand("verify").setExecutor(new Command());
        getServer().getPluginCommand("verify").setTabCompleter(new Command());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        saveData();
    }
}