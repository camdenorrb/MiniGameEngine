package dev.twelveoclock.minigameengine.module;

import dev.twelveoclock.minigameengine.minigame.MiniGame;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MiniGamePluginLoaderModule extends PluginModule {

	// GameName -> GameClass
	private final Map<String, Class<MiniGame>> gameTypes = new HashMap<>();


	public MiniGamePluginLoaderModule(final JavaPlugin plugin) {
		super(plugin);
	}


	@Override
	protected void onEnable() {

		try {
			loadMiniGames();
		}
		catch (final IOException e) {
			throw new RuntimeException("Failed to load MiniGames", e);
		}

		enableMiniGames();
	}

	@Override
	protected void onDisable() {
		unloadMiniGames();
	}


	private void loadMiniGames() throws IOException {

		final Path mainFolder = plugin.getDataFolder().toPath();
		Files.createDirectories(mainFolder);

		final Path updateFolder = mainFolder.resolve("Updates");
		Files.createDirectories(updateFolder);

		final Path gamesFolder = mainFolder.resolve("Games");
		Files.createDirectories(gamesFolder);

		// Move all jars from the games folder to the update folder
		try (final var files = Files.list(updateFolder)) {
			files.filter(Files::isRegularFile)
				.filter(it -> it.toString().toLowerCase().endsWith(".jar"))
				.forEach(path -> {
					try {
						Files.move(path, gamesFolder.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
					}
					catch (final IOException e) {
						throw new RuntimeException(e);
					}
				});
		}

		// Load all jars from games folder using URLClassLoader
		try (final var files = Files.list(gamesFolder)) {
			files.filter(Files::isRegularFile)
				.filter(it -> it.toString().toLowerCase().endsWith(".jar"))
				.forEach(path -> {
					try(final URLClassLoader gameClassLoader = new URLClassLoader(new URL[]{path.toUri().toURL()}, plugin.getClass().getClassLoader())) {

						final List<String> gameConfigLines = new String(
							gameClassLoader.getResourceAsStream("game.txt").readAllBytes()
						).lines().toList();

						if (gameConfigLines.size() < 2) {
							throw new RuntimeException("Invalid game.txt file, there is less than 2 lines: " + path);
						}

						final String gameName = gameConfigLines.get(0);
						final String gameClassPath = gameConfigLines.get(1);

						@SuppressWarnings("unchecked")
						final Class<MiniGame> gamePlugin = (Class<MiniGame>) gameClassLoader.loadClass(gameClassPath);

						gameTypes.put(gameName, gamePlugin);
					}
					catch (final IOException | ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				});
		}
	}

	private void enableMiniGames() {

	}

	private void disableMiniGames() {

	}

	private void unloadMiniGames() {

	}


	public Map<String, Class<MiniGame>> getGameTypes() {
		return gameTypes;
	}

}
