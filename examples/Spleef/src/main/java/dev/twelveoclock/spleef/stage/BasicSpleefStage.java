package dev.twelveoclock.spleef.stage;

import dev.twelveoclock.minigameengine.conversation.PlayerConversation;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;
import dev.twelveoclock.minigameengine.minigame.stage.StageBuilder;

public class BasicSpleefStage extends Stage {

	@Override
	protected void start() {

	}

	@Override
	protected void stop() {

	}


	public static class BasicSpleefStageBuilder implements StageBuilder<BasicSpleefStage> {

		@Override
		public BasicSpleefStage build(final PlayerConversation playerConversation) {
			return null;
		}

	}

}
