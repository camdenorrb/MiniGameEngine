package dev.twelveoclock.minigameengine.commands;

import dev.twelveoclock.minigameengine.gui.SetupPartGUI;
import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import dev.twelveoclock.minigameengine.module.MiniGamesModule;
import dev.twelveoclock.minigameengine.setup.PartSetup;
import dev.twelveoclock.minigameengine.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class MiniGameCommand implements CommandExecutor {

	private final JavaPlugin plugin;

	private final MiniGamesModule miniGamesModule;


	public MiniGameCommand(final JavaPlugin plugin, final MiniGamesModule miniGamesModule) {
		this.plugin = plugin;
		this.miniGamesModule = miniGamesModule;
	}


	@Override
	public boolean onCommand(
		@NotNull final CommandSender sender,
		@NotNull final Command command,
		@NotNull final String label,
		@NotNull final String[] args
	) {

		if (args.length == 0) {
			sendUsage(sender, args, "Please specify a subcommand.");
			return true;
		}

		switch (args[0].toLowerCase()) {
			//case "start" -> start(sender, command, label, args);
			// TODO: Remove this in favor of other methods of joining such as signs, gui, npc, etc
			case "join" -> join(sender, command, label, args);
			case "list"  -> list(sender, args); // TODO: Open GUI with all MiniGame plugins if arg 0 is "plugins" or "games"
			case "stop"  -> stop(sender, command, label, args); // TODO: Ask to confirm
			case "stage" -> stage(sender, command, label, args); // TODO: Open GUI with all stages of a MiniGame, with options to setup
			case "setup" -> setup(sender, command, label, args); // TODO: Walk through set up of a game stage
			case "setuppart" -> setupPart(sender, command, label, args); // TODO: Walk through set up of a game part
			case "createstage" -> createStage(sender, command, label, args); // TODO: Remove this in favor of `stage` command
			//case "config" -> config(sender, command, label, args); // TODO: GUI with options like auto start
			//case "setLobby" -> setLobby(sender, command, label, args); // TODO: Lobby where all players are teleported to, can be per game
			//case "delete" -> delete(sender, command, label, args); // TODO: Delete game
			case "setinstances" -> setInstances(sender, command, label, args); // TODO: Set how many of each game
			// TODO: Option to use GUI commands or Text only mode
			default -> sendUsage(sender, args, "Unknown subcommand.");
		}

		return true;
	}

	private void createStage(final CommandSender sender, final Command command, final String label, final String[] args) {

		// /createStage <MiniGame> <StageName>

		if (args.length < 3) {
			sendUsage(sender, args, "Invalid arguments. ");
			return;
		}

		final var miniGamePlugin = miniGamesModule.getPluginLoaderModule().getPlugins().get(args[1].toLowerCase());
		if (miniGamePlugin == null) {
			sender.sendMessage(ChatColor.RED + "Unknown MiniGame: " + args[1]);
			return;
		}

		final Path stageFolder = miniGamePlugin.getStageFolder();
		if (Files.notExists(stageFolder)) {
			try {
				Files.createDirectories(stageFolder);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		final var stageName = args[2];
		if (Arrays.stream(miniGamePlugin.listStages())
				.anyMatch(stageNameVal -> stageNameVal.equalsIgnoreCase(stageName))) {
			sender.sendMessage(ChatColor.RED + "Stage already exists: " + stageName);
			return;
		}


        try {
			Files.createFile(stageFolder.resolve(stageName + ".stage"));
        } catch (final IOException e) {
			e.printStackTrace();
        }

		sender.sendMessage(ChatColor.GREEN + "Stage created: " + stageName);
    }

	private void stage(final CommandSender sender, final Command command, final String label, final String[] args) {
		new SetupPartGUI(plugin, miniGamesModule.getPluginLoaderModule().getPlugins().values().stream().findFirst().orElse(null)).show((Player) sender);
	}

	private void setupPart(final CommandSender sender, final Command command, final String label, final String[] args) {

		// /setupPart <MiniGame> <StageName> <PartName> <SchematicFile>
		// Might be able to remove stage name

		// TODO:
		//       Create a new world
		//       Load the schematic into the world
		//  	 Teleport to a new world to set up a part
		// 		 Give inventory of items to place for specified stage (Mainly based off markers)
		//       Save part to file
		//       Delete world

		if (args.length < 5) {
			sendUsage(sender, args, "Invalid arguments.");
			return;
		}

		if (!(sender instanceof final Player player)) {
			sender.sendMessage(ChatColor.RED + "Only players can setup parts.");
			return;
		}

		final var miniGamePlugin = miniGamesModule.getPluginLoaderModule().getPlugins().get(args[1].toLowerCase());
		if (miniGamePlugin == null) {
			sender.sendMessage(ChatColor.RED + "Unknown MiniGame: " + args[1]);
			return;
		}

		final var stageName = Arrays.stream(miniGamePlugin.listStages()).filter(stageNameVal -> stageNameVal.equalsIgnoreCase(args[2]))
				.findFirst()
				.orElse(null);
		if (stageName == null) {
			sender.sendMessage(ChatColor.RED + "Unknown Stage: " + args[2]);
			return;
		}

		final var partName = args[3];

		final var schematicName = args[4];

		new PartSetup(plugin, miniGamePlugin, player, stageName, partName, schematicName).start();






	}

	/**
	 * A command to join a MiniGame
	 *
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 */
	private void join(final CommandSender sender, final Command command, final String label, final String[] args) {

		if (!(sender instanceof final Player player)) {
			sender.sendMessage(ChatColor.RED + "Only players can join MiniGames.");
			return;
		}
		if (args.length < 2) {
			sendUsage(sender, args, "Please specify a MiniGame to join.");
			return;
		}

		final List<MiniGame<?>> miniGames = miniGamesModule.getMiniGames().get(args[1].toLowerCase());
		if (miniGames == null) {
			sender.sendMessage(ChatColor.RED + "Unknown MiniGame: " + args[1]);
			return;
		}

		final MiniGame<?> miniGame = miniGames.stream().filter(game -> game.getState() == MiniGame.State.WAITING_ON_PLAYERS).findFirst().orElse(null);
		if (miniGame == null) {
			sender.sendMessage(ChatColor.RED + "No available MiniGames for: " + args[1]);
			return;
		}


		miniGame.playerJoin(player);
	}


	/**
	 * A command to start a MiniGame
	 *
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 */
	/* TODO: Use join instead to automatically determine if a game should start
	private void start(
		@NotNull final CommandSender sender,
		@NotNull final Command command,
		@NotNull final String label,
		@NotNull final String[] args
	) {

		if (args.length < 2) {
			sendUsage(sender, args, "Please specify a MiniGame to start.");
			return;
		}

		final MiniGamePlugin miniGamePlugin = miniGamesModule.getPluginLoaderModule().getPlugins().get(args[1].toLowerCase());

		if (miniGamePlugin == null) {
			sender.sendMessage(ChatColor.RED + "Unknown MiniGame: " + args[1]);
			return;
		}

		miniGamePlugin.enable();
	}*/

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
		@NotNull final CommandSender sender,
		@NotNull final String[] args
	) {

		if (args.length < 2) {
			sendUsage(sender, args, "Please specify a list to view.");
			return;
		}

		switch (args[1].toLowerCase()) {
			case "plugins" -> {

				final StringBuilder builder = new StringBuilder(ChatColor.GOLD + "" + ChatColor.BOLD + "MiniGame Plugins:");

				for (final MiniGamePlugin miniGamePlugin : miniGamesModule.getPluginLoaderModule().getPlugins().values()) {
					builder.append("\n    +").append(miniGamePlugin.getConfig().name());
				}

				sender.sendMessage(builder.toString());
			}
			case "games" -> {

				final StringBuilder builder = new StringBuilder(ChatColor.GOLD + "" + ChatColor.BOLD + "MiniGames:");

				// TODO: Include minigames with no instances
				miniGamesModule.getMiniGames().forEach((s, miniGames) ->
					builder.append("\n  ").append(StringUtils.titleCase(s)).append(": ").append(miniGames.size())
				);

				sender.sendMessage(builder.toString());
			}
			default -> sendUsage(sender, args, "Unknown list: " + args[1]);
		}
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
		// Conversation with player defined by the minigame
		// Save stage to file
	}

	private void sendUsage(
		@NotNull final CommandSender sender,
		@NotNull final String[] args,
		@NotNull final String reason
	) {

		final StringBuilder builder = new StringBuilder(
			"\n" + ChatColor.RED + reason + "\n" + ChatColor.GOLD + "Usage: " + ChatColor.GRAY + "/minigame"
		);

		switch (args.length) {

			case 1:
				switch (args[0].toLowerCase()) {
					case "list":
						builder.append(" list <plugins|games>");
						break;
					case "setinstances":
						builder.append(" setInstances <amount> <plugin>");
						break;
					case "setuppart":
						builder.append(" setupPart <MiniGame> <StageName> <PartName> <SchematicFile>");
						break;
				}
				break;

			case 2:
				switch (args[0].toLowerCase()) {
					case "setinstances":
						builder.append(" setInstances ").append(args[1]).append(" <plugin>");
						break;
				}

			default:
				builder.append(" <start|list|stop|setup>");
				break;
		}

		sender.sendMessage(builder.toString());
	}

	// TODO: A gui where you left click to increment and right click to decrement
	// TODO: Disable command if in dynamic mode
	private void setInstances(
		@NotNull final CommandSender sender,
		@NotNull final Command command,
		@NotNull final String label,
		@NotNull final String[] args
	) {

		if (args.length < 3) {
			sendUsage(sender, args, "Please specify a MiniGame and amount.");
			return;
		}

		miniGamesModule.setInstances(Integer.parseInt(args[1]), miniGamesModule.getPluginLoaderModule().getPlugins().get(args[2].toLowerCase()));
	}

//	/**
//	 * A command to see a menu for all the enabled/disabled minigames
//	 *
//	 * @param sender
//	 * @param command
//	 * @param label
//	 * @param args
//	 */
//	private void minigames(
//			@NotNull final CommandSender sender,
//			@NotNull final Command command,
//			@NotNull final String label,
//			@NotNull final String[] args
//	) {
//
//	}
}
