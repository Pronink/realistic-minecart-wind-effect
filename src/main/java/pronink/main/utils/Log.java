package pronink.main.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

// Class to center all logs created by Clancraft plugin, and then do nice things
public class Log {
    private static String getLogWaterMark() {
        return ChatColor.BOLD + "[RMS]" + ChatColor.RESET;
    }

    public static void error(String message) {
        String formattedMsg = ChatColor.RED + "[ERROR] " + message;
        toTerminal(getLogWaterMark() + formattedMsg);
        toOps(getLogWaterMark() + formattedMsg);
    }

    public static void info(String message) {
        String formattedMsg = ChatColor.AQUA + "[INFO] " + message;
        toTerminal(getLogWaterMark() + formattedMsg);
        toOps(getLogWaterMark() + formattedMsg);
    }

    public static void toTerminal(String text) {
        // Native implementation, but it only uses 8 colors instead 16, using bold instead brighter colors
        // Util.getPlugin().getServer().getConsoleSender().sendMessage(text);
        System.out.println(ColorParser.parseMinecraftToTerminal(text));
    }

    public static void toOps(String text) {
        for (Player player : Util.getPlugin().getServer().getOnlinePlayers()) {
            if (player.isOp()) {
                player.sendMessage(text);
            }
        }
    }
}
