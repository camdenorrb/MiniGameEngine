package dev.twelveoclock.minigameengine.commands;

import dev.twelveoclock.minigameengine.minigame.modules.MiniGamesModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class MiniGameCommand implements CommandExecutor {

    private final MiniGamesModule miniGamesModule;


    public MiniGameCommand(final MiniGamesModule miniGamesModule) {
        this.miniGamesModule = miniGamesModule;
    }


    @Override
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final Command command,
        @NotNull final String label,
        @NotNull final String[] args
    ) {

        switch (args[0].toLowerCase()) {
            case "start" -> start(sender, command, label, args);
            case "list" -> list(sender, command, label, args);
            case "stop" -> stop(sender, command, label, args);
        }

        return true;
    }


    private void start(
        @NotNull final CommandSender sender,
        @NotNull final Command command,
        @NotNull final String label,
        @NotNull final String[] args
    ) {

    }

    private void list(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String label,
            @NotNull final String[] args
    ) {
        miniGamesModule.
    }

    private void stop(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String label,
            @NotNull final String[] args
    ) {

    }

}
