package dev.twelveoclock.spleef;

import dev.twelveoclock.minigameengine.minigame.MiniGame;

public final class SpleefMiniGame extends MiniGame<SpleefPlugin> {

	public SpleefMiniGame(final SpleefPlugin plugin) {
		super(plugin);
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
