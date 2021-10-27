package dev.twelveoclock.minigameengine.minigame.data;

/**
 * @param name The name for the score
 * @param value A value between -1.0 and 1.0
 */
public record Score(
    String name,
    float value
) {}
