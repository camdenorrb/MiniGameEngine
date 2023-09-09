package dev.twelveoclock.minigameengine.minigame.team.npc;

import dev.twelveoclock.minigameengine.minigame.data.Armor;
import dev.twelveoclock.minigameengine.minigame.team.Team;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

public final class MobTeamNPC<T extends LivingEntity> implements TeamNPC {

	private final Team team;

	private final String displayName;

	private final Class<T> entityType;

	private final Consumer<T> onSpawn;

	private final Armor armor;


	public MobTeamNPC(final String displayName,
	                  final Team team,
	                  final Class<T> entityType,
	                  final Consumer<T> onSpawn) {
		this(displayName, team, entityType, onSpawn, null);
	}


	public MobTeamNPC(final String displayName,
	                  final Team team,
	                  final Class<T> entityType,
	                  final Consumer<T> onSpawn,
	                  final Armor armor) {
		this.displayName = displayName;
		this.team = team;
		this.entityType = entityType;
		this.onSpawn = onSpawn;
		this.armor = armor;
	}


	@Override
	public void spawn(final Location location) {
		location.getWorld().spawn(location, entityType, entity -> {
			entity.setAI(false);
			entity.setInvulnerable(true);
			entity.setSilent(true);
			entity.setCollidable(false);
			entity.setCustomName(displayName);
			entity.setCustomNameVisible(true);
			entity.setRemoveWhenFarAway(false);
			entity.setCanPickupItems(false);
			entity.setPersistent(true);
			entity.setGlowing(true);
			entity.setGravity(false);
			this.onSpawn.accept(entity);
		});
	}


	@Override
	public Team getTeam() {
		return team;
	}

	public Class<T> getEntityType() {
		return entityType;
	}

	public String getDisplayName() {
		return displayName;
	}

}
