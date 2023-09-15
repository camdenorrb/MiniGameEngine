package dev.twelveoclock.minigameengine.lobby;

import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.module.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lobby extends PluginModule {

	private State state;

	private final int minPlayers, maxPlayers;


	public Lobby(final JavaPlugin plugin, final MiniGame<?> miniGame, final int minPlayers, final int maxPlayers) {
		super(plugin);
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
	}


	public void start() {
		// TODO: Move players to game
	}

	public void end() {

	}


	private void changeState(final State state) {
		switch (state) {
			case STARTING -> start();
			case ENDING -> end();
		}
	}


	public enum State {
		/**
		 * The lobby is waiting for players to join
		 */
		WAITING,
		/**
		 * The lobby is starting the game
		 */
		STARTING,
		/**
		 * The lobby is in-game
		 */
		IN_GAME,
		/**
		 * The lobby is ending the game
		 */
		ENDING
	}

}
