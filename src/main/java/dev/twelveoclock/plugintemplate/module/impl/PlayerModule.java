package dev.twelveoclock.plugintemplate.module.impl;

import dev.twelveoclock.plugintemplate.module.PluginModule;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerModule extends PluginModule {

    public PlayerModule(final JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_PURR, 1.0F, 1.0F);
    }

}
