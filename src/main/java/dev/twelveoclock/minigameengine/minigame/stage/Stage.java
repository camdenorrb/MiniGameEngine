package dev.twelveoclock.minigameengine.minigame.stage;

import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public abstract class Stage {

    // Set by the loader
    private final String name;

    // Set by the loader
    private final Map<String, List<Location>> markers;


    public Stage(final String name, final Map<String, List<Location>> markers) {
        this.name = name;
        this.markers = markers;
    }


    public String getName() {
        return name;
    }

    public Map<String, List<Location>> getMarkers() {
        return markers;
    }

}

