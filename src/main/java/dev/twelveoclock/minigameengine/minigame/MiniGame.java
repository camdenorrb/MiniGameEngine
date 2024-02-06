package dev.twelveoclock.minigameengine.minigame;

import dev.twelveoclock.minigameengine.minigame.data.Stats;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;
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

    protected State state = State.WAITING_ON_PLAYERS;


    public MiniGame(final T plugin) {
        this.plugin = plugin;
    }

    protected abstract boolean onStart();

    protected abstract boolean onStop();

    protected abstract void onPlayerJoin(Player player);


    public final void playerJoin(Player player) {
        players.add(player);
        onPlayerJoin(player);
    }

    public final void waitOnPlayers() {
        state = State.WAITING_ON_PLAYERS;
    }

    /**
     * Called when the MiniGame is started
     */
    public final void start() {
        if (onStart()) {
            state = State.RUNNING;
        }
    }

    /**
     * Called when the MiniGame is stopped
     */
    public final void stop() {
        if (onStop()) {
            state = State.STOPPED;
        }
    }


    /**
     * @return The stats for players in the MiniGame
     */
    public Map<UUID, Stats> getStats() {
        return stats;
    }


    public State getState() {
        return state;
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


    public Stage loadStage(final String name) {
        // Make stageData relative to location
    }


    public enum State {
        WAITING_ON_PLAYERS,
        RUNNING,
        STOPPED,
    }

}
