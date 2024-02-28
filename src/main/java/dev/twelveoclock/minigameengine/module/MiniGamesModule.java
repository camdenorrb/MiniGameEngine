package dev.twelveoclock.minigameengine.module;

import dev.twelveoclock.minigameengine.MiniGameEnginePlugin;
import dev.twelveoclock.minigameengine.commands.MiniGameCommand;
import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MiniGamesModule extends PluginModule {

	private final MiniGamePluginLoaderModule pluginLoaderModule;

	private final SelectionMode selectorMode;

	private final Map<MiniGamePlugin, List<MiniGame<?>>> miniGamesByPlugin = new HashMap<>();

	private final Method createGameMethod, startGameMethod, stopGameMethod;

	private final MiniGameCommand minigameCommand;

	public MiniGamesModule(
		final MiniGameEnginePlugin plugin,
		final SelectionMode selectorMode
	) {
		this(plugin, new MiniGamePluginLoaderModule(plugin), selectorMode);
	}

	public MiniGamesModule(
		final MiniGameEnginePlugin plugin,
		final MiniGamePluginLoaderModule pluginLoaderModule,
		final SelectionMode selectorMode
	) {

		super(plugin);

		this.pluginLoaderModule = pluginLoaderModule;
		this.selectorMode = selectorMode;
		this.minigameCommand = new MiniGameCommand(this.plugin, this);

		try {

			this.createGameMethod = MiniGamePlugin.class.getDeclaredMethod("createGame");
			this.startGameMethod = MiniGame.class.getDeclaredMethod("start");
			this.stopGameMethod = MiniGame.class.getDeclaredMethod("stop");

			this.createGameMethod.setAccessible(true);
			this.startGameMethod.setAccessible(true);
			this.stopGameMethod.setAccessible(true);
		}
		catch (final NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	protected void onEnable() {
		pluginLoaderModule.enable();
		plugin.getCommand("minigame").setExecutor(minigameCommand);
	}

	@Override
	protected void onDisable() {
		plugin.getCommand("minigame").setExecutor(null);
		pluginLoaderModule.disable();
	}


	// TODO: Dynamically set this based on queued players if configured to do so
	public void setInstances(final int amount, final MiniGamePlugin plugin) {

		if (amount == 0) {
			miniGamesByPlugin.remove(plugin);
			return;
		}

		final List<MiniGame<?>> miniGames = this.miniGamesByPlugin.computeIfAbsent(plugin, k -> new ArrayList<>());
		final int size = miniGames.size();

		if (size == amount) {
			return;
		}

		try {
			if (size > amount) {
				for (int i = size - 1; i >= amount; i--) {
					// TODO: Wait for games to finish before removing them, can use bukkit task to check if game is running
					stopGameMethod.invoke(miniGames.remove(i));
				}
			} else {
				for (int i = size; i < amount; i++) {
					miniGamesByPlugin.computeIfAbsent(plugin, k -> new ArrayList<>()).add(createMiniGame(plugin));
				}
			}
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}

	}

	private MiniGame<?> createMiniGame(final MiniGamePlugin plugin) {
		try {
			return (MiniGame<?>) createGameMethod.invoke(plugin);
		}
		catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}


	public MiniGamePluginLoaderModule getPluginLoaderModule() {
		return pluginLoaderModule;
	}

	public SelectionMode getSelectorMode() {
		return selectorMode;
	}

	public Map<String, List<MiniGame<?>>> getMiniGames() {

		// LowerCase name -> MiniGame
		final Map<String, List<MiniGame<?>>> miniGames = new HashMap<>(miniGamesByPlugin.size());

		for (final Map.Entry<MiniGamePlugin, List<MiniGame<?>>> entry : miniGamesByPlugin.entrySet()) {
			miniGames.put(entry.getKey().getConfig().name().toLowerCase(), entry.getValue());
		}

		return miniGames;
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
