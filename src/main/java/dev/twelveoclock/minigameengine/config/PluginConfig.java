package dev.twelveoclock.minigameengine.config;

import dev.twelveoclock.minigameengine.utils.ChatUtils;

/**
 * Represents the `config.toml` file's data
 */
public record PluginConfig(String def) {

    public PluginConfig(final String catName) {
        this.catName = ChatUtils.colorize(catName);
    }

}