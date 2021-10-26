package dev.twelveoclock.plugintemplate.utils;

import org.bukkit.ChatColor;

/**
 * Utilities to make chat stuff easier
 */
public final class ChatUtils {

    /**
     * Don't allow construction of a utility class
     */
    private ChatUtils() {}


    /**
     * Attempt to convert Minecraft color codes in a string to their UTF representation
     * @param input The text you want to colorize
     * @return The colorized text
     */
    public static String colorize(final String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
