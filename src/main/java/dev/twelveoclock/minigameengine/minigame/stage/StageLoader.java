package dev.twelveoclock.minigameengine.minigame.stage;


import java.util.List;

public interface StageLoader {

    Stage load(final StageData stageData, final List<PartData> partData);

    // Unloads the stage from the world
    void unload(final Stage stage);

}
