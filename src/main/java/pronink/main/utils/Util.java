package pronink.main.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import pronink.main.Main;

import java.time.Instant;

public class Util {
    public static Plugin getPlugin() {
        return Main.getPlugin(Main.class);
    }

    public static World getWorld(World.Environment environment) {
        if (environment == World.Environment.NETHER) {
            return Bukkit.getWorld("world_nether");
        } else if (environment == World.Environment.THE_END) {
            return Bukkit.getWorld("world_the_end");
        }
        return Bukkit.getWorld("world");
    }

    public static void runAfter(long tick, Runnable runnable) {
        Util.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Util.getPlugin(), runnable, tick);
    }

    public static void runEach(Runnable runnable) {
        runEach(20, runnable);
    }

    public static void runEach(long tickInterval, Runnable runnable) {
        Util.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Util.getPlugin(), runnable, 0, tickInterval);
    }

    public static void runEach(long tickInterval, long fromTick, long toTick, Runnable runnable) {
        int taskId = Util.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Util.getPlugin(), runnable, fromTick, tickInterval);
        Util.runAfter(toTick, () -> Util.getPlugin().getServer().getScheduler().cancelTask(taskId));
    }

    public static void runEach(long tickInterval, long startTick, long toTick, Runnable runnable, Runnable runnableAfterEnd) {
        Util.runEach(tickInterval, startTick, toTick, runnable);
        Util.runAfter(toTick + 1, runnableAfterEnd);
    }

    public static void registerEventsListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Util.getPlugin().getServer().getPluginManager().registerEvents(listener, Util.getPlugin());
        }
    }

    public static long getTimestamp() {
        return Instant.now().getEpochSecond();
    }

    public static boolean isChunkLoaded(Location location) {
        return location.getWorld().isChunkLoaded(location.getBlockX() / 16, location.getBlockZ() / 16);
    }

    public static void setEntityLookLocation(Entity entity, Location targetLocation) {
        Vector dirBetweenLocations = targetLocation.toVector().subtract(entity.getLocation().toVector());
        Location entityLocation = entity.getLocation();
        entityLocation.setDirection(dirBetweenLocations);
        entity.teleport(entityLocation);
    }

}
