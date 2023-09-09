package dev.twelveoclock.minigameengine.module;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import dev.twelveoclock.minigameengine.config.MiniGamePluginConfig;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public final class MiniGamePluginLoaderModule extends PluginModule {

	private static final String GAMES_FOLDER_NAME = "Games";

	private static final String UPDATES_FOLDER_NAME = "Updates";

	private static final String CONFIG_FILE_NAME = "minigame.toml";


	// GameName -> GameClass
	private final Map<String, Class<MiniGamePlugin>> gameTypes = new HashMap<>();


	public MiniGamePluginLoaderModule(final JavaPlugin plugin) {
		super(plugin);
	}


	@Override
	protected void onEnable() {
		try {
			loadMiniGamesPlugins();
		}
		catch (final IOException e) {
			throw new RuntimeException("Failed to load MiniGames", e);
		}
	}

	@Override
	protected void onDisable() {
		unloadMiniGameLoaders();
	}


	private void loadMiniGamesPlugins() throws IOException {

		final Path mainFolder = plugin.getDataFolder().toPath();
		Files.createDirectories(mainFolder);

		final Path updateFolder = mainFolder.resolve(UPDATES_FOLDER_NAME);
		Files.createDirectories(updateFolder);

		final Path gamesFolder = mainFolder.resolve(GAMES_FOLDER_NAME);
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
					try (final URLClassLoader gameClassLoader = new URLClassLoader(new URL[]{path.toUri().toURL()}, plugin.getClass().getClassLoader())) {

						final MiniGamePluginConfig miniGameConfig = loadMiniGamePluginConfig(gameClassLoader);

						@SuppressWarnings("unchecked")
						final Class<MiniGamePlugin> gamePlugin = (Class<MiniGamePlugin>) gameClassLoader.loadClass(miniGameConfig.mainClassPath());

						if (gameTypes.containsKey(miniGameConfig.name())) {
							throw new RuntimeException("Duplicate game name: " + miniGameConfig.name());
						}

						gameTypes.put(miniGameConfig.name(), gamePlugin);
					}
					catch (final IOException | ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				});
		}
	}

	private void unloadMiniGameLoaders() {
		gameTypes.clear();
	}

	private MiniGamePluginConfig loadMiniGamePluginConfig(final URLClassLoader gameClassLoader) throws IOException {
		try (final InputStream inputStream = gameClassLoader.getResourceAsStream(CONFIG_FILE_NAME)) {
			return new TomlMapper().readValue(inputStream, MiniGamePluginConfig.class);
		}
	}


	public Map<String, Class<MiniGamePlugin>> getGameTypes() {
		return gameTypes;
	}

}
