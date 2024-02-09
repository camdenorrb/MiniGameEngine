package dev.twelveoclock.minigameengine.minigame.plugin;

import dev.twelveoclock.minigameengine.MiniGameEnginePlugin;
import dev.twelveoclock.minigameengine.config.MiniGamePluginConfig;
import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.marker.Marker;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;
import dev.twelveoclock.minigameengine.minigame.stage.StageBuilder;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public abstract class MiniGamePlugin {

    // Set by the loader
    protected MiniGamePluginConfig config;

    // Set by the loader
    protected MiniGameEnginePlugin engine;

    // Set by the loader
    protected JavaPlugin javaPlugin;

    // Set by the loader
    protected Path dataFolder;

    // Set by the loader
    //protected Stage[] stages;
    // TODO: Instead we will build a list of stages from file names

    /**
     * Creates a MiniGame instance based on the plugin
     *
     * @return The created MiniGame instance
     */
    protected abstract MiniGame<?> createGame();

    protected abstract Map<String, StageBuilder<? extends Stage>> getStageBuilders();


    // public abstract Class<Marker> getMarkerClass(); // TODO: This will be defined in stage instead, ideally not defined at all

    public void load() {}

    public void enable() {}

    public void disable() {}


    public abstract List<? extends Marker> getMarkers();


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

    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public String[] listStages() {

        // List stages from file names
        //return getStageBuilders().keySet().toArray(new String[0]);
        return null;
        // TODO: Implement this
    }

    public Stage loadStage(String stageName) {
        // Load stage from file
       // Stage

        // File structure:
        // stages/ - Folder
        //   stageName/ - Folder

        //   stageName.stage - Stage data in protobuf
        //   stageName/ - Folder
        //    partName.schematic - Schematic data in protobuf
        return null;
    }

}
