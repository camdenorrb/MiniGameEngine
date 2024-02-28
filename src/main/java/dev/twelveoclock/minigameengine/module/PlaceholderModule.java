package dev.twelveoclock.minigameengine.module;

import dev.twelveoclock.minigameengine.placeholder.Placeholder;
import dev.twelveoclock.minigameengine.placeholder.PlayerPlaceholder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class PlaceholderModule extends PluginModule {

    private final Map<String, Placeholder> placeholders = new HashMap<>();

    private final Map<String, PlayerPlaceholder> playerPlaceholders = new HashMap<>();


    public PlaceholderModule(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onEnable() {
        // TODO: Timers for updating placeholders
    }

    // Fills only global placeholders
    public String fillPlaceholders(String input) {

        // Fill global placeholders
        for (Map.Entry<String, Placeholder> entry : placeholders.entrySet()) {
            input = input.replace("{" + entry.getKey() + "}", entry.getValue().get());
        }

        return input;
    }

    // Fills global and player specific placeholders
    public String fillPlaceholders(String input, final Player player) {

        // Fill global placeholders
        input = fillPlaceholders(input);

        // Fill player specific placeholders
        for (Map.Entry<String, PlayerPlaceholder> entry : playerPlaceholders.entrySet()) {
            input = input.replace("{" + entry.getKey() + "}", entry.getValue().get(player));
        }

        return input;
    }


    public Map<String, Placeholder> getPlaceholders() {
        return placeholders;
    }

    public Map<String, PlayerPlaceholder> getPlayerPlaceholders() {
        return playerPlaceholders;
    }

}
