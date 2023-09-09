package dev.twelveoclock.minigameengine.minigame.plugin;

import dev.twelveoclock.minigameengine.MiniGameEnginePlugin;
import dev.twelveoclock.minigameengine.config.MiniGamePluginConfig;
import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.marker.Marker;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MiniGamePlugin {

    // Set by the loader
    protected MiniGamePluginConfig config;

    protected MiniGameEnginePlugin engine;


    /**
     * Creates a MiniGame instance based on the plugin
     *
     * @return The created MiniGame instance
     */
    public abstract MiniGame createGame();


    /**
     * @return The marker class for the MiniGame
     */
    public abstract Class<Marker> getMarkerClass();


    public void load() {}

    public void enable() {}

    public void disable() {}


    /**
     * @return The instance of the MiniGame engine
     */
    public MiniGameEnginePlugin getEngine() {
        return engine;
    }

}
