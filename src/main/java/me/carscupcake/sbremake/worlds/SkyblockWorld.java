package me.carscupcake.sbremake.worlds;

import lombok.Getter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.DownloadUtil;
import me.carscupcake.sbremake.util.MapList;
import me.carscupcake.sbremake.util.Returnable;
import me.carscupcake.sbremake.worlds.impl.DwarvenMines;
import me.carscupcake.sbremake.worlds.impl.HubWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.utils.chunk.ChunkUtils;
import net.minestom.server.world.DimensionType;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.jetbrains.annotations.Nullable;
import org.kohsuke.github.GitHub;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
public enum SkyblockWorld implements Returnable<SkyblockWorld.WorldProvider> {
    Hub("hub", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new HubWorld();
        }
    }, DwarvenMines("mines", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new DwarvenMines();
        }
    };
    private static final Object _lock = new Object();
    private static final MapList<SkyblockWorld, WorldProvider> worlds = new MapList<>();
    private final String id;
    private final FileEnding fileEnding;

    SkyblockWorld(String id, FileEnding fileEnding) {
        this.id = id;
        this.fileEnding = fileEnding;
    }

    public static void addWorld(WorldProvider provider) {
        synchronized (_lock) {
            worlds.add(provider.type(), provider);
        }
    }

    public static void removeWorld(WorldProvider provider) {
        synchronized (_lock) {
            worlds.removeFromList(provider.type(), provider);
        }
    }

    public static List<WorldProvider> getWorlds(SkyblockWorld world) {
        synchronized (_lock) {
            return worlds.get(world);
        }
    }

    public static WorldProvider getBestWorld(SkyblockWorld world) {
        synchronized (_lock) {
            if (worlds.get(world).isEmpty()) return null;
            return worlds.get(world).getFirst();
        }
    }

    public static List<WorldProvider> getAllWorlds() {
        synchronized (_lock) {
            List<WorldProvider> providers = new ArrayList<>();
            for (List<WorldProvider> providerList : worlds.values())
                providers.addAll(providerList);
            return providers;
        }
    }

    public static SkyblockWorld from(String id) {
        for (SkyblockWorld world : values())
            if (world.getId().equals(id)) return world;
        return null;
    }

    @SuppressWarnings({"unused", "ignored", "preview", "UnusedReturnValue"})
    public static abstract class WorldProvider {
        private final Set<SkyblockPlayer> players = Collections.synchronizedSet(new HashSet<>());

        public abstract SkyblockWorld type();

        public DynamicRegistry.Key<DimensionType> getDimension() {
            return DimensionType.OVERWORLD;
        }

        @Getter
        public InstanceContainer container;

        public void init(InstanceContainer container) {
            init(container, null);
        }

        public boolean useCustomMining() {
            return true;
        }

        public void init(InstanceContainer container, @Nullable Runnable after) {
            init(container, after, true);
        }

        private void init0(InstanceContainer container, @Nullable Runnable after, boolean async) {
            try {
                File f = new File(STR."./worlds/\{type().getId()}");
                if (!f.exists()) {
                    Main.LOGGER.info("Downloading world!");
                    //Download the world from my SkyblockRemake Repo
                    GitHub gitHub = GitHub.connectAnonymously();
                    f.mkdirs();
                    File dir = new File("./worlds");
                    File tempFolder = new File("./temp");
                    tempFolder.mkdirs();
                    AtomicReference<File> file = new AtomicReference<>();
                    try {
                        file.set(DownloadUtil.navigate(gitHub.getUser("CarsCupcake").getRepository("SbRemake").getFileContent(STR."resources/worlds/\{type().getId()}.\{type().fileEnding.literal}").getDownloadUrl(), null, tempFolder));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        if (type().fileEnding == FileEnding.RAR) {
                            for (InputStream stream : extract(file.get().getAbsolutePath()).keySet()) {
                                BufferedInputStream in = new BufferedInputStream(stream);
                                FileOutputStream fout = new FileOutputStream(f);
                                final byte[] data = new byte[1024];
                                int count;
                                while ((count = in.read(data, 0, 1024)) != -1) {
                                    fout.write(data, 0, count);
                                }
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
                    f = new File(STR."./worlds/\{type().getId()}");
                }
                container.setChunkLoader(new AnvilLoader(f.toPath()));
                var chunks = new ArrayList<CompletableFuture<Chunk>>();
                ChunkUtils.forChunksInRange(0, 0, 32, (x, z) -> chunks.add(container.loadChunk(x, z)));
                if (async) CompletableFuture.runAsync(() -> {
                    CompletableFuture.allOf(chunks.toArray(CompletableFuture[]::new)).join();
                    System.out.println("load end");
                    LightingChunk.relight(container, container.getChunks());
                    System.out.println("light end");
                    container.loadChunk(spawn().chunkX(), spawn().chunkZ());
                    if (after != null) after.run();
                });
                else {
                    CompletableFuture.allOf(chunks.toArray(CompletableFuture[]::new)).join();
                    System.out.println("load end");
                    LightingChunk.relight(container, container.getChunks());
                    System.out.println("light end");
                    container.loadChunk(spawn().chunkX(), spawn().chunkZ());
                    if (after != null) after.run();
                }
                addWorld(this);
                register();
                Main.LOGGER.info(STR."Loaded \{type().getId()} Instance");
            } catch (Exception e) {
                Main.LOGGER.info("A world failed to load!");
                e.printStackTrace(System.err);
                Main.LOGGER.trace(STR."An Error occured while loading \{type().getId()}", e);
            }
        }

        public void init(InstanceContainer container, @Nullable Runnable after, boolean async) {
            this.container = container;
            if (async) Thread.ofVirtual().factory().newThread(() -> {
                init0(container, after, true);
            }).start();
            else init0(container, after, false);
        }

        protected void register() {
        }

        public final void remove() {
            removeWorld(this);
            unregister();
        }

        protected void unregister() {
        }

        /**
         * This method will run if a player joins the world
         *
         * @param player the player
         */
        public final void addPlayer(SkyblockPlayer player) {
            if (onPlayerAdd(player)) {
                player.setInstance(getContainer());
                players.add(player);
                player.spawn();
            } else {
                player.sendMessage("§cYou are not allowed!");
            }
        }

        public boolean onPlayerAdd(SkyblockPlayer player) {
            return true;
        }

        public abstract Pos spawn();

        public abstract Region[] regions();
    }

    private static Map<InputStream, String> extract(String filePath) throws IOException {
        Map<InputStream, String> extractedMap = new HashMap<>();

        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
        RandomAccessFileInStream randomAccessFileStream = new RandomAccessFileInStream(randomAccessFile);
        IInArchive inArchive = SevenZip.openInArchive(null, randomAccessFileStream);

        for (ISimpleInArchiveItem item : inArchive.getSimpleInterface().getArchiveItems()) {
            if (!item.isFolder()) {
                ExtractOperationResult result = item.extractSlow(data -> {
                    extractedMap.put(new BufferedInputStream(new ByteArrayInputStream(data)), item.getPath());

                    return data.length;
                }, "");

                if (result != ExtractOperationResult.OK) {
                    throw new RuntimeException(String.format("Error extracting archive. Extracting error: %s", result));
                }
            }
        }

        return extractedMap;
    }

    private static void getZipFiles(String zipFile, String destFolder) throws IOException {
        BufferedOutputStream dest = null;
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            System.out.println("Extracting: " + entry.getName());
            int count;
            byte[] data = new byte[1024];

            if (entry.isDirectory()) {
                new File(destFolder + "/" + entry.getName()).mkdirs();
                continue;
            } else {
                int di = entry.getName().lastIndexOf('/');
                if (di != -1) {
                    new File(destFolder + "/" + entry.getName().substring(0, di)).mkdirs();
                }
            }
            FileOutputStream fos = new FileOutputStream(destFolder + "/" + entry.getName());
            dest = new BufferedOutputStream(fos);
            while ((count = zis.read(data)) != -1) dest.write(data, 0, count);
            dest.flush();
            dest.close();
        }
    }

    enum FileEnding {
        ZIP("zip"), RAR("rar");
        private final String literal;

        FileEnding(String literal) {
            this.literal = literal;
        }
    }
}
