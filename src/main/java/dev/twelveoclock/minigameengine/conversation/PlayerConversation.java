package dev.twelveoclock.minigameengine.conversation;

import dev.twelveoclock.minigameengine.MiniGameEnginePlugin;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class PlayerConversation {

    private final Player player;

    private final Map<String, CompletableFuture<String>> prompts = new HashMap<>();


    public PlayerConversation(final Player player) {
        this.player = player;
    }


    public CompletableFuture<String> request(final String prompt) {

        final CompletableFuture<String> responseFuture = new CompletableFuture<>();
        //player.beginConversation(new Conversation(MiniGameEnginePlugin.getPlugin(MiniGameEnginePlugin.class), player, ))

        new StringPrompt() {

            @NotNull
            @Override
            public String getPromptText(@NotNull final ConversationContext context) {
                return prompt;
            }

            @Nullable
            @Override
            public Prompt acceptInput(@NotNull final ConversationContext context, @Nullable final String input) {
                responseFuture.complete(input);
                return null;
            }

        };


        return responseFuture;
    }




}
