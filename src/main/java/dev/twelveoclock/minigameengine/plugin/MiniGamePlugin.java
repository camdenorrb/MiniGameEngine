package dev.twelveoclock.minigameengine.plugin;

import dev.twelveoclock.minigameengine.config.MiniGamePluginConfig;
import dev.twelveoclock.minigameengine.minigame.MiniGame;
import dev.twelveoclock.minigameengine.minigame.marker.Marker;

import java.util.List;

public interface MiniGamePlugin {

	MiniGame create(final List<Marker> markers);

}