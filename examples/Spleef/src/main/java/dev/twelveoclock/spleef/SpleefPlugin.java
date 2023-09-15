package dev.twelveoclock.spleef;

import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.plugin.MiniGamePlugin;

public final class SpleefPlugin extends MiniGamePlugin {

	@Override
	protected MiniGame<SpleefPlugin> createGame() {
		return new SpleefMiniGame(this);
	}

}
