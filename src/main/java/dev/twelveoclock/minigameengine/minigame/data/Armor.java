package dev.twelveoclock.minigameengine.minigame.data;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public final class Armor {

	private final ItemStack helmet, chestplate, leggings, boots;

	public Armor(final ItemStack helmet, final ItemStack chestplate, final ItemStack leggings, final ItemStack boots) {
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
	}

	public void apply(final LivingEntity entity) {
		final EntityEquipment equipment = entity.getEquipment();
		equipment.setHelmet(helmet);
		equipment.setChestplate(chestplate);
		equipment.setLeggings(leggings);
		equipment.setBoots(boots);
	}

}
