package dev.twelveoclock.plugintemplate.config;

import com.moandjiezana.toml.Toml;
import dev.twelveoclock.plugintemplate.utils.ChatUtils;

/**
 * Represents the `config.toml` file's data
 */
public record PluginConfig(String catName) {

    public PluginConfig(final String catName) {
        this.catName = ChatUtils.colorize(catName);
    }

    public static PluginConfig from(final Toml toml) {
        return new PluginConfig(toml.getString("catName", "&c&lMidnight"));
    }

}