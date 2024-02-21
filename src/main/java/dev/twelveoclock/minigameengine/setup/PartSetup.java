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
import dev.twelveoclock.minigameengine.minigame.marker.Marker;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import dev.twelveoclock.minigameengine.position.BlockPosition;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

// TODO: Have one item for selection which swaps between modes (single, cuboid, floodfill)

public final class PartSetup implements Listener {

    private final JavaPlugin plugin;

    private final MiniGamePlugin miniGamePlugin;

    private final Player player;

    private final String stageName, partName, schematicName;

    private boolean started = false;
    
    private final Map<Marker, List<BlockPosition>> markers = new HashMap<>();

    private final SelectionWand selectionWand = new SelectionWand();

    private final Eraser eraser = new Eraser();

    private SelectionBlinker blinker;

    private Marker marker;

    private Material markerMaterial;


    // Material to represent markers
    private static final List<Material> markerMaterials = new ArrayList<>(){{
        add(Material.REDSTONE_BLOCK);
        add(Material.LAPIS_BLOCK);
        add(Material.EMERALD_BLOCK);
        add(Material.DIAMOND_BLOCK);
        add(Material.GOLD_BLOCK);
        add(Material.IRON_BLOCK);
        add(Material.COAL_BLOCK);
        add(Material.QUARTZ_BLOCK);
        add(Material.REDSTONE_LAMP);
        add(Material.BLACK_CONCRETE);
        add(Material.BLUE_CONCRETE);
        add(Material.BROWN_CONCRETE);
        add(Material.CYAN_CONCRETE);
        add(Material.GRAY_CONCRETE);
        add(Material.GREEN_CONCRETE);
        add(Material.LIGHT_BLUE_CONCRETE);
        add(Material.LIGHT_GRAY_CONCRETE);
        add(Material.LIME_CONCRETE);
        add(Material.MAGENTA_CONCRETE);
        add(Material.ORANGE_CONCRETE);
        add(Material.PINK_CONCRETE);
        add(Material.PURPLE_CONCRETE);
        add(Material.RED_CONCRETE);
        add(Material.WHITE_CONCRETE);
        add(Material.YELLOW_CONCRETE);
    }};


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

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // player.getLocation().getWorld().getWorldCreator().name(stageName + "-" + partName)

        final var schematicFile = plugin.getDataFolder().toPath().resolve("Schematics").resolve(schematicName + ".schem");
        final ClipboardFormat format = ClipboardFormats.findByFile(schematicFile.toFile()); // TODO: Load part files

        final Clipboard clipboard;
        try (final var stream = new FileInputStream(schematicFile.toFile())) {
            clipboard = format.getReader(stream).read();
        } catch (final IOException e) {
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
        HandlerList.unregisterAll(this);

        // Delete world
        plugin.getServer().unloadWorld(stageName + "-" + partName, true);
    }


    public boolean isStarted() {
        return started;
    }


    @EventHandler(ignoreCancelled = true)
    public void onClick(final PlayerInteractEvent event) {

        if (!event.getPlayer().equals(player)) {
            return;
        }

        final var item = event.getItem();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        switch (item.getItemMeta().getDisplayName()) {
            case "Selection Wand":
                handleSelectionWandClick(event);
                event.setCancelled(true);
                break;
            case "Marker":
                // TODO: Open gui to select a marker
                handleMarkerClick(event);
                event.setCancelled(true);
                break;
            case "Flood Fill Marker":
                handleFloodFillMarkerClick(event);
                event.setCancelled(true);
                break;
            case "Eraser":
                Bukkit.broadcastMessage("Eraser");
                handleEraserClick(event);
                event.setCancelled(true);
                // Shift + right click to change type (flood fill or singular marker, maybe set radius)
                break;
            case "Options":
                handleOptionsClick(event);
                event.setCancelled(true);
                break;
            case "Save and Exit":
                handleSaveAndExitClick(event);
                event.setCancelled(true);
                break;
            default:
                // Handle unknown items, possibly ignoring or logging
                break;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwapHandItem(final PlayerSwapHandItemsEvent event) {

        if (!event.getPlayer().equals(player)) {
            return;
        }

        final var mainHandItem = event.getMainHandItem();
        if (!mainHandItem.hasItemMeta() || !mainHandItem.getItemMeta().hasDisplayName()) {
            return;
        }


        event.setCancelled(true);


        // TODO: Save blinker positions
        // blinker.positions




        // TODO: Save selection


    }

    @EventHandler(ignoreCancelled = true)
    public void onSwitchItem(final PlayerItemHeldEvent event) {

        if (!event.getPlayer().equals(player)) {
            return;
        }

        final var slotItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (slotItem == null || !slotItem.hasItemMeta() || !slotItem.getItemMeta().hasDisplayName()) {
            return;
        }

        if (slotItem.getItemMeta().getDisplayName().equals("Selection Wand")) {
            event.getPlayer().sendMessage("This is the selection wand.");
            event.getPlayer().sendActionBar(Component.text("This is the selection wand."));
            //event.setCancelled(true);
        }
        // else hide selection
    }


    private void giveItems() {

        this.player.getInventory().clear();


        // Changes based on type of marker
        final var marker = applyMeta(new ItemStack(Material.STONE), itemMeta ->
            itemMeta.setDisplayName("Marker")
        );

        // WorldEdit Wand - Diamond Pickaxe
        final var wand = applyMeta(new ItemStack(Material.DIAMOND_AXE), itemMeta ->
            itemMeta.setDisplayName("Selection Wand")
        );

        // Flood fill marker - Bucket (On right click set, on left click remove, on shift right change type)
        final var floodFillMarker = applyMeta(new ItemStack(Material.BUCKET), itemMeta ->
            itemMeta.setDisplayName("Flood Fill Marker")
        );

        // TODO: Undo/Redo - Clock (On right click undo, on left click redo)
        // TODO: Confirm - Emerald (On right click confirm, on left click cancel)

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

        inventory.setItem(0, marker);
        inventory.setItem(1, wand);
        inventory.setItem(2, floodFillMarker);
        inventory.setItem(3, eraser);
        inventory.setItem(7, options);
        inventory.setItem(8, saveAndExit);
    }

    private ItemStack applyMeta(final ItemStack item, final Consumer<ItemMeta> itemMetaConsumer) {
        final var meta = item.getItemMeta();
        itemMetaConsumer.accept(meta);
        item.setItemMeta(meta);
        return item;
    }

    private void handleSelectionWandClick(final PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        final Action action = event.getAction();

        final var clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        final var selection = selectionWand.getSelection();

        if (action.isLeftClick()) {
            selection.setPos1(new BlockPosition(clickedBlock));
        } else {
            selection.setPos2(new BlockPosition(clickedBlock));
        }

        if (selection.pos1 != null && selection.pos2 != null) {

            final var min = new BlockPosition(
                Math.min(selection.pos1.getX(), selection.pos2.getX()),
                Math.min(selection.pos1.getY(), selection.pos2.getY()),
                Math.min(selection.pos1.getZ(), selection.pos2.getZ())
            );
            final var max = new BlockPosition(
                Math.max(selection.pos1.getX(), selection.pos2.getX()),
                Math.max(selection.pos1.getY(), selection.pos2.getY()),
                Math.max(selection.pos1.getZ(), selection.pos2.getZ())
            );

            final List<Long> positions = new ArrayList<>();

            for (int x = min.getX(); x <= max.getX(); x++) {
                for (int y = min.getY(); y <= max.getY(); y++) {
                    for (int z = min.getZ(); z <= max.getZ(); z++) {
                        positions.add(BlockPosition.getAsBitMask(x, y, z));
                    }
                }
            }

            final var randomMaterial = markerMaterials.get(ThreadLocalRandom.current().nextInt(markerMaterials.size()));

            if (blinker != null) {
                blinker.stop();
            }

            blinker = new SelectionBlinker(player, positions.toArray(new Long[0]), randomMaterial);
            blinker.start(20L * 5L);
        }


        // Placeholder functionality: send a message to the player
        player.sendMessage(ChatColor.GREEN + "Selection Wand used.");
        // Here you would add the logic for handling selections, e.g., choosing two corners of a cubic region.
    }

    private void handleMarkerClick(final PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        final Action action = event.getAction();

        // Assuming a RIGHT_CLICK_AIR or RIGHT_CLICK_BLOCK is for placing a marker
	    // Assuming a LEFT_CLICK_AIR or LEFT_CLICK_BLOCK is for removing a marker
	    if (action.equals(Action.RIGHT_CLICK_BLOCK)) {

		    //placeMarker(player);
	    } else if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            //removeMarker(player);
	    }
    }

    private Long[] floodFill(final Block start) {

        final Set<Long> visitedPositions = new HashSet<>();

        final Material targetMaterial = start.getType(); // The type of block we're filling

        final Queue<Block> blocksQueue = new LinkedList<>();
        blocksQueue.add(start);

        while (!blocksQueue.isEmpty()) {
            final Block block = blocksQueue.poll();

            final long position = BlockPosition.getAsBitMask(block.getX(), block.getY(), block.getZ());
            if (visitedPositions.contains(position)) {
                continue;
            }

            visitedPositions.add(position);

            // Add neighboring blocks to queue
            for (final BlockFace face : BlockFace.values()) {
                final Block relative = block.getRelative(face);
                if (relative.getType() == targetMaterial) {
                    blocksQueue.add(relative);
                }
            }
        }

        return visitedPositions.toArray(new Long[0]);
    }

    private void handleFloodFillMarkerClick(final PlayerInteractEvent event) {

        final var floodFilled = floodFill(event.getClickedBlock());

        player.sendMessage(ChatColor.GREEN + "Flood fill completed.");

        if (blinker != null) {
            blinker.stop();
        }

        blinker = new SelectionBlinker(player, floodFilled, Material.LIME_CONCRETE);
        blinker.start(20L * 5L);
    }

    private void handleEraserClick(final PlayerInteractEvent event) {

        if (blinker != null) {
            blinker.stop();
        }

        final var clickedBlock = event.getClickedBlock();
        player.sendMessage(ChatColor.GREEN + "Eraser used." + (clickedBlock != null ? " Block: " + clickedBlock.getType() : ""));

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            // Remove marker
            blinker = new SelectionBlinker(player, new Long[]{BlockPosition.getAsBitMask(clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ())}, Material.RED_CONCRETE);
        } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            // Flood fill remove
            blinker = new SelectionBlinker(player, floodFill(clickedBlock), Material.RED_CONCRETE);
        }

        blinker.start(20L * 5L);

        // This function could reset areas, remove markers, or revert selections based on where the player is pointing.
    }

    private void handleOptionsClick(final PlayerInteractEvent event) {
        // Placeholder functionality: send a message to the player
        player.sendMessage(ChatColor.GREEN + "Options accessed.");
        // You might want to open a custom GUI here that lets players adjust settings or make selections.
    }

    private void handleSaveAndExitClick(final PlayerInteractEvent event) {
        // Placeholder functionality: send a message to the player
        player.sendMessage(ChatColor.GREEN + "Saving your work and exiting.");
        // Copy schematic
        // Save markers
        // Logic to save changes made during the edit session and possibly unload the world or teleport the player out.
    }



    interface ModeSwappable {
        void nextMode();
    }

    static class SelectionWand implements ModeSwappable {

        private final CubeSelection selection = new CubeSelection(null, null);

        private Mode mode = Mode.CUBOID;

        @Override
        public void nextMode() {
            mode = Mode.values()[(mode.ordinal() + 1) % Mode.values().length];
        }

        public void setMode(final Mode mode) {
            this.mode = mode;
        }

        public CubeSelection getSelection() {
            return selection;
        }

        enum Mode {
            CUBOID,
        }

    }

    static class Eraser implements ModeSwappable {

        private Mode mode = Mode.SINGLE;

        @Override
        public void nextMode() {
            mode = Mode.values()[(mode.ordinal() + 1) % Mode.values().length];
        }

        public void setMode(final Mode mode) {
            this.mode = mode;
        }

        enum Mode {
            SINGLE,
            FLOOD_FILL,
        }

    }


    static class CubeSelection {

        private BlockPosition pos1, pos2;


        public CubeSelection(final BlockPosition pos1, final BlockPosition pos2) {
            this.pos1 = pos1;
            this.pos2 = pos2;
        }


        public void setPos1(final BlockPosition pos1) {
            this.pos1 = pos1;
        }

        public void setPos2(final BlockPosition pos2) {
            this.pos2 = pos2;
        }

        public BlockPosition getPos1() {
            return pos1;
        }

        public BlockPosition getPos2() {
            return pos2;
        }
    }

    // TODO: Take in array for positions
    class SelectionBlinker {

        private final Player player;

        private final BlockData markerMaterial;

        private final AtomicBoolean showBarrier = new AtomicBoolean();

        private final Long[] positions;

        private BukkitTask task, stopTask;

        private boolean isRunning;

        public SelectionBlinker(final Player player, final Long[] positions, final Material markerMaterial) {
            this.player = player;
            this.positions = positions;
            this.markerMaterial = markerMaterial.createBlockData();
        }

        public void start(final Long duration) {

            if (isRunning) {
                throw new IllegalStateException("Blinker is already running.");
            }

            isRunning = true;

            this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                for (final var position : positions) {
                    final var blockLocation = new Location(player.getWorld(), BlockPosition.getX(position), BlockPosition.getY(position), BlockPosition.getZ(position));
                    final var block = blockLocation.getBlock();
                    if (showBarrier.get()) {
                        player.sendBlockChange(blockLocation, block.getBlockData());
                    } else {
                        player.sendBlockChange(blockLocation, markerMaterial);
                    }
                }

                showBarrier.set(!showBarrier.get());
            }, 0, 20);

            stopTask = Bukkit.getScheduler().runTaskLater(plugin, this::stop, duration);
        }

        public void stop() {

            if (task != null) {
                task.cancel();
            }

            if (stopTask != null) {
                stopTask.cancel();
            }

            if (isRunning) {
                isRunning = false;
                for (final var position : positions) {
                    final var blockLocation = new Location(player.getWorld(), BlockPosition.getX(position), BlockPosition.getY(position), BlockPosition.getZ(position));
                    final var block = blockLocation.getBlock();
                    player.sendBlockChange(blockLocation, block.getBlockData());
                }
            }
        }

        public Long[] getPositions() {
            return positions;
        }

    }

}
