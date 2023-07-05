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
			case "build" -> build(sender, command, label, args);
			case "setmainspawn" -> setMainSpawn(sender, command, label, args);
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
	private void build(
		@NotNull final CommandSender sender,
		@NotNull final Command command,
		@NotNull final String label,
		@NotNull final String[] args
	) {

	}

	/**
	 * A command to set the main spawn for a MiniGame
	 *
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 */
	private void setMainSpawn(
			@NotNull final CommandSender sender,
			@NotNull final Command command,
			@NotNull final String label,
			@NotNull final String[] args
	) {

	}
}
