package dev.twelveoclock.minigameengine.conversation;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;

public interface PromptRequestMethod<T> {

    void request(final String prompt, final Player player, final Consumer<T> callback);

    default void removeRequest(final Player player) {}

    default void sendPrompt(final Player player, final String prompt) {
        player.sendMessage(prompt);
    }

    default void onBlockPlace(final BlockPlaceEvent event) {}

    default void onChat(final AsyncPlayerChatEvent event) {}

}