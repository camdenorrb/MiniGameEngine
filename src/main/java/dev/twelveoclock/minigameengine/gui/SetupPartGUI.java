package dev.twelveoclock.minigameengine.gui;

import dev.twelveoclock.minigameengine.gui.view.SlicedChest;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import dev.twelveoclock.minigameengine.module.PluginModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
        build();
        player.openInventory(view.getInventory());
    }


    private void build() {

        final var gameTypesView = new SlicedChest(view.getInventory(), 0, 0, 9, 4);

        gameTypesView.fillWith(miniGamePlugin.getMarkers(), marker -> {

            final var gameItem = new ItemStack(Material.CHEST);
            final var meta = gameItem.getItemMeta();
            meta.setDisplayName(marker.toString());
            gameItem.setItemMeta(meta);

            return gameItem;
        });
    }

}
