package dev.twelveoclock.minigameengine.minigame.stage;

import dev.twelveoclock.minigameengine.minigame.marker.Marker;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public abstract class Stage {

    // Set by the loader
    private final String name;

    // Set by the loader
    private final Map<Marker, List<Location>> markers;


    public Stage(final String name, final Map<Marker, List<Location>> markers) {
        this.name = name;
        this.markers = markers;
    }


    public String getName() {
        return name;
    }

    public Map<Marker, List<Location>> getMarkers() {
        return markers;
    }

}

