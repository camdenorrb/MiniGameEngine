package dev.twelveoclock.minigameengine.module;

import dev.twelveoclock.minigameengine.minigame.MiniGame;

public final class MiniGamesModule {

	private final MiniGamePluginLoaderModule pluginLoaderModule;

	private final SelectionMode selectorMode;


	public MiniGamesModule(final MiniGamePluginLoaderModule pluginLoaderModule, final SelectionMode selectorMode) {
		this.pluginLoaderModule = pluginLoaderModule;
		this.selectorMode = selectorMode;
	}


	public MiniGamePluginLoaderModule getPluginLoaderModule() {
		return pluginLoaderModule;
	}

	public SelectionMode getSelectorMode() {
		return selectorMode;
	}


	public enum SelectionMode {
		/**
		 * The MiniGame is selected at random
		 */
		RANDOM,
		/**
		 * The MiniGame is selected by vote
		 */
		VOTE,
		/**
		 * The MiniGame is selected by the relative BungeeCord server
		 */
		BUNGEE,
	}

}
