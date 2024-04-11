package dev.twelveoclock.minigameengine.minigame.stage;

import dev.twelveoclock.minigameengine.conversation.PlayerConversation;
import dev.twelveoclock.minigameengine.minigame.marker.Marker;
import dev.twelveoclock.minigameengine.part.Part;

import java.util.List;

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

    List<? extends Marker> getMarkers();

    List<? extends Part> getParts();

}