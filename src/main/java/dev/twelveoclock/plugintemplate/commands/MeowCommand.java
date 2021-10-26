package dev.twelveoclock.plugintemplate.commands;

import dev.twelveoclock.plugintemplate.utils.ChatUtils;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MeowCommand implements CommandExecutor {

    private final String catName;


    public MeowCommand(final String catName) {
        this.catName = catName;
    }


    @Override
    public boolean onCommand(
        @NotNull final CommandSender sender,
        @NotNull final Command command,
        @NotNull final String label,
        @NotNull final String[] args
    ) {

        if (!(sender instanceof final Player player)) {
            sender.sendMessage("This command can only be ran as a Player");
            return true;
        }

        final Location location = player.getLocation();

        location.getWorld().spawn(location, Cat.class, cat -> {
            cat.setInvulnerable(true);
            cat.setCatType(Cat.Type.BLACK);
            cat.setCollarColor(DyeColor.RED);
            cat.setCustomName(catName);
            cat.setCustomNameVisible(true);
        });

        player.sendMessage(ChatUtils.colorize("&a&lMeow!"));

        return true;
    }

}
