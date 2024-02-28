package dev.twelveoclock.minigameengine.conversation;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class PlayerConversation implements Listener {

    private final Player player;

    private final JavaPlugin plugin;

    private boolean isActive = false;


    public PlayerConversation(final JavaPlugin plugin, final Player player) {
        this.plugin = plugin;
        this.player = player;
    }


    public void beginConversation() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        isActive = true;
    }

    public void endConversation(@Nullable final String reason) {
        HandlerList.unregisterAll(this);
        isActive = false;
    }


    public void prompt(final String prompt) {
        player.sendMessage(prompt);
    }

    public void waitForBlock(final Consumer<BlockPlaceEvent> callback) {
        waitForEvent(BlockPlaceEvent.class, event -> event.getPlayer().equals(player), callback);
    }

    public void waitForChat(final Consumer<AsyncPlayerChatEvent> callback) {
        waitForTargetEvent(AsyncPlayerChatEvent.class, callback);
    }


    public <E extends PlayerEvent> void waitForTargetEvent(
        final Class<E> eventClass,
        final Consumer<E> callback
    ) {
        waitForEvent(eventClass, event -> event.getPlayer().equals(player), callback);
    }

    public <E extends Event> void waitForEvent(
        final Class<E> eventClass,
        final Predicate<E> predicate, // Whether or not the event should be accepted
        final Consumer<E> callback
    ) {
        final EventListener<E> listener = new EventListener<>(eventClass, predicate, callback);
        plugin.getServer().getPluginManager().registerEvent(eventClass, listener, EventPriority.NORMAL, listener, plugin, true);
    }



    /*
    @EventHandler(ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent event) {
        if (event.getPlayer() == player && currentRequestMethod != null) {
            currentRequestMethod.onChat(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.getPlayer() == player && currentRequestMethod != null) {
            currentRequestMethod.onBlockPlace(event);
        }
    }
    */


    private static class EventListener<T extends Event> implements Listener, EventExecutor {

        private final Class<T> eventClass;

        private final Consumer<T> callback;

        private final Predicate<T> predicate;


        public EventListener(final Class<T> eventClass, final Predicate<T> predicate, final Consumer<T> callback) {
            this.eventClass = eventClass;
            this.predicate = predicate;
            this.callback = callback;
        }


        @Override
        public void execute(@NotNull final Listener listener, @NotNull final Event event) {
            //noinspection unchecked
            if (eventClass.isInstance(event) && predicate.test((T) event)) {
                //noinspection unchecked
                callback.accept((T) event);
                // Unregister self
                HandlerList.unregisterAll(this);
            }
        }

    }
}
