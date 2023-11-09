package pronink.main.utils;

import org.bukkit.ChatColor;

import java.util.EnumMap;
import java.util.Map;


// Transforms minecraft chat messages to terminal messages
// From https://github.com/SkinsRestorer/SkinsRestorerX/blob/0045d50e71e6508eeee370876ad2aa583c12a62c/shared/src/main/java/net/skinsrestorer/shared/utils/log/ANSIConverter.java#L29
// And from https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/command/ColouredConsoleSender.java
public class ColorParser {
    private static final Map<ChatColor, String> replacements = new EnumMap<>(ChatColor.class);

    static {
        replacements.put(ChatColor.BLACK, getTerminalCode(30));
        replacements.put(ChatColor.DARK_BLUE, getTerminalCode(34));
        replacements.put(ChatColor.DARK_GREEN, getTerminalCode(32));
        replacements.put(ChatColor.DARK_AQUA, getTerminalCode(36));
        replacements.put(ChatColor.DARK_RED, getTerminalCode(31));
        replacements.put(ChatColor.DARK_PURPLE, getTerminalCode(35));
        replacements.put(ChatColor.GOLD, getTerminalCode(33));
        replacements.put(ChatColor.GRAY, getTerminalCode(90));
        replacements.put(ChatColor.DARK_GRAY, getTerminalCode(90));
        replacements.put(ChatColor.BLUE, getTerminalCode(94));
        replacements.put(ChatColor.GREEN, getTerminalCode(92));
        replacements.put(ChatColor.AQUA, getTerminalCode(96));
        replacements.put(ChatColor.RED, getTerminalCode(91));
        replacements.put(ChatColor.LIGHT_PURPLE, getTerminalCode(95));
        replacements.put(ChatColor.YELLOW, getTerminalCode(93));
        replacements.put(ChatColor.WHITE, getTerminalCode(97));
        replacements.put(ChatColor.MAGIC, getTerminalCode(6));
        replacements.put(ChatColor.BOLD, getTerminalCode(1));
        replacements.put(ChatColor.STRIKETHROUGH, getTerminalCode(9));
        replacements.put(ChatColor.UNDERLINE, getTerminalCode(4));
        replacements.put(ChatColor.ITALIC, getTerminalCode(3));
        replacements.put(ChatColor.RESET, getTerminalCode(0));
    }

    // Transforms minecraft chat messages to terminal messages
    // Better alternative of getServer().getConsoleSender().sendMessage(text);
    public static String parseMinecraftToTerminal(String minecraftMessage) {
        String result = minecraftMessage + ChatColor.RESET;
        for (ChatColor color : ChatColor.values()) {
            result = result.replaceAll("(?i)" + color.toString(),
                    replacements.getOrDefault(color, ""));
        }
        return result;
    }

    private static String getTerminalCode(int colorCode) {
        char escapeChar = (char) 27;
        return escapeChar + "[" + colorCode + "m";
    }

}