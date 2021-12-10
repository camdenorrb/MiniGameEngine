package dev.twelveoclock.minigameengine.minigame.scoreboard;

import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.module.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GOLD;


// TODO: Account for Current MiniGame state, Teams
public class MiniGameScoreboard extends PluginModule {

	private final MiniGame miniGame;

	private final BukkitScoreboard scoreboard;

	private final BukkitScoreboard.SideBar sideBar;

	private final long updateTickRate;


	public MiniGameScoreboard(
		final MiniGame miniGame,
		final JavaPlugin plugin,
		final Integer updateTickRate
	) {

		super(plugin);

		this.miniGame = miniGame;
		this.updateTickRate = updateTickRate;
		this.scoreboard = new BukkitScoreboard(plugin);

		this.sideBar = scoreboard.createSideBar(
			"MiniGames",
			GOLD + "" + BOLD + "MiniGames"
		);
	}


	private void updateScoreboardContent() {
		sideBar.setLine(0, miniGame.getName());
		sideBar.setLine(1, miniGame.getStage().getName());
	}


	//region Getters

	public MiniGame getMiniGame() {
		return miniGame;
	}

	public BukkitScoreboard getScoreboard() {
		return scoreboard;
	}

	public BukkitScoreboard.SideBar getSideBar() {
		return sideBar;
	}

	public long getUpdateTickRate() {
		return updateTickRate;
	}

	//endregion

}
