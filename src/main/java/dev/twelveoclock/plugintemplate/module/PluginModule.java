package dev.twelveoclock.plugintemplate.module;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginModule implements Module, Listener {

    private boolean isEnabled = false;

    protected final JavaPlugin plugin;


    public PluginModule(final JavaPlugin plugin) {
        this.plugin = plugin;
    }


    protected void onEnable() {}

    protected void onDisable() {}


    @Override
    public final void enable() {
        if (!isEnabled) {
            onEnable();
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            isEnabled = true;
        }
    }

    @Override
    public final void disable() {
        if (isEnabled) {
            onDisable();
            HandlerList.unregisterAll(this);
            isEnabled = false;
        }
    }


    @Override
    public final boolean isEnabled() {
        return isEnabled;
    }

}
