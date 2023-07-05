package dev.twelveoclock.minigameengine.minigame.plugin;

import dev.twelveoclock.minigameengine.MiniGameEnginePlugin;
import dev.twelveoclock.minigameengine.minigame.MiniGame;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MiniGamePlugin extends JavaPlugin {

    // Set by the loader

    protected Data data;

    protected MiniGameEnginePlugin engine;


    /**
     * Creates a MiniGame instance based on the plugin
     *
     * @return The created MiniGame instance
     */
    public abstract MiniGame createGame();


    public void load() {}

    public void enable() {}

    public void disable() {}

    @Override
    public final void onLoad() {
        load();
    }

    @Override
    public final void onEnable() {
        enable();
    }

    @Override
    public final void onDisable() {
        disable();
    }

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
