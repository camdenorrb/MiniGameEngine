package dev.twelveoclock.minigameengine;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import dev.twelveoclock.minigameengine.config.MiniGamePluginConfig;
import dev.twelveoclock.minigameengine.minigame.modules.MiniGamesModule;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MiniGameEnginePlugin extends JavaPlugin {

    private MiniGamePluginConfig pluginConfig;

    private MiniGamesModule miniGamesModule;


    /**
     * Constructor for MockBukkit
     */
    public MiniGameEnginePlugin() {
        super();
    }

    /**
     * Constructor for MockBukkit
     */
    public MiniGameEnginePlugin(final JavaPluginLoader loader, final PluginDescriptionFile description, final File dataFolder, final File file) {
        super(loader, description, dataFolder, file);
    }


    @Override
    public void onLoad() {
        loadConfig();
        miniGamesModule = new MiniGamesModule(this, pluginConfig);
    }

    @Override
    public void onEnable() {
        miniGamesModule.enable();
    }

    @Override
    public void onDisable() {
        miniGamesModule.disable();
    }


    /**
     * Loads the current config or copies the default
     */
    private void loadConfig() {

        final Path configPath = Path.of(getDataFolder().getAbsolutePath(), "config.toml");

        // Create the default config if no file exists
        if (Files.notExists(configPath)) {
            try (final InputStream inputStream = getClass().getResource("/config.toml").openStream()) {
                Files.createDirectories(configPath.getParent());
                Files.copy(inputStream, configPath);
            }
            catch (final IOException ex) {
                ex.printStackTrace();
            }
        }

        // Read config
        try (final InputStream inputStream = Files.newInputStream(configPath)) {
            pluginConfig = new TomlMapper().readValue(inputStream, MiniGamePluginConfig.class);
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

}
