package dev.twelveoclock.minigameengine.setup;

import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class PartSetup {

    private final JavaPlugin plugin;

    private final MiniGamePlugin miniGamePlugin;

    private final Player player;

    private final String stageName, partName;

    private boolean started = false;

    public PartSetup(@NotNull final JavaPlugin plugin, @NotNull final MiniGamePlugin miniGamePlugin, @NotNull final Player player, @NotNull final String stageName, @NotNull final String partName) {
        this.plugin = plugin;
        this.player = player;
        this.miniGamePlugin = miniGamePlugin;
        this.stageName = stageName;
        this.partName = partName;
    }


    public void start() {

        if (started) {
            throw new IllegalStateException("PartSetup has already been started.");
        }

        if (plugin.getServer().getWorld(stageName + "-" + partName) != null) {
            throw new IllegalStateException("World already exists.");
        }

        started = true;

        // player.getLocation().getWorld().getWorldCreator().name(stageName + "-" + partName)

        final var world = new WorldCreator(stageName + "-" + partName)
                .generator("WorldGenTesting:void")
                .createWorld();
        if (world == null) {
            throw new IllegalStateException("World could not be created.");
        }

        player.teleport(world.getSpawnLocation());
    }

    public void stop() {

        if (!started) {
            throw new IllegalStateException("PartSetup has not been started.");
        }

        started = false;
    }


    public boolean isStarted() {
        return started;
    }


    private void giveItems() {
        this.player.getInventory().clear();
        //this.miniGamePlugin.getMarkers()
        //this.miniGamePlugin.getConfig()
    }


}
