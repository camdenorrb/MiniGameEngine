package dev.twelveoclock.minigameengine.commands;

import dev.twelveoclock.minigameengine.minigame.modules.MiniGamesModule;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class MiniGameCommand implements CommandExecutor {

	private final MiniGamesModule miniGamesModule;


	public MiniGameCommand(final MiniGamesModule miniGamesModule) {
		this.miniGamesModule = miniGamesModule;
	}


	@Override
	public boolean onCommand(
		@NotNull final CommandSender sender,
		@NotNull final Command command,
		@NotNull final String label,
		@NotNull final String[] args
	) {

		switch (args[0].toLowerCase()) {
			case "start" -> start(sender, command, label, args);
			case "list"  -> list(sender);
			case "stop"  -> stop(sender, command, label, args);
			case "setup" -> setup(sender, command, label, args);
			case "config" -> config(sender, command, label, args); // TODO: GUI with options like auto start
			case "setLobby" -> setLobby(sender, command, label, args); // TODO: Lobby where all players are teleported to, can be per game
			case "delete" -> delete(sender, command, label, args); // TODO: Delete game
			case "setInstances" -> setInstances(sender, command, label, args); // TODO: Set how many of each game
		}

		return true;
	}


	/**
	 * A command to start a MiniGame
	 *
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 */
	private void start(
		@NotNull final CommandSender sender,
		@NotNull final Command command,
		@NotNull final String label,
		@NotNull final String[] args
	) {

	}

	/**
	 *	A command to list the MiniGames currently loaded
	 *
	 * 	Output Format
	 *  ```
	 * 	MiniGames:
	 * 		+ Name1
	 * 		+ Name2
	 *  ```
	 *
	 * @param sender The person who executed the command
	 */
	private void list(
		@NotNull final CommandSender sender
	) {

		final StringBuilder builder = new StringBuilder(ChatColor.GOLD + ChatColor.BOLD.toString() + "MiniGames:");

		for (final MiniGamePlugin miniGamePlugin : miniGamesModule.getMiniGamePlugins()) {
			builder.append("\n    +").append(miniGamePlugin.getData().name());
		}

		sender.sendMessage(builder.toString());
	}

	/**
	 * A command to stop a MiniGame
	 *
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 */
	private void stop(
		@NotNull final CommandSender sender,
		@NotNull final Command command,
		@NotNull final String label,
		@NotNull final String[] args
	) {

	}

	/**
	 * A command to construct a stage for a MiniGame
	 *
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 */
	private void setup(
		@NotNull final CommandSender sender,
		@NotNull final Command command,
		@NotNull final String label,
		@NotNull final String[] args
	) {

	}

	/**
	 * A command to see a menu for all the enabled/disabled minigames
	 *
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 */
	private void minigames(
			@NotNull final CommandSender sender,
			@NotNull final Command command,
			@NotNull final String label,
			@NotNull final String[] args
	) {
		command
	}
}
