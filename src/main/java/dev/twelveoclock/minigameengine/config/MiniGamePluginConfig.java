package dev.twelveoclock.minigameengine.config;

/**
 * Represents the `config.toml` file's data
 */
public record MiniGamePluginConfig(String name, String mainClassPath) {

    public MiniGamePluginConfig(final String name, final String mainClassPath) {
        this.name = name;
        this.mainClassPath = mainClassPath;
    }

}