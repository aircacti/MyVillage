package com.lambdaprofessional.myvillage.utils;


import com.lambdaprofessional.myvillage.helpers.Helpers;
import org.bukkit.Registry;
import org.bukkit.entity.Villager;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class VillagerUtils {

    public static boolean isValidProfession(String anyCaseProfession) {
        if (anyCaseProfession == null) return false;
        String lower = anyCaseProfession.toLowerCase();
        return Helpers.VillagerProfessions.getLowercase().contains(lower);
    }

    public static boolean isValidType(String anyCaseType) {
        if (anyCaseType == null) return false;
        String lower = anyCaseType.toLowerCase();
        return Helpers.VillagerTypes.getLowercase().contains(lower);
    }

    public static Villager.Profession getProfessionByName(String anyCaseProfession) {
        if (anyCaseProfession == null) return null;
        String lower = anyCaseProfession.toLowerCase();

        Optional<Villager.Profession> prof = StreamSupport.stream(Registry.VILLAGER_PROFESSION.spliterator(), false)
                .filter(p -> p.toString().toLowerCase().equals(lower))
                .findFirst();

        return prof.orElse(null);
    }

    public static String getProfessionName(Villager.Profession profession) {
        if (profession == null) return null;
        return profession.toString().toLowerCase();
    }

    public static Villager.Type getTypeByName(String anyCaseType) {
        if (anyCaseType == null) return null;
        String lower = anyCaseType.toLowerCase();

        Optional<Villager.Type> type = StreamSupport.stream(Registry.VILLAGER_TYPE.spliterator(), false)
                .filter(t -> t.toString().toLowerCase().equals(lower))
                .findFirst();

        return type.orElse(null);
    }

    public static String getTypeName(Villager.Type type) {
        if (type == null) return null;
        return type.toString().toLowerCase();
    }


}
