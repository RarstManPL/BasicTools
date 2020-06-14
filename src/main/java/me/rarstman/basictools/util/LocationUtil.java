package me.rarstman.basictools.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationUtil {

    public static String locationToString(final Location location) {
        return "X: " + location.getBlockX() + ", Y: " + location.getBlockY() + ", Z: " + location.getBlockZ();
    }

    public static Set<World> getWorlds(final String name){
        if(name.equalsIgnoreCase("*")){
            return Bukkit.getWorlds().stream().collect(Collectors.toSet());
        }

        if(Bukkit.getWorld(name) == null){
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(Bukkit.getWorld(name)));
    }
}
