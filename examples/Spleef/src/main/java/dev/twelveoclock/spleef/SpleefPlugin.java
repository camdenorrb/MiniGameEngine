package dev.twelveoclock.spleef;

import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;
import dev.twelveoclock.minigameengine.minigame.stage.StageBuilder;
import dev.twelveoclock.spleef.stage.BasicSpleefStage;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class SpleefPlugin extends MiniGamePlugin {

	private static final Random random = ThreadLocalRandom.current();


	@Override
	protected MiniGame<SpleefPlugin> createGame() {

		if (this.listStages().length == 0) {
			throw new IllegalStateException("No stages have been registered for the Spleef mini-game.\n"
				+ " Please register at least one stage using the registerStage method in the onEnable method of your plugin.");
		}

		return new SpleefMiniGame(this, randomStage());
	}

	@Override
	public Map<String, StageBuilder<? extends Stage>> getStageBuilders() {
		return new HashMap<>(){{
			put("basic", new BasicSpleefStage.Builder());
		}};
	}


	public Stage randomStage() {
		return this.loadStage(this.listStages()[random.nextInt(this.listStages().length)]);
	}


	// TODO: Figure out how to do this when there are so many stages


}