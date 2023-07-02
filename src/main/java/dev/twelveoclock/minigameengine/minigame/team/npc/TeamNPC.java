package dev.twelveoclock.minigameengine.minigame.team.npc;


import dev.twelveoclock.minigameengine.minigame.team.Team;
import org.bukkit.Location;

// The NPC that represents a team in the LobbyWorld
// Make mine use mobs for now
public interface TeamNPC {

	Team getTeam();

	void spawn(Location location);

}