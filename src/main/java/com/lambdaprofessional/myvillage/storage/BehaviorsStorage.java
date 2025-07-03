package com.lambdaprofessional.myvillage.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaprofessional.myvillage.configs.Hardcode;
import com.lambdaprofessional.myvillage.objects.LambdaBehavior;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BehaviorsStorage {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final File FILE = Hardcode.BEHAVIORS_FILE;

    public static List<LambdaBehavior> loadFromFile() throws IOException {
        if (!FILE.exists() || FILE.length() == 0) {
            return new ArrayList<>();
        }
        return MAPPER.readValue(FILE, new TypeReference<>() {});
    }

    public static LambdaBehavior getFromFile(String behaviorId) throws IOException {
        List<LambdaBehavior> behaviors = loadFromFile();
        return behaviors.stream()
                .filter(b -> b.getId().equals(behaviorId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Behavior with id '" + behaviorId + "' not found"));
    }


    public static void addBehaviorToFile(LambdaBehavior behavior) throws IOException {
        List<LambdaBehavior> behaviors = loadFromFile();
        behaviors.add(behavior);
        saveToFile(behaviors);
    }

    public static void removeBehaviorFromFile(String behaviorId) throws IOException {
        List<LambdaBehavior> behaviors = loadFromFile();
        boolean removed = behaviors.removeIf(b -> b.getId().equals(behaviorId));
        if (!removed) {
            throw new IllegalArgumentException("Behavior with id '" + behaviorId + "' not found");
        }
        saveToFile(behaviors);
    }

    public static boolean existsInFile(String behaviorId) throws IOException {
        List<LambdaBehavior> behaviors = loadFromFile();
        return behaviors.stream().anyMatch(b -> b.getId().equals(behaviorId));
    }


    private static void saveToFile(List<LambdaBehavior> behaviors) throws IOException {
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE, behaviors);
    }
}
