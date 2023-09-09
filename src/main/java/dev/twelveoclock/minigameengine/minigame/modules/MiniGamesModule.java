package dev.twelveoclock.minigameengine.minigame.modules;

import dev.twelveoclock.minigameengine.config.MiniGamePluginConfig;
import dev.twelveoclock.minigameengine.config.PluginConfig;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import dev.twelveoclock.minigameengine.module.PluginModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * A module that handles loading/running/unloading of MiniGames
 */
public final class MiniGamesModule extends PluginModule {

    private final PluginConfig config;

    private final List<MiniGamePlugin> miniGamePlugins = new ArrayList<>();


    public MiniGamesModule(final JavaPlugin plugin, final PluginConfig config) {
        super(plugin);
        this.config = config;
    }


    @Override
    protected void onEnable() {
        loadAllMiniGames();
        // TODO: Start the MiniGame specified by the config
    }

    @Override
    protected void onDisable() {
        miniGamePlugins.forEach(MiniGamePlugin::onDisable);
        // TODO: Disable all MiniGames
        // TODO: Unload all MiniGames
    }


    @EventHandler
    private void onJoin(final PlayerJoinEvent event) {
        // TODO: Make the player join the MiniGame specified by the config
    }


    public PluginConfig getConfig() {
        return config;
    }

    public List<MiniGamePlugin> getMiniGamePlugins() {
        return miniGamePlugins;
    }


    private void loadAllMiniGames() {

    }

}
