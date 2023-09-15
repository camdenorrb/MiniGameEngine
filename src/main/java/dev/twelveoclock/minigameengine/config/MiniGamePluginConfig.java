package dev.twelveoclock.minigameengine.config;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the `config.toml` file's data
 */
public record MiniGamePluginConfig(
    @NotNull String name,
    @NotNull String description,
    @NotNull String version,
    @NotNull String author,
    @NotNull String mainClassPath
) {

    public MiniGamePluginConfig(
        @NotNull final String name,
        @NotNull final String description,
        @NotNull final String version,
        @NotNull final String author,
        @NotNull final String mainClassPath
    ) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.mainClassPath = mainClassPath;
    }

}