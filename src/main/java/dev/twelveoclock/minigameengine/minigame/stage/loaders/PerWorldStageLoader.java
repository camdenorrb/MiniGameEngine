package dev.twelveoclock.minigameengine.minigame.stage.loaders;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.twelveoclock.minigameengine.minigame.stage.Stage;
import dev.twelveoclock.minigameengine.minigame.stage.StageData;
import dev.twelveoclock.minigameengine.minigame.stage.StageLoader;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public final class PerWorldStageLoader implements StageLoader {

    private final JavaPlugin plugin;


    public PerWorldStageLoader(final JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public Stage load(final StageData stageData) {

        try {

            final WorldCreator worldCreator = new WorldCreator("test").generator("VoidGenerator");
            final World world = worldCreator.createWorld();

            final WorldEdit worldEdit = WorldEdit.getInstance();
            final EditSession editSession = worldEdit.newEditSession(BukkitAdapter.adapt(world));

            final ClipboardFormat format = ClipboardFormats.findByFile(stageFile.toFile()); // TODO: Load part files
            final Clipboard clipboard = format.getReader(new FileInputStream(stageFile.toFile())).read();

            final Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(BlockVector3.at(0, 192, 0)) // Paste at center of the world
                    .ignoreAirBlocks(false)
                    .build();

            Operations.complete(operation);

        } catch (final IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void unload(final Stage stage) {

        plugin.getServer().unloadWorld(stage.getName(), true); // Need to teleport all players out of the world first

    }

    private StageData loadStageData(final Path stageFile, final World world) {
        return null;
    }

}
