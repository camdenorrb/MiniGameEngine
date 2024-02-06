package dev.twelveoclock.spleef;

import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;
import dev.twelveoclock.minigameengine.minigame.stage.StageBuilder;
import dev.twelveoclock.spleef.stage.BasicSpleefStage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class SpleefPlugin extends MiniGamePlugin {

	private static final Random random = ThreadLocalRandom.current();


	@Override
	protected MiniGame<SpleefPlugin> createGame() {

		if (stages.length == 0) {
			throw new IllegalStateException("No stages have been registered for the Spleef mini-game.\n"
				+ " Please register at least one stage using the registerStage method in the onEnable method of your plugin.");
		}

		return new SpleefMiniGame(this, randomStage());
	}

	@Override
	protected Map<String, StageBuilder<? extends Stage>> getStageBuilders() {
		return new HashMap<>(){{
			put("basic", new BasicSpleefStage.BasicSpleefStageBuilder());
		}};
	}


	public Stage randomStage() {
		return stages[random.nextInt(stages.length)];
	}

}
