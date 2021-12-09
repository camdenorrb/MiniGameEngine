package dev.twelveoclock.minigameengine.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class PlayerConversation {

    private final Player player;

    private final List<>


    public PlayerConversation(final Player player) {
        this.player = player;
    }


    public void thing() {

        new CompletableFuture<String>();

        new StringPrompt(){

            @NotNull
            @Override
            public String getPromptText(@NotNull ConversationContext context) {
                context.getAllSessionData();
                return null;
            }

            @Nullable
            @Override
            public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
                return null;
            }

        }

    }




}
