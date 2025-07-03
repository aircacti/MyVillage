package com.lambdaprofessional.myvillage.tasks;

import com.lambdaprofessional.myvillage.Inators.Spawninator;
import com.lambdaprofessional.myvillage.MyVillage;
import com.lambdaprofessional.myvillage.configs.UserConfig;
import com.lambdaprofessional.myvillage.utils.Messenger;
import org.bukkit.scheduler.BukkitRunnable;


public class VillagerCheckTask extends BukkitRunnable {

    public static VillagerCheckTask instance;

    public static void startTask() {
        if (instance != null) {
            instance.cancel();
        }

        instance = new VillagerCheckTask();
        instance.runTaskTimer(MyVillage.getInstance(), 40L, UserConfig.getCheckEveryTicks());
    }

    public static void stopTask() {
        if (instance != null && !instance.isCancelled()) {
            instance.cancel();
            instance = null;
        }
    }

    @Override
    public void run() {
        int expected = Spawninator.getExpectedVillagerCount();
        int actual = Spawninator.getCurrentVillagerCount();

        if (actual != expected) {
            Messenger.info("Villager count mismatch. Expected: " + expected + ", Actual: " + actual + ". Fixing...");
            Spawninator.refresh();
        } else {
            Messenger.info("Villager count OK. Expected: " + expected + ", Actual: " + actual);
        }
    }
}
