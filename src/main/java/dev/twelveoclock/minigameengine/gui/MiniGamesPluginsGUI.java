package dev.twelveoclock.minigameengine.gui;

import dev.twelveoclock.minigameengine.gui.view.SlicedChest;
import dev.twelveoclock.minigameengine.module.MiniGamePluginLoaderModule;
import dev.twelveoclock.minigameengine.module.PluginModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiniGamesPluginsGUI extends PluginModule implements GUI {

	private final SlicedChest view;

	private final MiniGamePluginLoaderModule miniGamesPluginModule;


	public MiniGamesPluginsGUI(final JavaPlugin plugin, final MiniGamePluginLoaderModule miniGamesPluginModule) {
		super(plugin);
		this.view = new SlicedChest(plugin.getServer().createInventory(null, InventoryType.CHEST));
		this.miniGamesPluginModule = miniGamesPluginModule;
	}


	@Override
	protected void onEnable() {
		build();
	}

	@Override
	public void show(final Player player) {

	}


	private void build() {

		final var gameTypesView = new SlicedChest(view.getInventory(), 0, 0, 1, 1);

		gameTypesView.fillWith(miniGamesPluginModule.getPlugins().values(), gamePlugin -> {

			final var gameItem = new ItemStack(Material.CHEST);
			final var meta = gameItem.getItemMeta();

			meta.setDisplayName(gamePlugin.getConfig().name());
			gameItem.setItemMeta(meta);

			return gameItem;
		});
	}

}
