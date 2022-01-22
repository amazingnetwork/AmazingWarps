package br.com.amazingnetwork.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {
    public static String serialize(Location location) {
        if (location == null) return "empty";

        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        return String.format(
                "%s;%s;%s;%s;%s;%s",
                world,
                x,
                y,
                z,
                yaw,
                pitch
        );
    }

    public static Location deserialize(String value) {
        if (value.equalsIgnoreCase("empty")) return null;

        String[] split = value.split(";");

        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }
}
