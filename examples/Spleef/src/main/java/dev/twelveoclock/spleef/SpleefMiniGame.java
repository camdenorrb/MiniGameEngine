package dev.twelveoclock.spleef;

import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;

public final class SpleefMiniGame extends MiniGame<SpleefPlugin> {

	public SpleefMiniGame(final SpleefPlugin plugin, final Stage stage) {
		super(plugin);
		stage.getData().markers().get(SpleefMarker.SPAWN);
	}


	@Override
	protected void start() {
		System.out.println("Hello World! (SpleefMiniGame)");
	}

	@Override
	protected void stop() {
		System.out.println("Goodbye World! (SpleefMiniGame)");
	}

}
