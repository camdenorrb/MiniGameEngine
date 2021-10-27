package dev.twelveoclock.minigameengine.minigame.data;

import java.util.List;

/**
 * @param scores The scores that makes up a MiniGame
 */
public record Stats(
    List<Score> scores
) {

    /**
     * @return The average score to determine performance
     */
    public float averageScore() {
        return (float) (scores.stream().mapToDouble(Score::value).sum() / scores.size());
    }

}
