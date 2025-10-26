package me.carscupcake.sbremake.worlds.impl;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.item.minion.IMinionData;
import me.carscupcake.sbremake.item.minion.Minion;
import me.carscupcake.sbremake.item.minion.MinionRemoveReason;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.DownloadUtil;
import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.worlds.EntityNpc;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.WarpLocation;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.anvil.AnvilLoader;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.kohsuke.github.GitHub;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static me.carscupcake.sbremake.worlds.SkyblockWorld.extract;
import static me.carscupcake.sbremake.worlds.SkyblockWorld.getZipFiles;

@Getter
public class PrivateIsle extends SkyblockWorld.WorldProvider {
    public static final EventNode<Event> NODE = EventNode.all("privateIsle")
            .addListener(PlayerMoveEvent.class, playerMoveEvent -> {
                SkyblockPlayer player = (SkyblockPlayer) playerMoveEvent.getPlayer();
                if (player.getWorldProvider().type() == SkyblockWorld.Hub) {
                    var pos = player.getPosition();
                    if (pos.x() <= -1 && pos.x() >= -5 && pos.z() <= -62 && pos.z() >= -65 && ((long) pos.y()) == 70) {
                        SkyblockWorld.sendToBest(WarpLocation.PrivateIsle, player);
                    }
                }
            });
    public final Map<UUID, Minion> minions = new HashMap<>();
    private final UUID owner;
    @Setter
    private int maxMinions = 5;

    public PrivateIsle(UUID owner) throws IOException {
        super(new EntityNpc[]{new EntityNpc(new Pos(9, 100, 33), null, "Jerry", EntityType.VILLAGER)});
        this.owner = owner;
        customEntry.put(SkyblockWorld.Hub, new Pos(6.5, 100, 40.5, 180, 0));
        File dir = new File(ConfigFile.getConfigFolder(owner), "private_isle");
        if (!dir.exists() || !dir.isDirectory()) {

            File localCashed = new File("./worlds/%s".formatted(type().getId()));
            if (!localCashed.exists() || !localCashed.isDirectory()) {
                Main.LOGGER.debug("File not found in cache! Downloading...");
                dir = localCashed;
                File f = localCashed.getParentFile();
                GitHub gitHub = GitHub.connectAnonymously();
                f.mkdirs();
                File tempFolder = new File("./temp");
                tempFolder.mkdirs();
                AtomicReference<File> file = new AtomicReference<>();
                try {
                    file.set(DownloadUtil.navigate(gitHub.getUser("CarsCupcake").getRepository("SbRemake").getFileContent("resources/worlds/" + (type().getId()) + "." + (type().getFileEnding().getLiteral())).getDownloadUrl(), null, tempFolder));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (type().getFileEnding() == SkyblockWorld.FileEnding.RAR) {
                        for (InputStream stream : extract(file.get().getAbsolutePath()).keySet()) {
                            BufferedInputStream in = new BufferedInputStream(stream);
                            FileOutputStream fout = new FileOutputStream(f);
                            final byte[] data = new byte[1024];
                            int count;
                            while ((count = in.read(data, 0, 1024)) != -1) {
                                fout.write(data, 0, count);
                            }
                            fout.close();
                            in.close();
                            stream.close();
                        }
                    } else {
                        f.delete();
                        getZipFiles(file.get().getAbsolutePath(), dir.getAbsolutePath());
                        new File(dir, "world").renameTo(new File(dir, type().getId()));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                File wrongFile = new File("./worlds/Unidentified_Server");
                if (wrongFile.exists()) {
                    f.delete();
                    wrongFile.renameTo(new File(dir, type().getId()));
                }
                Arrays.stream(Objects.requireNonNull(tempFolder.listFiles())).forEach(file1 -> {
                    try {
                        file1.delete();
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                });
                tempFolder.delete();
                File fakeFolder = new File(dir, "world");

                if (fakeFolder.exists()) {
                    Arrays.stream(Objects.requireNonNull(fakeFolder.listFiles())).forEach(file1 -> {
                        try {
                            file1.delete();
                        } catch (Exception e) {
                            e.printStackTrace(System.err);
                        }
                    });
                }
                fakeFolder.delete();

            }
            FileUtils.copyDirectoryToDirectory(localCashed, findWorldFolder().getParentFile());
            Main.LOGGER.debug("Created new isle");
        }
    }

    public PrivateIsle(SkyblockPlayer owner) throws IOException {
        this(owner.getUuid());
    }

    @Override
    public Pair<Pos, Pos> getChunksToLoad() {
        return new Pair<>(new Pos(0, 0, 0), new Pos(0, 0, 0));
    }

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.PrivateIsle;
    }

    @Override
    public Pos spawn() {
        return new Pos(7.5, 100, 7.5);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }

    @Override
    protected void register() {
        ConfigFile config = new ConfigFile("minions", owner);
        for (var entry : config.as(ConfigSection.SECTIONS).entrySet()) {
            try {
                var minionData = entry.getValue();
                var id = minionData.get("id", ConfigSection.STRING);
                var level = minionData.get("level", ConfigSection.INTEGER);
                var pos = minionData.get("pos", ConfigSection.POSITION);
                addMinion(IMinionData.minions.get(id), level, new Pos(pos), UUID.fromString(entry.getKey()));
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }

    @Override
    protected void unregister() {
        for (var minion : minions.values()) {
            minion.remove(null, MinionRemoveReason.QUIT);
        }
        Path path = null;
        try {
            var f = AnvilLoader.class.getDeclaredField("path");
            f.setAccessible(true);
            path = (Path) f.get(container.getChunkLoader());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        Main.LOGGER.debug("Saving Private Isle to {}", path == null ? "null" : path.toAbsolutePath().toString());
        container.saveInstance().join();
        container.saveChunksToStorage().join();
        Main.LOGGER.debug("DONE!");
        super.unregister();
    }

    public File findWorldFolder() {
        return new File(ConfigFile.getConfigFolder(owner), "/private_isle");
    }

    @Override
    public boolean useCustomMining() {
        return false;
    }

    public boolean addMinion(IMinionData minion, int level, Pos location) {
        UUID uuid = UUID.randomUUID();
        while (minions.containsKey(uuid)) uuid = UUID.randomUUID();
        return addMinion(minion, level, location, uuid);
    }

    public boolean addMinion(IMinionData minion, int level, Pos location, UUID uuid) {
        if (minions.size() + 1 >= maxMinions) return false;
        minions.put(uuid, Minion.getMinion(minion, level, container, location, uuid.toString(), owner));
        return true;
    }

    public void pickupMinion(SkyblockPlayer player, Minion minion) {
        Assert.assertTrue(minions.containsValue(minion));
        minion.remove(player, MinionRemoveReason.PICKUP_MINION);
        minions.remove(minion.getId());
    }
}
