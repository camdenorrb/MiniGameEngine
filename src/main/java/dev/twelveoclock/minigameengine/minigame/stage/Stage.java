package dev.twelveoclock.minigameengine.minigame.stage;

import org.bukkit.Location;
import java.util.List;
import java.util.Map;

public abstract class Stage {

    // Set by the loader
    private String name;

    // Set by the loader
    private Map<String, List<Location>> markers;

}

