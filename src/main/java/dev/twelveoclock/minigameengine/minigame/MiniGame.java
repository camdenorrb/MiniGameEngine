package dev.twelveoclock.minigameengine.minigame;

import dev.twelveoclock.minigameengine.minigame.data.Stats;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import dev.twelveoclock.minigameengine.minigame.team.Team;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class MiniGame<T extends MiniGamePlugin> {

    protected Map<UUID, Stats> stats = new HashMap<>();

    protected Set<Player> players = new HashSet<>();

    // Maybe move teams to stage? // No, we will have multiple minigame instances
    protected Map<Team, Player> teams = new HashMap<>();


    // Set by the loader
    // protected Stage stage;

    protected T plugin;

    public MiniGame(final T plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when the MiniGame is started
     */
    protected abstract void start();

    /**
     * Called when the MiniGame is stopped
     */
    protected abstract void stop();


    /**
     * @return The stats for players in the MiniGame
     */
    public Map<UUID, Stats> getStats() {
        return stats;
    }

    /**
     * @return The players in the MiniGame
     */
    public Set<Player> getPlayers() {
        return players;
    }

    /**
     * @return The MiniGame plugin
     */
    public T getPlugin() {
        return plugin;
    }

}
