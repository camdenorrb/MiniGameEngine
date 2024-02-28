package dev.twelveoclock.minigameengine.gui.view;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public final class SlicedChest implements View {

	private final Inventory inventory;

	private final int startX, startY, endX, endY;

	private final int size, colSize, rowSize;

	private final Map<String, SlicedChest> sections = new HashMap<>();


	public SlicedChest(final Inventory inventory) {
		this(inventory, 0, 0, 9, inventory.getSize() / 9);
	}

	public SlicedChest(final Inventory inventory, final int startX, final int startY, final int endX, final int endY) {

		if (startX < 0 || startX > 9) {
			throw new IllegalArgumentException("startX must be between 0 and 8");
		}
		if (startY < 0 || startY > inventory.getSize() / 9) {
			throw new IllegalArgumentException("startY must be between 0 and " + (inventory.getSize() / 9));
		}
		if (endX < 0 || endX > 9) {
			throw new IllegalArgumentException("endX must be between 0 and 8");
		}
		if (endY < 0 || endY > inventory.getSize() / 9) {
			throw new IllegalArgumentException("endY must be between 0 and " + (inventory.getSize() / 9));
		}

		this.inventory = inventory;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.colSize = endX - startX;
		this.rowSize = endY - startY;
		this.size = colSize * rowSize;
	}


	@Override
	public void show(final Player player) {
		player.openInventory(inventory);
	}


	public void setItem(final int x, final int y, final ItemStack item) {
		inventory.setItem(originalSlotFromXY(x, y), item);
	}

	public void fill(final ItemStack item) {
		for (int slot = 0; slot < this.size; slot++) {
			inventory.setItem(originalSlot(slot), item);
		}
	}

	public void fill(final Function<Integer, ItemStack> filler) {
		for (int slot = 0; slot < this.size; slot++) {
			final int originSlot = originalSlot(slot);
			inventory.setItem(originSlot, filler.apply(originSlot));
		}
	}

	// Returns value -> slot
	public <T> Map<Integer, T> fillWith(final Iterable<T> values, final Function<T, ItemStack> filler) {

		final Map<Integer, T> slotToValue = new HashMap<>();
		final Iterator<T> iterator = values.iterator();

		for (int slot = 0; slot < this.size; slot++) {

			if (!iterator.hasNext()) {
				break;
			}

			final T value = iterator.next();
			final int originSlot = originalSlot(slot);

			inventory.setItem(originSlot, filler.apply(value));
			slotToValue.put(originSlot, value);
		}

		return slotToValue;
	}

	public SlicedChest slice(final String name, final int startX, final int startY, final int width, final int height) {

		final var sliced = new SlicedChest(
			inventory,
			this.startX + startX,
			this.startY + startY,
			this.startX + startX + width,
			this.startY + startY + height
		);

		sections.put(name, sliced);

		return sliced;
	}

	public int originalSlot(final int slot) {
		return startX + slot + ((9 - colSize) * (slot / colSize));
	}

	public int originalSlotFromXY(final int x, final int y) {
		return originalSlot(slotFromXY(x, y));
	}

	public int slotFromXY(final int x, final int y) {

		if (x < 0 || x > colSize) {
			throw new IllegalArgumentException("x must be between 0 and " + colSize);
		}

		if (y < 0 || y > rowSize) {
			throw new IllegalArgumentException("y must be between 0 and " + rowSize);
		}

		return (startX + x + ((startY + y) * 9)) - 1;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	public Map<String, SlicedChest> getSections() {
		return sections;
	}

	public boolean isThisInventory(final Inventory inventory) {
		return this.inventory.equals(inventory);
	}

}
