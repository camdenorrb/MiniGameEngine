package dev.twelveoclock.minigameengine.placeholder;

import java.util.function.Supplier;

public final class Placeholder {

    // Tick rate for updates
    private int tickRate;

    // Cached value from previous tick
    private String cache;

    // Use context to generate next placeholder
    private Supplier<String> producer;


    public Placeholder(final int tickRate, final Supplier<String> producer) {
        this.tickRate = tickRate;
        this.producer = producer;
    }

    public void update() {
        cache = producer.get();
    }

    public String get() {

        if (cache == null) {
            cache = producer.get();
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
