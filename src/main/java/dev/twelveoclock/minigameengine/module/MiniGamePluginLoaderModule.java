package dev.twelveoclock.minigameengine.module;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import dev.twelveoclock.minigameengine.MiniGameEnginePlugin;
import dev.twelveoclock.minigameengine.config.MiniGamePluginConfig;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

	private final Field configField, engineField, dataFolderField, javaPluginField;

	// GameName (LowerCase) -> GameClass
	private final Map<String, MiniGamePlugin> gamePlugins = new HashMap<>();


	public MiniGamePluginLoaderModule(final MiniGameEnginePlugin plugin) {

		super(plugin);

		try {

			configField = MiniGamePlugin.class.getDeclaredField("config");
			engineField = MiniGamePlugin.class.getDeclaredField("engine");
			dataFolderField = MiniGamePlugin.class.getDeclaredField("dataFolder");
			javaPluginField = MiniGamePlugin.class.getDeclaredField("javaPlugin");

			configField.setAccessible(true);
			engineField.setAccessible(true);
			dataFolderField.setAccessible(true);
			javaPluginField.setAccessible(true);
		}
		catch (final NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	protected void onEnable() {
		try {

			final Path mainFolder = plugin.getDataFolder().toPath();
			final Path updateFolder = mainFolder.resolve(UPDATES_FOLDER_NAME);
			final Path gamesFolder = mainFolder.resolve(GAMES_FOLDER_NAME);

			Files.createDirectories(mainFolder);
			Files.createDirectories(updateFolder);
			Files.createDirectories(gamesFolder);

			updatePlugins(gamesFolder, updateFolder);
			loadPlugins(gamesFolder);
			gamePlugins.values().forEach(MiniGamePlugin::enable); // TODO: Filter based on GUI data
		}
		catch (final IOException e) {
			throw new RuntimeException("Failed to load MiniGames", e);
		}
	}

	@Override
	protected void onDisable() {
		unloadLoaders();
	}


	private void loadPlugins(final Path gamesFolder) throws IOException {
		// Load all jars from games folder using URLClassLoader
		try (final var files = Files.list(gamesFolder)) {
			files.filter(Files::isRegularFile)
				.filter(it -> it.toString().toLowerCase().endsWith(".jar"))
				.forEach(path -> {
					try (final URLClassLoader gameClassLoader = new URLClassLoader(new URL[]{path.toUri().toURL()}, plugin.getClass().getClassLoader())) {
						loadPlugin(gameClassLoader, gamesFolder);
					}
					catch (final RuntimeException | IOException e) {
						throw new RuntimeException(e);
					}
				});
		}
	}

	private void updatePlugins(final Path gamesFolder, final Path updateFolder) throws IOException {
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
	}

	private void loadPlugin(final URLClassLoader gameClassLoader, final Path gamesFolder) {
		try {

			final MiniGamePluginConfig miniGameConfig = loadGameConfig(gameClassLoader);

			@SuppressWarnings("unchecked")
			final Class<MiniGamePlugin> gamePluginClass = (Class<MiniGamePlugin>) gameClassLoader.loadClass(miniGameConfig.mainClassPath());

			if (gamePlugins.containsKey(miniGameConfig.name().toLowerCase())) {
				throw new RuntimeException("Duplicate game name: " + miniGameConfig.name());
			}

			final MiniGamePlugin gamePlugin = gamePluginClass.getConstructor().newInstance();
			final Path gameFolder = gamesFolder.resolve(miniGameConfig.name());

			Files.createDirectories(gameFolder);

			configField.set(gamePlugin, miniGameConfig);
			engineField.set(gamePlugin, plugin);
			dataFolderField.set(gamePlugin, gameFolder);
			javaPluginField.set(gamePlugin, plugin);

			gamePlugins.put(miniGameConfig.name().toLowerCase(), gamePlugin);

			gamePlugin.load();
		}
		catch (final IOException | ClassNotFoundException | InvocationTargetException |
		             InstantiationException | IllegalAccessException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private void loadStages() {

		// TODO: Make stage data relative per minigame

	}

	private void unloadLoaders() {
		gamePlugins.values().forEach(MiniGamePlugin::disable);
		gamePlugins.clear();
	}

	private MiniGamePluginConfig loadGameConfig(final URLClassLoader gameClassLoader) throws IOException {
		try (final InputStream inputStream = gameClassLoader.getResourceAsStream(CONFIG_FILE_NAME)) {
			return new TomlMapper().readValue(inputStream, MiniGamePluginConfig.class);
		}
	}


	public Map<String, MiniGamePlugin> getPlugins() {
		return gamePlugins;
	}

}
