package dev.twelveoclock.minigameengine.minigame.stage;

import dev.twelveoclock.minigameengine.minigame.marker.Marker;
import dev.twelveoclock.minigameengine.position.BlockPosition;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record PartData(Map<Marker, Set<BlockPosition>> markers, Path schematic) {


    /**
     * Shifts all marker positions by a specified delta.
     * <p>
     * This method creates a new mapping of markers to their shifted positions based on the given delta values for x, y, and z coordinates.
     * It iterates over the current markers, calculates their new positions by adding the specified deltas to each one,
     * and then stores these updated positions in a new map. This approach ensures that the original positions of the markers
     * are not modified, preserving the immutability of the original markers' positions.
     *
     * @param deltaX The amount to shift the marker positions along the x-axis.
     * @param deltaY The amount to shift the marker positions along the y-axis.
     * @param deltaZ The amount to shift the marker positions along the z-axis.
     * @return A new map containing the markers associated with their shifted positions.
     */

    public Map<Marker, Set<BlockPosition>> getMarkersShifted(final int deltaX, final int deltaY, final int deltaZ) {

        final Map<Marker, Set<BlockPosition>> newMarkers = new HashMap<>(markers.size());

        for (final var entry : markers.entrySet()) {

            // Calculate the new positions for each marker by applying the specified deltas
            final Set<BlockPosition> newPositions = entry.getValue().stream()
                    .map(pos -> pos.add(deltaX, deltaY, deltaZ))
                    .collect(Collectors.toSet());

            // Store the marker with its updated positions in the new map
            newMarkers.put(entry.getKey(), newPositions);
        }

        return newMarkers;
    }

}
