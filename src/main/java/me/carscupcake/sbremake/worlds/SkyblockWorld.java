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
import me.carscupcake.sbremake.worlds.impl.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Getter
public enum SkyblockWorld implements Supplier<WorldProvider>, WorldSelector {
    PrivateIsle("Private Isle", "private_isle", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull WorldProvider getWorldProvider(List<WorldProvider> providers, SkyblockPlayer player) {
            for (WorldProvider provider : providers) {
                if (provider instanceof PrivateIsle privateIsle) {
                    if (privateIsle.getOwner().equals(player.getConfigId())) {
                        return privateIsle;
                    }
                }
            }
            try {
                return new PrivateIsle(player);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    },
    Hub("Hub", "hub", FileEnding.ZIP, WorldProvider.VANILLA_ORES) {
        @Override
        public WorldProvider get() {
            return new Hub();
        }
    }, GoldMines("Gold Mines", "gold", FileEnding.ZIP, WorldProvider.VANILLA_ORES) {
        @Override
        public WorldProvider get() {
            return new GoldMines();
        }
    }, DeepCaverns("Deep Caverns", "deep", FileEnding.ZIP, WorldProvider.VANILLA_ORES) {
        @Override
        public WorldProvider get() {
            return new DeepCaverns();
        }
    }, DwarvenMines("Dwarven Mines", "mines", FileEnding.ZIP, new Stone(), new Cobblestone(), new CoalOre(), new IronOre(), new GoldOre(), new LapisLazuliOre(), new RedstoneOre(), new EmeraldOre(), new DiamondBlock(), new DiamondOre(), new BlueMithril(), new CyanTerracottaMithril(), new DarkPrismarineMithril(), new GrayWoolMithril(), new PrismarineBrickMithril(), new PrismarineMithril(), new Titanium()) {
        @Override
        public WorldProvider get() {
            return new DwarvenMines();
        }
    },
    LegacyPark("Legacy Park", "park", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new LegacyPark();
        }
    },
    ThePark("Park", "the_park", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new ThePark();
        }
    },
    Galatea("Galatea", "galatea", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new Galatea();
        }
    },
    FarmingIsles("Farming Isles", "farming_isles", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new FarmingIsles();
        }
    },
    SpidersDen("Spiders Den", "spiders_den", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new SpidersDen();
        }
    },
    End("End", "end", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new End();
        }
    },
    CrimsonIsle("Crimson Isle", "crimson_isle", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            return new CrimsonIsle();
        }
    },
    Dungeon("Dungeon", "dungeon", FileEnding.ZIP) {
        @Override
        public WorldProvider get() {
            throw new UnsupportedOperationException("Dungeon cant be instantiated");
        }
    };
    public static final Object _lock = new Object();
    @Getter
    private static final MapList<SkyblockWorld, WorldProvider> worlds = new MapList<>();
    private final String id;
    private final FileEnding fileEnding;
    private final MiningBlock[] ores;
    private final String name;

    SkyblockWorld(String name, String id, FileEnding fileEnding, MiningBlock... ores) {
        this.id = id;
        this.name = name;
        this.fileEnding = fileEnding;
        this.ores = (ores == null) ? new MiningBlock[0] : ores;
    }

    public RegistryKey<DimensionType> getDimension() {
        return DimensionType.OVERWORLD;
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

    public static WorldProvider getBestWorld(SkyblockPlayer player, SkyblockWorld world) {
        return world.getWorldProvider(worlds.get(world), player);
    }


    public static void getBestWorld(SkyblockPlayer player, SkyblockWorld world, Consumer<WorldProvider> after) {
        var p = getBestWorld(player, world);
        if (p == null) {
            WorldProvider provider = world.get();
            world.get().init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> after.accept(provider), true);
        } else
            after.accept(p);
    }

    public static void sendToBest(WarpLocation warpLocation, SkyblockPlayer player) {
        if (player.isInWorldTransfer()) return;
        player.setInWorldTransfer(true);
        SkyblockWorld world = warpLocation.getWorld();
        player.sendMessage("§7Sending to " + (world.getId()));
        /*if (worlds.get(world).isEmpty()) {
            WorldProvider provider = world.get();
            world.get().init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> {
                System.out.println(":(");
                MinecraftServer.getSchedulerManager().buildTask(() -> player.setWorldProvider(provider)).delay(TaskSchedule.tick(1)).schedule();
            }, true);
            return;
        }
        player.setWorldProvider(worlds.get(world).getFirst());*/
        WorldProvider provider = SkyblockWorld.getBestWorld(player, world);
        if (provider == null) {
            provider = world.get();
        }
        if (!provider.isLoaded()) {
            player.sendMessage("§7Starting " + (world.id));
            WorldProvider finalProvider = provider;
            provider.init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> {
                synchronized (player) {
                    player.setWorldProvider(finalProvider, warpLocation);
                }
            });
        } else player.setWorldProvider(provider, warpLocation);
    }

    public static void sendToBest(SkyblockWorld world, SkyblockPlayer player) {
        player.sendMessage("§7Sending to " + (world.getId()));
        /*if (worlds.get(world).isEmpty()) {
            WorldProvider provider = world.get();
            world.get().init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> {
                System.out.println(":(");
                MinecraftServer.getSchedulerManager().buildTask(() -> player.setWorldProvider(provider)).delay(TaskSchedule.tick(1)).schedule();
            }, true);
            return;
        }
        player.setWorldProvider(worlds.get(world).getFirst());*/
        WorldProvider provider = SkyblockWorld.getBestWorld(player, world);
        if (provider == null) {
            provider = world.get();
        }
        if (!provider.isLoaded()) {
            player.sendMessage("§7Starting " + (world.id));
            WorldProvider finalProvider = provider;
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

    protected File findWorldFolder() {
        return new File("./worlds/" + (getId()));
    }

    public File updateFiles() throws IOException {
        File f = findWorldFolder();
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
                file.set(DownloadUtil.navigate(gitHub.getUser("CarsCupcake").getRepository("SbRemake").getFileContent("resources/worlds/" + (getId()) + "." + (fileEnding.literal)).getDownloadUrl(), null, tempFolder));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                if (fileEnding == FileEnding.RAR) {
                    for (InputStream stream : extract(file.get().getAbsolutePath()).keySet()) {
                        BufferedInputStream in = new BufferedInputStream(stream);
                        FileOutputStream fout = new FileOutputStream(f);
                        final byte[] data = new byte[1024];
                        int count;
                        while ((count = in.read(data, 0, 1024)) != -1) {
                            fout.write(data, 0, count);
                        }
                        fout.flush();
                        fout.close();
                        in.close();
                    }
                } else {
                    f.delete();
                    getZipFiles(file.get().getAbsolutePath(), dir.getAbsolutePath());
                    new File(dir, "world").renameTo(findWorldFolder());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            File wrongFile = new File("./worlds/Unidentified_Server");
            if (wrongFile.exists()) {
                f.delete();
                wrongFile.renameTo(findWorldFolder());
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
            return findWorldFolder();
        }
        return f;
    }

    public static Map<InputStream, String> extract(String filePath) throws IOException {
        Map<InputStream, String> extractedMap = new HashMap<>();

        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
        try (RandomAccessFileInStream randomAccessFileStream = new RandomAccessFileInStream(randomAccessFile)) {
            try (IInArchive inArchive = SevenZip.openInArchive(null, randomAccessFileStream)) {
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
            }
        }

        return extractedMap;
    }

    public static void getZipFiles(String zipFile, String destFolder) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Extracting: " + (entry.getName()));
                int count;
                byte[] data = new byte[1024];

                if (entry.isDirectory()) {
                    new File((destFolder) + "/" + (entry.getName())).mkdirs();
                    continue;
                } else {
                    int di = entry.getName().lastIndexOf('/');
                    if (di != -1) {
                        new File((destFolder) + "/" + (entry.getName().substring(0, di))).mkdirs();
                    }
                }
                try (FileOutputStream fos = new FileOutputStream((destFolder) + "/" + (entry.getName())); var dest = new BufferedOutputStream(fos)) {
                    while ((count = zis.read(data)) != -1) dest.write(data, 0, count);
                    dest.flush();
                    dest.close();
                    new File(zipFile).delete();
                }
            }
        }

    }


    @Getter
    public enum FileEnding {
        ZIP("zip"), RAR("rar");
        private final String literal;

        FileEnding(String literal) {
            this.literal = literal;
        }
    }
}
