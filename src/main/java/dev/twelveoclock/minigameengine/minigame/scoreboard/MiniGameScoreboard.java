package dev.twelveoclock.minigameengine.minigame.scoreboard;

import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.module.PluginModule;
import dev.twelveoclock.minigameengine.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

// TODO: Account for Current MiniGame state, Teams
public class MiniGameScoreboard extends PluginModule {

	private final MiniGame miniGame;

	private final BukkitScoreboard scoreboard;

	private final BukkitScoreboard.SideBar sideBar;

	@Nullable private final Integer updateDelay;


	public MiniGameScoreboard(
		final MiniGame miniGame,
		final JavaPlugin plugin,
		@Nullable final Integer updateDelay
	) {

		super(plugin);

		this.miniGame = miniGame;
		this.updateDelay = updateDelay;
		this.scoreboard = new BukkitScoreboard(plugin);

		this.sideBar = scoreboard.createSideBar(
			"MiniGames",
			ChatUtils.colorize("&6&lMiniGames")
		);
	}


	public List<String> createScoreboardContent() {
		return List.of(
			miniGame.getName(),
			miniGame.getStage(),
		);
	}

	public Team createTeam(final String name, final String prefix, final String suffix) {


		final Team team = scoreboard.registerNewTeam(name);

		team.setPrefix(prefix);
		team.setSuffix(suffix);

		return team;
	}




	/*
	public static class Entry {

		private final String text;

		private final int delay;


		public Entry(final String text, final int delay) {
			this.text = text;
			this.delay = delay;
		}


		public int getDelay() {
			return delay;
		}

		public String getText() {
			return text;
		}

	}
	*/



}
