package dev.twelveoclock.minigameengine.minigame;

import dev.twelveoclock.minigameengine.minigame.data.Stats;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public abstract class MiniGame {

    // Set by the loader

    protected Map<UUID, Stats> stats;

    protected Map<UUID, Player> players;


    protected Stage stage;


    /**
     * @return The name of the MiniGame
     */
    public abstract String getName();


    /**
     * @return The stats for players in the MiniGame
     */
    public Map<UUID, Stats> getStats() {
        return stats;
    }

    /**
     * @return The players in the MiniGame
     */
    public Map<UUID, Player> getPlayers() {
        return players;
    }

    /**
     * @return The current stage for the MiniGame
     */
    public Stage getStage() {
        return stage;
    }

}
