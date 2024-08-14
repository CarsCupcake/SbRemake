package me.carscupcake.sbremake.worlds;

import lombok.Getter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.blocks.impl.Cobblestone;
import me.carscupcake.sbremake.blocks.impl.Stone;
import me.carscupcake.sbremake.blocks.impl.Titanium;
import me.carscupcake.sbremake.blocks.impl.mithril.*;
import me.carscupcake.sbremake.blocks.impl.ore.*;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.DownloadUtil;
import me.carscupcake.sbremake.util.MapList;
import me.carscupcake.sbremake.util.Returnable;
import me.carscupcake.sbremake.worlds.impl.*;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
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
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
public enum SkyblockWorld implements Returnable<SkyblockWorld.WorldProvider> {
    Hub("hub", FileEnding.ZIP, WorldProvider.VANILLA_ORES) {
        @Override
        public WorldProvider get() {
            return new Hub();
        }
    }, GoldMines("gold", FileEnding.ZIP, WorldProvider.VANILLA_ORES) {
        @Override
        public WorldProvider get() {
            return new GoldMines();
        }
    }, DeepCaverns("deep", FileEnding.ZIP, WorldProvider.VANILLA_ORES) {
        @Override
        public WorldProvider get() {
            return new DeepCaverns();
        }
    }, DwarvenMines("mines", FileEnding.ZIP, new Stone(), new Cobblestone(), new CoalOre(), new IronOre(), new GoldOre(), new LapisLazuliOre(), new RedstoneOre(), new EmeraldOre(), new DiamondBlock(), new DiamondOre(), new BlueMithril(), new CyanTerracottaMithril(), new DarkPrismarineMithril(), new GrayWoolMithril(), new PrismarineBrickMithril(), new PrismarineMithril(), new Titanium()) {
        @Override
        public WorldProvider get() {
            return new DwarvenMines();
        }
    },
    Park("park", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new Park();
        }
    },
    FarmingIsles("farming_isles", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new FarmingIsles();
        }
    };
    private static final Object _lock = new Object();
    private static final MapList<SkyblockWorld, WorldProvider> worlds = new MapList<>();
    private final String id;
    private final FileEnding fileEnding;
    private final MiningBlock[] ores;

    SkyblockWorld(String id, FileEnding fileEnding, MiningBlock... ores) {
        this.id = id;
        this.fileEnding = fileEnding;
        this.ores = (ores == null) ? new MiningBlock[0] : ores;
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
        if (worlds.get(world).isEmpty()) {
            return null;
        }
        return worlds.get(world).getFirst();
    }

    public static void getBestWorld(SkyblockWorld world, Consumer<WorldProvider> after) {
        if (worlds.get(world).isEmpty()) {
            WorldProvider provider = world.get();
            world.get().init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> after.accept(provider), true);
        } else
            after.accept(worlds.get(world).getFirst());
    }

    public static void sendToBest(WarpLocation warpLocation, SkyblockPlayer player) {
        getBestWorld(warpLocation.getWorld(), worldProvider -> worldProvider.addPlayer(player, warpLocation.getSpawn()));
    }

    public static void sendToBest(SkyblockWorld world, SkyblockPlayer player) {
        player.sendMessage(STR."§7Sending to \{world.getId()}");
        /*if (worlds.get(world).isEmpty()) {
            WorldProvider provider = world.get();
            world.get().init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> {
                System.out.println(":(");
                MinecraftServer.getSchedulerManager().buildTask(() -> player.setWorldProvider(provider)).delay(TaskSchedule.tick(1)).schedule();
            }, true);
            return;
        }
        player.setWorldProvider(worlds.get(world).getFirst());*/
        SkyblockWorld.WorldProvider provider = SkyblockWorld.getBestWorld(world);
        if (provider == null) {
            provider = world.get();
            player.sendMessage(STR."§7Starting \{world.id}");
            SkyblockWorld.WorldProvider finalProvider = provider;
            provider.init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> {
                synchronized (player) {
                    player.setWorldProvider(finalProvider);
                }
            });
        } else player.setWorldProvider(provider);
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
    @Getter
    public static abstract class WorldProvider {
        private static final MiningBlock[] VANILLA_ORES = {new Stone(), new Cobblestone(), new CoalOre(), new IronOre(), new GoldOre(), new LapisLazuliOre(), new RedstoneOre(), new EmeraldOre(), new DiamondBlock(), new DiamondOre()};

        private final Set<SkyblockPlayer> players = Collections.synchronizedSet(new HashSet<>());
        private final String id;
        protected Npc[] npcs;
        private final List<Launchpad> launchpads;
        private boolean loaded = false;
        public List<Runnable> onStart = new ArrayList<>();
        protected HashMap<SkyblockWorld, Pos> customEntry = new HashMap<>();

        public WorldProvider(List<Launchpad> launchpads, Npc... npcs) {
            this.npcs = (npcs == null) ? new Npc[0] : npcs;
            id = STR."\{type().id}\{getWorlds(type()).size()}";
            this.launchpads = launchpads;
        }

        public WorldProvider(Npc... npcs) {
            this(new ArrayList<>());
        }

        public abstract SkyblockWorld type();

        public DynamicRegistry.Key<DimensionType> getDimension() {
            return DimensionType.OVERWORLD;
        }

        @Getter
        public InstanceContainer container;

        public MiningBlock[] ores(Pos pos) {
            return type().getOres();
        }

        public void init() {
            init(MinecraftServer.getInstanceManager().createInstanceContainer());
        }

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
            if (after != null)
                onStart.add(after);
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
                    LightingChunk.relight(container, container.getChunks());
                    container.loadChunk(spawn().chunkX(), spawn().chunkZ());
                    synchronized (_lock) {
                        loaded = true;
                        for (Runnable runnable : onStart) runnable.run();
                    }
                });
                else {
                    CompletableFuture.allOf(chunks.toArray(CompletableFuture[]::new)).join();
                    LightingChunk.relight(container, container.getChunks());
                    container.loadChunk(spawn().chunkX(), spawn().chunkZ());
                }
                addWorld(this);
                register();
                for (Runnable runnable : onStart) runnable.run();
                Main.LOGGER.info(STR."Loaded \{type().getId()} Instance");
                loaded = true;
            } catch (Exception e) {
                Main.LOGGER.info("A world failed to load!");
                e.printStackTrace(System.err);
                Main.LOGGER.trace(STR."An Error occured while loading \{type().getId()}", e);
            }
        }

        public void init(InstanceContainer container, @Nullable Runnable after, boolean async) {
            if (loaded) {
                Main.LOGGER.error("Tried to load an already loaded instance!");
                return;
            }
            this.container = container;
            if (async) Thread.ofVirtual().factory().newThread(() -> init0(container, after, true)).start();
            else init0(container, after, false);
        }

        protected void register() {
        }

        public final void remove() {
            for (SkyblockPlayer player : players)
                player.kick("§cInstance is shutting down!");
            container.getEntities().forEach(Entity::remove);
            for (Chunk c : container.getChunks())
                container.getChunkLoader().unloadChunk(c);
            MinecraftServer.getInstanceManager().unregisterInstance(container);
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
            addPlayer(player, spawn());
        }

        public final void addPlayer(SkyblockPlayer player, SkyblockWorld previous) {
            addPlayer(player, customEntry.getOrDefault(previous, spawn()));
        }

        public final void addPlayer(SkyblockPlayer player, Pos spawn) {
            if (onPlayerAdd(player)) {
                if (shutdownTask != null) {
                    shutdownTask.cancel();
                    shutdownTask = null;
                }
                player.setWarping(true);
                if (player.getInstance() != container) {
                    player.setInstance(getContainer(), spawn);
                }
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    synchronized (_lock) {
                        initPlayer(player, spawn);
                    }
                }).delay(TaskSchedule.tick(5)).schedule();
            } else {
                player.sendMessage("§cYou are not allowed!");
            }
        }

        public void initPlayer(SkyblockPlayer player, Pos spawn) {
            players.add(player);
            player.spawn(spawn);
            for (Npc npc : npcs)
                npc.spawn(player);
        }

        private Task shutdownTask;

        public final void removePlayer(SkyblockPlayer player) {
            players.remove(player);
            if (players.isEmpty()) {
                shutdownTask = MinecraftServer.getSchedulerManager().buildTask(this::remove).delay(Duration.ofMinutes(5)).schedule();
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
        BufferedOutputStream dest;
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            System.out.println(STR."Extracting: \{entry.getName()}");
            int count;
            byte[] data = new byte[1024];

            if (entry.isDirectory()) {
                new File(STR."\{destFolder}/\{entry.getName()}").mkdirs();
                continue;
            } else {
                int di = entry.getName().lastIndexOf('/');
                if (di != -1) {
                    new File(STR."\{destFolder}/\{entry.getName().substring(0, di)}").mkdirs();
                }
            }
            FileOutputStream fos = new FileOutputStream(STR."\{destFolder}/\{entry.getName()}");
            dest = new BufferedOutputStream(fos);
            while ((count = zis.read(data)) != -1) dest.write(data, 0, count);
            dest.flush();
            dest.close();
            new File(zipFile).delete();
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
