package com.lambdaprofessional.myvillage.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Locale;

public class LocationUtils {
    public static String locationToString(Location loc) {
        if (loc == null) return "";
        return String.format(Locale.ENGLISH, "%s;%f;%f;%f;%f;%f",
                loc.getWorld().getName(),
                loc.getX(), loc.getY(), loc.getZ(),
                loc.getYaw(), loc.getPitch());
    }

    public static Location stringToLocation(String str) {
        if (str == null || str.isEmpty()) return null;
        String[] parts = str.split(";");
        if (parts.length < 6) return null;

        World world = Bukkit.getWorld(parts[0]);
        if (world == null) return null;

        String xStr = parts[1].replace(',', '.');
        String yStr = parts[2].replace(',', '.');
        String zStr = parts[3].replace(',', '.');
        String yawStr = parts[4].replace(',', '.');
        String pitchStr = parts[5].replace(',', '.');

        double x = Double.parseDouble(xStr);
        double y = Double.parseDouble(yStr);
        double z = Double.parseDouble(zStr);
        float yaw = Float.parseFloat(yawStr);
        float pitch = Float.parseFloat(pitchStr);

        return new Location(world, x, y, z, yaw, pitch);
    }
}
