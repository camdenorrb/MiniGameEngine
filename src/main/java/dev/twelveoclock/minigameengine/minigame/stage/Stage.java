package dev.twelveoclock.minigameengine.minigame.stage;

import java.nio.file.Path;

public abstract class Stage {

    // Set by the loader
    protected String name;

    // Set by the loader
    protected StageData data;

    // Set by the loader
    protected Path dataFolder;

    /**
     * Called when the stage is started
     */
    protected abstract void start();

    /**
     * Called when the stage is stopped
     */
    protected abstract void stop();


    public String getName() {
        return name;
    }

    public StageData getData() {
        return data;
    }

    public Path getDataFolder() {
        return dataFolder;
    }

}

