package dev.twelveoclock.minigameengine.minigame.stage;

import dev.twelveoclock.minigameengine.conversation.PlayerConversation;

/**
 * Used to build the stage from a player conversation
 *
 * @param <S> The stage type to build
 */
public interface StageBuilder<S> {

    /**
     * Build the stage using player input
     *
     * @param conversation The conversation to pull information from
     * @return The built Stage
     */
    S build(final PlayerConversation conversation);

}