package dev.twelveoclock.minigameengine.minigame.plugin;

import dev.twelveoclock.minigameengine.MiniGameEnginePlugin;
import dev.twelveoclock.minigameengine.minigame.MiniGame;

public abstract class MiniGamePlugin {

    // Set by the loader

    protected Data data;

    protected MiniGameEnginePlugin engine;


    /**
     * Creates a MiniGame instance based on the plugin
     *
     * @return The created MiniGame instance
     */
    public abstract MiniGame createGame();


    /**
     * @return The data loaded for the MiniGame plugin
     */
    public Data getData() {
        return data;
    }

    /**
     * @return The instance of the MiniGame engine
     */
    public MiniGameEnginePlugin getEngine() {
        return engine;
    }


    /**
     * The data stored in `minigame.toml`
     *
     * @param name The name of the MiniGame
     * @param version The version of the MiniGame
     */
    public record Data(
        String name,
        String version
    ) {}

}
