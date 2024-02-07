package dev.twelveoclock.minigameengine.minigame.stage;

import java.util.UUID;

/**
 * Stores data about the Stage for loading purposes
 *
 * @param name The name of the stage
*/
public record StageData(
    String name,
    UUID worldUUID

    // Keep parts separate from the stage data

    //List<PartData> parts // The loader will load this from a folder
    //Map<Marker, List<BlockPosition>> markers // The loader to make this relative -> absolute
    //Map<String, Path> partSchematics This can just be in a folder
) {}