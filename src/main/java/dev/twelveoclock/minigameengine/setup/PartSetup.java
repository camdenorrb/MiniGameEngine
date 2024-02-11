package dev.twelveoclock.minigameengine.setup;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.twelveoclock.minigameengine.generator.VoidGenerator;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;

public final class PartSetup {

    private final JavaPlugin plugin;

    private final MiniGamePlugin miniGamePlugin;

    private final Player player;

    private final String stageName, partName, schematicName;

    private boolean started = false;

    public PartSetup(@NotNull final JavaPlugin plugin,
                     @NotNull final MiniGamePlugin miniGamePlugin,
                     @NotNull final Player player,
                     @NotNull final String stageName,
                     @NotNull final String partName,
                     @NotNull final String schematicName) {
        this.plugin = plugin;
        this.player = player;
        this.miniGamePlugin = miniGamePlugin;
        this.stageName = stageName;
        this.partName = partName;
        this.schematicName = schematicName;
    }


    public void start() {

        if (started) {
            throw new IllegalStateException("PartSetup has already been started.");
        }

        //if (plugin.getServer().getWorld(stageName + "-" + partName) != null) {
        //    throw new IllegalStateException("World already exists.");
        //}

        started = true;

        // player.getLocation().getWorld().getWorldCreator().name(stageName + "-" + partName)

        final var schematicFile = plugin.getDataFolder().toPath().resolve("Schematics").resolve(schematicName + ".schem");
        final ClipboardFormat format = ClipboardFormats.findByFile(schematicFile.toFile()); // TODO: Load part files

        final Clipboard clipboard;
        try {
            clipboard = format.getReader(new FileInputStream(schematicFile.toFile())).read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var world = new WorldCreator(stageName + "-" + partName)
                .generator(new VoidGenerator())
                .createWorld();
        if (world == null) {
            throw new IllegalStateException("World could not be created.");
        }

        final WorldEdit worldEdit = WorldEdit.getInstance();
        final EditSession editSession = worldEdit.newEditSession(BukkitAdapter.adapt(world));

        final var pasteLocation = BlockVector3.at(50, 50, -50);

        final Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                .to(pasteLocation) // Paste at center of the world
                .ignoreAirBlocks(true)
                .build();



        clipboard.getDimensions().subtract(clipboard.getOrigin());

        //Bukkit.broadcastMessage("Origin: " + clipboard.getOrigin().toString() + " Dimensions: " + clipboard.getDimensions().toString());


        Operations.complete(operation);
        editSession.flushQueue();

        //Bukkit.broadcastMessage("Width: " + clipboard.getWidth() + " Length: " + clipboard.getLength() + " Height: " + clipboard.getHeight());


        // Set spawn to where it was copied
        final var spawn = clipboard.getRegion().getCenter().subtract(clipboard.getOrigin().toVector3()).add(pasteLocation.toVector3()).add(0.5, clipboard.getHeight(), 0.5);

        // Spawn on top in center
        player.teleportAsync(new Location(world, spawn.getX(), spawn.getBlockY(), spawn.getZ()));
    }

    public void stop() {

        if (!started) {
            throw new IllegalStateException("PartSetup has not been started.");
        }

        started = false;

        // Delete world
        plugin.getServer().unloadWorld(stageName + "-" + partName, true);
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
