package dev.twelveoclock.minigameengine.minigame.stage;

import dev.twelveoclock.minigameengine.minigame.marker.Marker;
import dev.twelveoclock.minigameengine.position.BlockPosition;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Stores data about the Stage for loading purposes
 *
 * @param name The name of the stage
 * @param markers The markers for the stage
 * @param partSchematics The path of the schematics that make up the stage
 */
public record StageData(
    String name,
    UUID worldUUID,
    Map<Marker, List<BlockPosition>> markers, // The loader to make this relative -> absolute
    //Map<String, Path> partSchematics This can just be in a folder
) {}