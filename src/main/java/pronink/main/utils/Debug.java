package pronink.main.utils;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.List;

public class Debug {
    public static void drawLine(Location point1, Location point2, Color color) {
        point1 = point1.clone();
        point1.setX(point1.getBlockX() + 0.5d);
        point1.setY(point1.getBlockY() + 0.5d);
        point1.setZ(point1.getBlockZ() + 0.5d);
        point2 = point2.clone();
        point2.setX(point2.getBlockX() + 0.5d);
        point2.setY(point2.getBlockY() + 0.5d);
        point2.setZ(point2.getBlockZ() + 0.5d);
        World world = point1.getWorld();
        double distance = point1.distance(point2);
        double space = 1;
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            Location location = new Location(world, p1.getX(), p1.getY(), p1.getZ());
            world.spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, new Particle.DustOptions(color, 1), true);
            length += space;
        }
    }

    public static void drawPoint(Location point, Color color) {
        point = point.clone();
        World world = point.getWorld();
        world.spawnParticle(Particle.REDSTONE, point, 1, 0, 0, 0, 0, new Particle.DustOptions(color, 2), true);
    }

    public static void drawPointCentered(Location point, Color color) {
        point = point.clone();
        World world = point.getWorld();
        point.setX(point.getBlockX() + 0.5d);
        point.setY(point.getBlockY() + 0.5d);
        point.setZ(point.getBlockZ() + 0.5d);
        world.spawnParticle(Particle.REDSTONE, point, 1, 0, 0, 0, 0, new Particle.DustOptions(color, 2), true);
    }

    public static Runnable printCpuUsage() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    double cpuLoad = Debug.getProcessCpuLoad();
                    Log.toTerminal(ChatColor.AQUA + "CPU USAGE: " + cpuLoad + "%\t" + "█".repeat((int) cpuLoad / 2) + "░".repeat((100 - (int) cpuLoad) / 2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static Runnable printMemoryUsage() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Runtime runtime = Runtime.getRuntime();
                    float total = runtime.totalMemory() / 1_000_000f;
                    float free = runtime.freeMemory() / 1_000_000f;
                    float used = total - free;
                    Log.toTerminal(ChatColor.LIGHT_PURPLE + "MEMORY: " + (int) used + " / " + (int) total + " MB\t" + "█".repeat(Math.round(used / 100f)) + "░".repeat(Math.round(free / 100f)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static Runnable printBukkitTasks() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    final Plugin myPlugin = Util.getPlugin();
                    final List<BukkitTask> tasks = Util.getPlugin().getServer().getScheduler().getPendingTasks();
                    final int allTasks = tasks.size();
                    final int myTasks = (int) tasks.stream().filter(t -> t.getOwner() == myPlugin).count();
                    Log.toTerminal(ChatColor.GOLD + "TASKS: " + myTasks + "/" + allTasks + "\t" + "█".repeat(myTasks / 1000) + "░".repeat((allTasks - myTasks) / 1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static double getProcessCpuLoad() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});

        if (list.isEmpty()) return Double.NaN;

        Attribute att = (Attribute) list.get(0);
        Double value = (Double) att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0) return Double.NaN;
        // returns a percentage value with 1 decimal point precision
        return ((int) (value * 1000) / 10.0);
    }
}
