package dev.twelveoclock.minigameengine.minigame.plugin;

import dev.twelveoclock.minigameengine.MiniGameEnginePlugin;

public abstract class MiniGamePlugin {

    // Set by the loader
    protected MiniGameEnginePlugin engine;


    /**
     * @return The instance of the MiniGame engine
     */
    public MiniGameEnginePlugin getEngine() {
        return engine;
    }

}
