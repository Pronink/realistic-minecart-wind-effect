package pronink.main;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;
import pronink.main.utils.Debug;
import pronink.main.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class MinecartEventListener implements Listener {
    private final ArrayList<Location> recentLocations = new ArrayList<>();

    @EventHandler
    public void onMinecartMove(VehicleMoveEvent event) {
        if (event.getVehicle() instanceof Minecart minecart) {
            // Prevent if no player are inside the minecart
            if (minecart.getPassengers().isEmpty()) return;
            if (!(minecart.getPassengers().get(0) instanceof Player)) return;

            // Prevent execute sounds once per minecart block
            final Location minecartLocation = minecart.getLocation();
            if (recentLocations.stream().anyMatch(location ->
                    location.getBlockX() == minecartLocation.getBlockX() &&
                            location.getBlockY() == minecartLocation.getBlockY() &&
                            location.getBlockZ() == minecartLocation.getBlockZ())
            ) return;

            // Add current minecart location to prevent future executions
            recentLocations.add(minecartLocation);
            Util.runAfter(20, () -> recentLocations.remove(minecartLocation)); // Remove after 1 second

            float speed = (float) minecart.getVelocity().length(); // Speed from 0 to 2.054
            if (speed < 0.2f) return;

            Vector unitVectorVelocity = minecart.getVelocity().normalize();

            Location rightLocation = minecart.getLocation().add(unitVectorVelocity.clone().rotateAroundY(1.5708));
            Location rightBackLocation = rightLocation.clone().subtract(unitVectorVelocity);
            Location rightAboveLocation = rightLocation.clone().add(0, 1, 0);
            Location rightBackAboveLocation = rightBackLocation.clone().add(0, 1, 0);

            Location leftLocation = minecart.getLocation().add(unitVectorVelocity.clone().rotateAroundY(-1.5708));
            Location leftBackLocation = leftLocation.clone().subtract(unitVectorVelocity);
            Location leftAboveLocation = leftLocation.clone().add(0, 1, 0);
            Location leftBackAboveLocation = leftBackLocation.clone().add(0, 1, 0);

            Location centerAboveLocation = minecart.getLocation().add(0, 2.25f, 0);
            Location centerBackAboveLocation = centerAboveLocation.clone().subtract(unitVectorVelocity);

            //Bukkit.broadcastMessage(speed + "");
            //Debug.drawPoint(rightLocation, Color.PURPLE);
            //Debug.drawPoint(rightBackLocation, Color.RED);
            //Debug.drawPoint(rightAboveLocation, Color.PURPLE);
            //Debug.drawPoint(rightBackAboveLocation, Color.RED);

            //Debug.drawPoint(leftLocation, Color.ORANGE);
            //Debug.drawPoint(leftBackLocation, Color.YELLOW);
            //Debug.drawPoint(leftAboveLocation, Color.ORANGE);
            //Debug.drawPoint(leftBackAboveLocation, Color.YELLOW);

            //Debug.drawPoint(centerAboveLocation, Color.GREEN);
            //Debug.drawPoint(centerBackAboveLocation, Color.LIME);

            float randomPitch = 0.2f + (new Random().nextFloat() / 2f); // From 0.2 to 0.7

            Location[] rightLocations = new Location[]{rightLocation, rightBackLocation, rightAboveLocation, rightBackAboveLocation};
            Location[] leftLocations = new Location[]{leftLocation, leftBackLocation, leftAboveLocation, leftBackAboveLocation};
            Location[] topLocations = new Location[]{centerAboveLocation, centerBackAboveLocation};

            // Should sound right
            if (!areAllBlocksSameType(rightLocations)) {
                playWind(rightLocation, 0.7f, randomPitch);
                playWind(rightLocation, 0.7f, randomPitch + 0.4f);
            }
            // Should sound left
            if (!areAllBlocksSameType(leftLocations)) {
                playWind(leftLocation, 0.7f, randomPitch);
                playWind(leftLocation, 0.7f, randomPitch + 0.4f);
            }
            // Should sound top
            if (!areAllBlocksSameType(topLocations)) {
                playWind(centerAboveLocation, 0.7f, randomPitch);
                playWind(centerAboveLocation, 0.7f, randomPitch + 0.4f);
            }

            // Should sound if all locations are passable (tunnel)
            boolean shouldSoundTunnel = Arrays.stream(new Location[][]{rightLocations, leftLocations})
                    .allMatch(this::areAllBlocksPassable);
            if (shouldSoundTunnel) {
                playWind(minecart.getLocation(), 0.7f, 0.1f);
            }
        }
    }

    private void playWind(Location location, float volume, float pitch) {
        location.getWorld().playSound(location, Sound.ENTITY_HORSE_BREATHE, volume, pitch);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean areAllBlocksSameType(Location[] locations) {
        // If all blocks are air, or all blocks are non-air, then all blocks are the same type. Only testing if block is air/non-air
        return Arrays.stream(locations).allMatch(location -> location.getBlock().isPassable()) ||
                Arrays.stream(locations).noneMatch(location -> location.getBlock().isPassable());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean areAllBlocksPassable(Location[] locations) {
        return Arrays.stream(locations).noneMatch(location -> location.getBlock().isPassable());
    }
}
