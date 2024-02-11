package dev.twelveoclock.minigameengine.setup;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.event.platform.BlockInteractEvent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.twelveoclock.minigameengine.generator.VoidGenerator;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public final class PartSetup implements Listener {

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
        try (final var stream = new FileInputStream(schematicFile.toFile())) {
            clipboard = format.getReader(stream).read();
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
        final var pasteLocation = BlockVector3.at(50, 50, -50);

        final EditSession editSession = worldEdit.newEditSessionBuilder()
            .world(BukkitAdapter.adapt(world))
            .fastMode(true) // Enables fast mode which can improve performance for some operations
            .limitUnlimited() // Be cautious with unlimited changes, consider setting a limit
            .build();

        try (editSession) {

            final Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                .to(pasteLocation) // Paste at center of the world
                .ignoreAirBlocks(true)
                .build();

            clipboard.getDimensions().subtract(clipboard.getOrigin());
            Operations.complete(operation);
        }

        //Bukkit.broadcastMessage("Origin: " + clipboard.getOrigin().toString() + " Dimensions: " + clipboard.getDimensions().toString());
        //Bukkit.broadcastMessage("Width: " + clipboard.getWidth() + " Length: " + clipboard.getLength() + " Height: " + clipboard.getHeight());


        // Set spawn to where it was copied
        final var spawn = clipboard.getRegion().getCenter().subtract(clipboard.getOrigin().toVector3()).add(pasteLocation.toVector3()).add(0.5, clipboard.getHeight(), 0.5);

        // Spawn on top in center
        player.teleportAsync(new Location(world, spawn.getX(), spawn.getBlockY(), spawn.getZ()));

        giveItems();
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


    @EventHandler
    public void onClick(final PlayerInteractEvent event) {

        if (event.getPlayer() != player) {
            return;
        }

        final var item = event.getItem();
        if (item == null || !item.hasItemMeta()) {
            return;
        }

        switch (item.getItemMeta().getDisplayName()) {
            case "Selection Wand":
                handleSelectionWandClick(event);
                break;
            case "Marker":
                handleMarkerClick(event);
                break;
            case "Flood Fill Marker":
                handleFloodFillMarkerClick(event);
                break;
            case "Eraser":
                handleEraserClick(event);
                break;
            case "Options":
                handleOptionsClick(event);
                break;
            case "Save and Exit":
                handleSaveAndExitClick(event);
                break;
            default:
                // Handle unknown items, possibly ignoring or logging
                break;
        }
    }


    private void giveItems() {

        this.player.getInventory().clear();

        // WorldEdit Wand - Diamond Pickaxe
        final var wand = applyMeta(new ItemStack(Material.DIAMOND_AXE), itemMeta ->
            itemMeta.setDisplayName("Selection Wand")
        );

        // Marker - Blaze Rod (On left click set, on right click remove, on shift right change type)
        final var marker = applyMeta(new ItemStack(Material.BLAZE_ROD), itemMeta ->
            itemMeta.setDisplayName("Marker")
        );

        // Flood fill marker - Bucket (On right click set, on left click remove, on shift right change type)
        final var floodFillMarker = applyMeta(new ItemStack(Material.BUCKET), itemMeta ->
            itemMeta.setDisplayName("Flood Fill Marker")
        );

        // Eraser - Sponge (Use with right click to remove markers, flood fill, selections)
        final var eraser = applyMeta(new ItemStack(Material.SPONGE), itemMeta ->
            itemMeta.setDisplayName("Eraser")
        );

        // Options - Compass (On right click)
        final var options = applyMeta(new ItemStack(Material.COMPASS), itemMeta ->
            itemMeta.setDisplayName("Options")
        );

        // Save and exit - Bed
        final var saveAndExit = applyMeta(new ItemStack(Material.RED_BED), itemMeta ->
            itemMeta.setDisplayName("Save and Exit")
        );

        final var inventory = this.player.getInventory();

        inventory.setItem(0, wand);
        inventory.setItem(1, marker);
        inventory.setItem(2, floodFillMarker);
        inventory.setItem(3, eraser);
        inventory.setItem(7, options);
        inventory.setItem(8, saveAndExit);
    }

    private ItemStack applyMeta(ItemStack item, Consumer<ItemMeta> itemMetaConsumer) {
        var meta = item.getItemMeta();
        itemMetaConsumer.accept(meta);
        item.setItemMeta(meta);
        return item;
    }

    private void handleSelectionWandClick(PlayerInteractEvent event) {
        // Placeholder functionality: send a message to the player
        player.sendMessage(ChatColor.GREEN + "Selection Wand used.");
        // Here you would add the logic for handling selections, e.g., choosing two corners of a cubic region.
    }

    private void handleMarkerClick(PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        final Action action = event.getAction();

        // Assuming a RIGHT_CLICK_AIR or RIGHT_CLICK_BLOCK is for placing a marker
	    // Assuming a LEFT_CLICK_AIR or LEFT_CLICK_BLOCK is for removing a marker
	    if (action.equals(Action.RIGHT_CLICK_AIR)) {
		    placeMarker(player);
	    } else if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
		    placeMarker(player);
	    } else {
		    if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
			    removeMarker(player);
		    }
	    }
    }

    private void placeMarker(Player player) {
        Block targetBlock = player.getTargetBlockExact(5); // Get the block the player is looking at, within 5 blocks range
        if (targetBlock != null && targetBlock.getType().isAir()) {
            targetBlock.setType(Material.GOLD_BLOCK); // Set the block to gold, marking it
            player.sendMessage(ChatColor.GREEN + "Marker placed.");
        }
    }

    private void removeMarker(Player player) {
        final Block targetBlock = player.getTargetBlockExact(5); // Same 5 blocks range for targeting
        if (targetBlock != null && targetBlock.getType() == Material.GOLD_BLOCK) {
            targetBlock.setType(Material.AIR); // Remove the marker by setting it to air
            player.sendMessage(ChatColor.RED + "Marker removed.");
        }
    }
    private void handleFloodFillMarkerClick(PlayerInteractEvent event) {
        // Placeholder functionality: send a message to the player
        player.sendMessage(ChatColor.GREEN + "Flood Fill Marker used.");
        // The flood fill could be for changing all connected similar blocks starting from a point to a new type.
        // Implementation could utilize a queue or stack for Breadth First Search (BFS) or Depth First Search (DFS) approaches.
    }

    private void handleEraserClick(PlayerInteractEvent event) {
        // Placeholder functionality: send a message to the player
        player.sendMessage(ChatColor.GREEN + "Eraser used.");
        // This function could reset areas, remove markers, or revert selections based on where the player is pointing.
    }

    private void handleOptionsClick(PlayerInteractEvent event) {
        // Placeholder functionality: send a message to the player
        player.sendMessage(ChatColor.GREEN + "Options accessed.");
        // You might want to open a custom GUI here that lets players adjust settings or make selections.
    }

    private void handleSaveAndExitClick(PlayerInteractEvent event) {
        // Placeholder functionality: send a message to the player
        player.sendMessage(ChatColor.GREEN + "Saving your work and exiting.");
        // Logic to save changes made during the edit session and possibly unload the world or teleport the player out.
    }


}
