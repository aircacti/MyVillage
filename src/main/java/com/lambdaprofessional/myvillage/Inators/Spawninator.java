package com.lambdaprofessional.myvillage.Inators;

import com.lambdaprofessional.myvillage.configs.Hardcode;
import com.lambdaprofessional.myvillage.objects.LambdaVillager;
import com.lambdaprofessional.myvillage.storage.VillagersStorage;
import com.lambdaprofessional.myvillage.utils.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Spawninator {

    public static void spawn(LambdaVillager villager) {
        String id = villager.getId();

        if (isVillagerAlreadySpawned(id)) {
            Messenger.info("Villager '" + id + "' is already spawned");
            return;
        }

        Location location = villager.getLocation();
        if (location == null) {
            Messenger.info("Villager '" + id + "' has no saved location");
            return;
        }

        Villager entity = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);

        Component displayNameComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(villager.getDisplayName());
        entity.customName(displayNameComponent);
        entity.setCustomNameVisible(villager.getDisplayVisible());
        entity.setProfession(villager.getProfession());
        entity.setVillagerLevel(villager.getLevel());
        entity.setVillagerType(villager.getType());
        entity.setSilent(villager.getSilent());
        entity.setAI(false);
        entity.setInvulnerable(true);


        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING, id);

        Messenger.info("Spawned villager '" + id + "' at " + location.toVector());
    }

    public static void spawn(String id) {
        if (isVillagerAlreadySpawned(id)) {
            Messenger.info("Villager '" + id + "' is already spawned");
            return;
        }

        try {
            LambdaVillager villager = VillagersStorage.getFromFile(id);
            spawn(villager);
        } catch (IOException e) {
            Messenger.info("Failed to spawn villager '" + id + "': " + e.getMessage());
        }
    }

    public static void spawnAll() {
        int spawnedCount = 0;
        try {
            for (LambdaVillager lambdaVillager : VillagersStorage.loadFromFile()) {
                spawn(lambdaVillager);
                spawnedCount++;
            }
            Messenger.info("Spawned " + spawnedCount + " villagers");
        } catch (IOException e) {
            Messenger.info("Failed to spawn all villagers: " + e.getMessage());
        }
    }

    public static void despawn(String id) {
        boolean removed = false;

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Villager.class)) {
                PersistentDataContainer pdc = entity.getPersistentDataContainer();
                if (pdc.has(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING)) {
                    String storedName = pdc.get(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING);
                    if (id.equals(storedName)) {
                        entity.remove();
                        Messenger.info("Despawned villager '" + id + "'");
                        removed = true;
                    }
                }
            }
        }

        if (!removed) {
            Messenger.info("Villager '" + id + "' not found");
        }
    }

    public static void despawnAll() {
        int removedCount = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Villager.class)) {
                PersistentDataContainer pdc = entity.getPersistentDataContainer();
                if (pdc.has(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING)) {
                    entity.remove();
                    removedCount++;
                }
            }
        }
        Messenger.info("Despawned " + removedCount + " villagers");
    }

    public static void refresh() {
        despawnAll();
        spawnAll();
    }

    public static int getExpectedVillagerCount() {
        try {
            return VillagersStorage.loadFromFile().size();
        } catch (IOException e) {
            Messenger.info("Failed to load villagers from file to count expected: " + e.getMessage());
            return 0;
        }
    }

    public static int getCurrentVillagerCount() {
        int count = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Villager.class)) {
                PersistentDataContainer pdc = entity.getPersistentDataContainer();
                if (pdc.has(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static List<Villager> getSpawnedVillagers() {
        List<Villager> villagers = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            for (Villager entity : world.getEntitiesByClass(Villager.class)) {
                PersistentDataContainer pdc = entity.getPersistentDataContainer();
                if (pdc.has(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING)) {
                    villagers.add(entity);
                }
            }
        }
        return villagers;
    }

    public static boolean isOurVillager(Entity entity) {
        if (!(entity instanceof Villager)) return false;

        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.has(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING);
    }

    private static boolean isVillagerAlreadySpawned(String name) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Villager.class)) {
                PersistentDataContainer pdc = entity.getPersistentDataContainer();
                if (pdc.has(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING)) {
                    String storedName = pdc.get(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING);
                    if (name.equals(storedName)) return true;
                }
            }
        }
        return false;
    }
}
