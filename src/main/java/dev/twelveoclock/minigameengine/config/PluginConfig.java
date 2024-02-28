package dev.twelveoclock.minigameengine.config;

import dev.twelveoclock.minigameengine.utils.ChatUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the `config.toml` file's data
 */
public record PluginConfig(@NotNull String catName) {

    public PluginConfig(@NotNull final String catName) {
        this.catName = ChatUtils.colorize(catName);
    }

}