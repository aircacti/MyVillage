package com.lambdaprofessional.myvillage.helpers;

import com.lambdaprofessional.myvillage.enums.LambdaBehaviorType;
import org.bukkit.Registry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Helpers {

    public static class VillagerTypes {

        public static List<String> get() {
            return StreamSupport.stream(Registry.VILLAGER_TYPE.spliterator(), false)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        public static List<String> getLowercase() {
            return get().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }

        public static String commaList = String.join(", ",
                get().stream().map(String::toLowerCase).toList());

        public static String dashList = get().stream()
                .map(s -> "- " + s.toLowerCase())
                .collect(Collectors.joining("\n"));
    }

    public static class VillagerProfessions {

        public static List<String> get() {
            return StreamSupport.stream(Registry.VILLAGER_PROFESSION.spliterator(), false)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        public static List<String> getLowercase() {
            return get().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }

        public static String commaList = String.join(", ",
                get().stream().map(String::toLowerCase).toList());

        public static String dashList = get().stream()
                .map(s -> "- " + s.toLowerCase())
                .collect(Collectors.joining("\n"));
    }

    public static class VillagerLevels {

        private static final List<String> levels = Arrays.asList(
                "1 (stone badge, novice)",
                "2 (iron badge, apprentice)",
                "3 (gold badge, journeyman)",
                "4 (emerald badge, expert)",
                "5 (diamond badge, master)"
        );

        public static List<String> get() {
            return levels;
        }

        public static String commaList = String.join(", ", levels);

        public static String dashList = levels.stream()
                .map(s -> "- " + s)
                .collect(Collectors.joining("\n"));
    }

    public static class BehaviorTypes {

        public static final String dashList = Arrays.stream(LambdaBehaviorType.values())
                .map(e -> "- %s".formatted(e.name().toLowerCase()))
                .collect(Collectors.joining("\n"));


    }

}
