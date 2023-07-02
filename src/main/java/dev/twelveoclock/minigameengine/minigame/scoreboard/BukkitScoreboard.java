package dev.twelveoclock.minigameengine.minigame.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

public final class BukkitScoreboard {

	private final Scoreboard scoreboard;


	public BukkitScoreboard(final JavaPlugin plugin) {
		this.scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
	}


	public SideBar createSideBar(final String name, final String displayName) {
		return createSideBar(name, Criteria.DUMMY, displayName);
	}

	public SideBar createSideBar(final String name, final Criteria criteria, final String displayName) {
		return new SideBar(
			scoreboard.registerNewObjective(name, criteria, displayName),
			displayName
		);
	}


	public Team createOrUpdateTeam(final String name, final String prefix, final String suffix) {

		Team team = scoreboard.getTeam(name);

		if (team == null) {
			team = scoreboard.registerNewTeam(name);
		}

		team.setPrefix(prefix);
		team.setSuffix(suffix);

		return team;
	}


	public final class SideBar {

		private final Objective objective;

		private final String displayName;

		private final String[] lines = new String[ChatColor.values().length];


		public SideBar(final Objective objective, final String displayName) {
			this.objective = objective;
			this.displayName = displayName;
		}


		public void setLine(final int index, final String text) {

			final String teamName = ChatColor.values()[index] + "" + ChatColor.RESET;

			final String prefix = text.length() > 16 ? text.substring(0, 16) : text;
			final String suffix = text.length() > 16 ? ChatColor.getLastColors(prefix) + text.substring(16) : "";

			lines[index] = text;

			createOrUpdateTeam(teamName, prefix, suffix);
		}


		//region Getters

		public String getDisplayName() {
			return displayName;
		}

		public Objective getObjective() {
			return objective;
		}

		public String[] getLines() {
			return lines;
		}

		//endregion

	}

}
