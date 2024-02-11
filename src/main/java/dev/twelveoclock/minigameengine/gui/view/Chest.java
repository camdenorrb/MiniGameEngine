package dev.twelveoclock.minigameengine.gui.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public final class Chest implements View {

	// This will set the item display name to debugging related information
	private static final boolean IS_DEBUGGING = true;

	private final Inventory inventory;


	public Chest(final String title) {
		this(title, 54);
	}

	public Chest(final String title, final int size) {
		this(Bukkit.createInventory(null, size, title));
	}

	public Chest(final @NotNull Inventory inventory) {

		this.inventory = inventory;

		final var type = inventory.getType();
		if (type != InventoryType.CHEST && type != InventoryType.ENDER_CHEST) {
			throw new IllegalArgumentException("Inventory must be a chest");
		}
	}


	@Override
	public void show(final @NotNull Player player) {
		player.openInventory(inventory);
	}


	public void setItem(final int index, final ItemStack item) {
		inventory.setItem(index, item);
	}

	public void setItem(final int row, final int col, final ItemStack item) {
		inventory.setItem(slotFor(row, col), item);
	}

	public void fill(final ItemStack item) {

		final int size = inventory.getSize();

		for (int slot = 0; slot < size; slot++) {
			inventory.setItem(slot, item);
		}
	}

	public void fill(final Function<Integer, ItemStack> filler) {

		final int size = inventory.getSize();

		for (int slot = 0; slot < size; slot++) {
			inventory.setItem(slot, filler.apply(slot));
		}
	}

	public <T> void fillWith(final Iterable<T> values, final Function<T, ItemStack> filler) {

		final Iterator<T> iterator = values.iterator();
		final int size = inventory.getSize();

		for (int slot = 0; slot < size; slot++) {

			if (!iterator.hasNext()) {
				break;
			}

			inventory.setItem(slot, filler.apply(iterator.next()));
		}
	}

	public SlicedChest asSlicedChest() {
		return new SlicedChest(inventory);
	}

	public SlicedChest slice(final int startX, final int startY, final int width, final int height) {
		return new SlicedChest(
			inventory,
			startX,
			startY,
			startX + width,
			startY + height
		);
	}

	public int slotFor(final int row, final int col) {
		return row * 9 + col;
	}

	public boolean isThisInventory(final Inventory inventory) {
		return this.inventory.equals(inventory);
	}

}