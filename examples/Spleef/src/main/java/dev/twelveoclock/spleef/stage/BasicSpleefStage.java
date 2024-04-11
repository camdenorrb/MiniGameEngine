package dev.twelveoclock.spleef.stage;

import dev.twelveoclock.minigameengine.conversation.PlayerConversation;
import dev.twelveoclock.minigameengine.minigame.marker.Marker;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;
import dev.twelveoclock.minigameengine.minigame.stage.StageBuilder;
import dev.twelveoclock.minigameengine.part.Part;
import org.bukkit.Material;
import org.yaml.snakeyaml.error.Mark;

import java.util.Arrays;
import java.util.List;

public class BasicSpleefStage extends Stage {

	@Override
	protected void start() {

	}

	@Override
	protected void stop() {

	}


	public enum Marker implements dev.twelveoclock.minigameengine.minigame.marker.Marker {
		SPAWN(Material.GOLD_BLOCK),
		BLOCK(Material.SNOW_BLOCK),
		DEATH(Material.REDSTONE_BLOCK);

		private final Material display;

		Marker(final Material display) {
			this.display = display;
		}

		public Material getDisplay() {
			return this.display;
		}

	}


	public enum Part implements dev.twelveoclock.minigameengine.part.Part {
		ICE,
		WALL
	}

	public static class Builder implements StageBuilder<BasicSpleefStage> {

		// Get more information before collecting markers
		@Override
		public BasicSpleefStage build(final PlayerConversation playerConversation) {
			return null;
		}

		@Override
		public List<Marker> getMarkers() {
			return List.of(Marker.values());
		}

		@Override
		public List<Part> getParts() {
			return List.of(Part.values());
		}

	}

}
