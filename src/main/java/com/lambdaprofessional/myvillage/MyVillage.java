package com.lambdaprofessional.myvillage;

import com.lambdaprofessional.myvillage.Inators.Spawninator;
import com.lambdaprofessional.myvillage.commands.MyVillageCommand;
import com.lambdaprofessional.myvillage.configs.Hardcode;
import com.lambdaprofessional.myvillage.events.VillagerDamageBlockListener;
import com.lambdaprofessional.myvillage.events.VillagerRightClickListener;
import com.lambdaprofessional.myvillage.storage.ConfigStorage;
import com.lambdaprofessional.myvillage.tasks.VillagerCheckTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public final class MyVillage extends JavaPlugin {

    private static String version;

    @Override
    public void onEnable() {

        version = getDescription().getVersion(); // alternative?

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        if (!Hardcode.BEHAVIORS_FILE.exists()) {
            try {
                Hardcode.BEHAVIORS_FILE.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        if (!Hardcode.VILLAGERS_FILE.exists()) {
            try {
                Hardcode.VILLAGERS_FILE.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ConfigStorage.saveDefaultConfigIfNotExist();

        registerCommands();
        registerListeners();

        VillagerCheckTask.startTask();

    }

    @Override
    public void onDisable() {
        VillagerCheckTask.stopTask();
        Spawninator.despawnAll();
    }

    public static String getVersion() {
        return version;
    }

    public void registerCommands() {
        Objects.requireNonNull(getCommand("myvillage")).setExecutor(new MyVillageCommand());
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new VillagerDamageBlockListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerRightClickListener(), this);
    }

    public static MyVillage getInstance() {
        return getPlugin(MyVillage.class);
    }
}
