package com.lambdaprofessional.myvillage.storage;

import com.lambdaprofessional.myvillage.MyVillage;
import com.lambdaprofessional.myvillage.tasks.VillagerCheckTask;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigStorage {

    private static final MyVillage plugin = MyVillage.getInstance();

    public static FileConfiguration get() {
        return plugin.getConfig();
    }

    public static void save() {
        plugin.saveConfig();
    }

    public static void reload() {
        plugin.reloadConfig();
        VillagerCheckTask.stopTask();
        VillagerCheckTask.startTask();
    }

    public static void saveDefaultConfigIfNotExist(){
        plugin.saveDefaultConfig();
    }

    public static Map<String, Object> getAllKeysAndValues() {
        FileConfiguration config = get();
        Map<String, Object> map = new HashMap<>();

        Set<String> keys = config.getKeys(true);

        for (String key : keys) {
            if (config.isConfigurationSection(key)) continue;

            Object value = config.get(key);
            map.put(key, value);
        }
        return map;
    }

}
