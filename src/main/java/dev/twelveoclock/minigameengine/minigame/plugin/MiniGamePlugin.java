package dev.twelveoclock.minigameengine.minigame.plugin;

import dev.twelveoclock.minigameengine.MiniGameEnginePlugin;
import dev.twelveoclock.minigameengine.config.MiniGamePluginConfig;
import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;

import java.nio.file.Path;

public abstract class MiniGamePlugin {

    // Set by the loader
    protected MiniGamePluginConfig config;

    // Set by the loader
    protected MiniGameEnginePlugin engine;

    // Set by the loader
    protected Path dataFolder;

    // Set by the loader
    protected Stage[] stages;

    /**
     * Creates a MiniGame instance based on the plugin
     *
     * @return The created MiniGame instance
     */
    protected abstract MiniGame<?> createGame();


    // public abstract Class<Marker> getMarkerClass(); // TODO: This will be defined in stage instead, ideally not defined at all

    public void load() {}

    public void enable() {}

    public void disable() {}


    /**
     * @return The instance of the MiniGame engine
     */
    public MiniGameEnginePlugin getEngine() {
        return engine;
    }

    /**
     * @return The instance of the config
     */
    public MiniGamePluginConfig getConfig() {
        return config;
    }

    /**
     * @return The instance of the data folder
     */
    public Path getDataFolder() {
        return dataFolder;
    }

}
