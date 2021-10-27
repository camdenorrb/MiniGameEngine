package dev.twelveoclock.minigameengine.minigame.stage;

import dev.twelveoclock.minigameengine.minigame.marker.Marker;
import dev.twelveoclock.minigameengine.position.BlockPosition;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Stores data about the Stage for loading purposes
 *
 * @param name The name of the stage
 * @param markers The markers for the stage
 * @param partSchematics The path of the schematics that make up the stage
 */
public final record StageData(
    String name,
    Map<Marker, List<BlockPosition>> markers,
    Map<String, Path> partSchematics
) {}