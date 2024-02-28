package dev.twelveoclock.spleef;

import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;
import dev.twelveoclock.minigameengine.position.BlockPosition;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public final class SpleefMiniGame extends MiniGame<SpleefPlugin> implements Listener {

	private final Stage stage;

	public SpleefMiniGame(final SpleefPlugin plugin, final Stage stage) {
		super(plugin);
		this.stage = stage;
	}

	@Override
	protected boolean onStart() {
		System.out.println("Hello World! (SpleefMiniGame)");
		players.forEach(this::join);
		return false;
	}

	@Override
	protected boolean onStop() {
		System.out.println("Goodbye World! (SpleefMiniGame)");
		return true;
	}

	@Override
	protected void onPlayerJoin(final Player player) {
		join(player);
	}


	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {

		/*
		final List<BlockPosition> deathLocs = stage.getData().markers().get(SpleefMarker.DEATH);
		if (deathLocs == null || deathLocs.isEmpty()) {
			throw new IllegalStateException("No death point has been set for the Spleef stage.");
		}

		deathLocs.forEach(deathLoc -> {
			if (new BlockPosition(Objects.requireNonNull(event.getTo())).equals(deathLoc)) {
				death(event.getPlayer());
			}
		});*/
	}


	private void join(final Player player) {
		spawn(player);
		// TODO: Give items
	}

	private void spawn(final Player player) {

		/*
		final List<BlockPosition> spawn = stage.getData().markers().get(SpleefMarker.SPAWN);
		if (spawn == null || spawn.isEmpty()) {
			throw new IllegalStateException("No spawn point has been set for the Spleef stage.");
		}

		final World world = plugin.getJavaPlugin().getServer().getWorld(stage.getData().worldUUID());
		final Location spawnLocation = spawn.get(ThreadLocalRandom.current().nextInt(spawn.size())).toLocation(world);
		player.teleport(spawnLocation);

		 */
	}

	private void death(final org.bukkit.entity.Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		spawn(player);
	}

}
