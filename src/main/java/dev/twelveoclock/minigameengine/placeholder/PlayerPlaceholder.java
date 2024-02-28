package dev.twelveoclock.minigameengine.placeholder;

import org.bukkit.entity.Player;

import java.util.function.Function;

public final class PlayerPlaceholder {

    // Tick rate for updates
    private int tickRate;

    // Cached value from previous tick
    private String cache;

    // Use context to generate next placeholder
    private Function<Player, String> producer;


    public PlayerPlaceholder(final int tickRate, final Function<Player, String> producer) {
        this.tickRate = tickRate;
        this.producer = producer;
    }

    public void update(final Player player) {
        cache = producer.apply(player);
    }

    public String get(final Player player) {

        if (cache == null) {
            cache = producer.apply(player);
        }

        return cache;
    }

    public int getTickRate() {
        return tickRate;
    }

    public void setTickRate(final int tickRate) {
        this.tickRate = tickRate;
    }

}
