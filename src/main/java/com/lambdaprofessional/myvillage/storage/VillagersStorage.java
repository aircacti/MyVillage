package com.lambdaprofessional.myvillage.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaprofessional.myvillage.configs.Hardcode;
import com.lambdaprofessional.myvillage.objects.LambdaVillager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VillagersStorage {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final File FILE = Hardcode.VILLAGERS_FILE;

    public static List<LambdaVillager> loadFromFile() throws IOException {
        if (!FILE.exists() || FILE.length() == 0) {
            return new ArrayList<>();
        }
        return MAPPER.readValue(FILE, new TypeReference<>() {});
    }

    public static void addVillagerToFile(LambdaVillager villager) throws IOException {
        List<LambdaVillager> villagers = loadFromFile();
        villagers.add(villager);
        saveToFile(villagers);
    }

    public static void modifyVillagerInFile(String villagerId, LambdaVillager newVillager) throws IOException {
        if (loadFromFile().stream().noneMatch(v -> v.getId().equals(villagerId))) {
            throw new IllegalArgumentException("Villager with name '" + villagerId + "' not found");
        }
        removeVillagerFromFile(villagerId);
        addVillagerToFile(newVillager);
    }

    public static LambdaVillager getFromFile(String villagerId) throws IOException {
        List<LambdaVillager> villagers = loadFromFile();
        return villagers.stream()
                .filter(v -> v.getId().equals(villagerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Villager with name '" + villagerId + "' not found"));
    }

    public static void removeVillagerFromFile(String villagerId) throws IOException {
        List<LambdaVillager> villagers = loadFromFile();
        boolean removed = villagers.removeIf(v -> v.getId().equals(villagerId));
        if (!removed) {
            throw new IllegalArgumentException("Villager with name '" + villagerId + "' not found");
        }
        saveToFile(villagers);
    }

    public static boolean existsInFile(String villagerId) throws IOException {
        List<LambdaVillager> villagers = loadFromFile();
        return villagers.stream().anyMatch(v -> v.getId().equalsIgnoreCase(villagerId));
    }

    private static void saveToFile(List<LambdaVillager> villagers) throws IOException {
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE, villagers);
    }
}
