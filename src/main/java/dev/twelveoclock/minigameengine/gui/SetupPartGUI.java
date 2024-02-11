package dev.twelveoclock.minigameengine.gui;

import dev.twelveoclock.minigameengine.gui.view.SlicedChest;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import dev.twelveoclock.minigameengine.module.PluginModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class SetupPartGUI extends PluginModule implements GUI {

    private final SlicedChest view;

    private final MiniGamePlugin miniGamePlugin;


    public SetupPartGUI(final JavaPlugin plugin, final MiniGamePlugin miniGamePlugin) {
        super(plugin);
        this.view = new SlicedChest(plugin.getServer().createInventory(null, 36, "Setup Part"));
        this.miniGamePlugin = miniGamePlugin;
    }


    @Override
    public void show(final Player player) {

        if (!this.isEnabled()) {
            this.enable();
        }

        build();
        view.show(player);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (view.isThisInventory(event.getClickedInventory()) || (event.isShiftClick() && view.isThisInventory(event.getView().getTopInventory()))) {
            event.getWhoClicked().sendMessage("You clicked on the setup part GUI");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        if (view.isThisInventory(event.getView().getTopInventory())) {
            if (event.getRawSlots().stream().anyMatch(slot ->
                slot < event.getView().getTopInventory().getSize()
            )) {
                event.getWhoClicked().sendMessage("You dragged on the setup part GUI");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryMoveItem(final InventoryMoveItemEvent event) {
        if (view.isThisInventory(event.getDestination()) || view.isThisInventory(event.getSource())) {
            event.setCancelled(true);
        }
    }


    private void build() {

        final var gameTypesView = new SlicedChest(view.getInventory(), 0, 0, 9, 4);

        gameTypesView.fillWith(miniGamePlugin.getMarkers(), marker -> {

            final var gameItem = new ItemStack(Material.ARMOR_STAND);
            final var meta = gameItem.getItemMeta();

            meta.setDisplayName(marker.toString());
            gameItem.setItemMeta(meta);

            return gameItem;
        });
    }

}
