package me.carscupcake.sbremake;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.pets.Pets;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.item.modifiers.enchantment.UltimateEnchantments;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import me.carscupcake.sbremake.listeners.*;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.accessories.AccessoryBag;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.potion.IPotion;
import me.carscupcake.sbremake.player.potion.Potion;
import me.carscupcake.sbremake.player.skill.impl.*;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import me.carscupcake.sbremake.util.PlayerBrodcastOutputStream;
import me.carscupcake.sbremake.util.SkyblockSimpleLogger;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.util.lootTable.blockLoot.BlockLootTable;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.Time;
import me.carscupcake.sbremake.worlds.impl.PrivateIsle;
import me.carscupcake.sbremake.worlds.region.Region;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.minestom.server.LoggerProvider;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.lan.OpenToLAN;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.client.play.ClientConfigurationAckPacket;
import net.minestom.server.network.packet.client.play.ClientDebugSampleSubscriptionPacket;
import net.minestom.server.network.packet.server.play.DebugSamplePacket;
import net.minestom.server.timer.TaskSchedule;
import org.reflections.Reflections;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CarsCupcake
 */
public class Main {
    public static final Object _lock = new Object();
    private static final long startTime = System.currentTimeMillis();
    private static final String[] registerDefaultManagers = new String[]{"banner", "trapped_chest", "skull", "chest", "sign", "brewing_stand", "enchanting_table", "furnace", "dispenser", "daylight_detector", "jukebox", "beacon", "ender_chest", "dropper", "comparator", "bed", "hopper", "end_portal"};
    public static volatile AtomicBoolean running = new AtomicBoolean(true);
    public static Thread CONSOLE_THREAD;
    public static volatile SkyblockSimpleLogger LOGGER;
    public static volatile boolean isCracked = false;
    static long tickDelay = -1;
    private volatile static ConfigFile crackedRegistry;

    public static void main(String[] args) throws Exception {
        MinecraftServer.LoggerProvider = new SkyblockLoggerProvider();
        LOGGER = (SkyblockSimpleLogger) MinecraftServer.LoggerProvider.getLogger(Main.class);
        MinecraftServer.LOGGER = LOGGER;
        MinecraftServer server = MinecraftServer.init();
        MinecraftServer.setBrandName("CarsCupcakes Skyblock Remake");
        for (var s : registerDefaultManagers)
            MinecraftServer.getBlockManager().registerHandler("minecraft:" + s, () -> () -> Key.key(s));
        if (System.getenv().getOrDefault("DEVELOPEMENT", "false").equals("true")) {
            LOGGER.setLogLevel(0);
            LOGGER.isEnabledForLevel(Level.DEBUG);
            LOGGER.debug("Debug logging enabled");
        }
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println("Error occured on thread " + (t.getName()));
            LOGGER.error("", e);
            if (t.getName().equals("main")) System.exit(1);
        });
        ISbItem.init();
        loadBlockDrops();
        LOGGER.info("Loading Recipes");
        long time = System.currentTimeMillis();
        Recipe.init();
        LOGGER.info("Loaded {} Crafting Recipes in {}ms", Recipe.craftingRecipes.size(), System.currentTimeMillis() - time);
        MiningBlock.init();
        MinecraftServer.getPacketListenerManager().setConfigurationListener(ClientConfigurationAckPacket.class, (_, player) -> {
            System.out.println("IN: " + player.getName());
        });
        MinecraftServer.getGlobalEventHandler().addListener(PlayerBlockPlaceEvent.class, new PlayerBlockPlaceListener());
        MinecraftServer.getGlobalEventHandler().addListener(PlayerBlockBreakEvent.class, new PlayerBlockBreakListener());
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, new AsyncPlayerConfigurationListener());
        MinecraftServer.getGlobalEventHandler().addListener(PlayerPacketOutEvent.class, new PacketOutListener());
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, new PlayerSpawnListener());
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChangeHeldSlotEvent.class, new SwapSlotListener());
        MinecraftServer.getGlobalEventHandler().addChild(Ability.ABILITY_NODE);
        MinecraftServer.getGlobalEventHandler().addChild(Gui.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(CombatSkill.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(FarmingSkill.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(ForagingSkill.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(MiningSkill.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(Dungeoneering.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(FishingSkill.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(Region.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(EnchantmentUtils.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(HotmUpgrade.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(Potion.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(AlchemySkill.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(Pets.events);
        MinecraftServer.getGlobalEventHandler().addChild(PrivateIsle.NODE);
        MinecraftServer.getGlobalEventHandler().addChild(AccessoryBag.LISTENER);
        MinecraftServer.getGlobalEventHandler().addListener(ServerTickMonitorEvent.class, serverTickMonitorEvent -> tickDelay = (long) serverTickMonitorEvent.getTickMonitor().getTickTime());
        for (Potion potion : Potion.values()) IPotion.potions.put(potion.getId(), potion);
        MinecraftServer.getPacketListenerManager().setPlayListener(ClientDebugSampleSubscriptionPacket.class, (_, player) -> {
            //TODO return Debug Sample Packet
            player.sendPacket(new DebugSamplePacket(new long[]{tickDelay, tickDelay, 0, 50 - tickDelay}, DebugSamplePacket.Type.TICK_TIME));
        });
        for (SkyblockEnchantment enchantment : NormalEnchantments.values())
            SkyblockEnchantment.enchantments.put(enchantment.getId(), enchantment);
        for (SkyblockEnchantment enchantment : UltimateEnchantments.values())
            SkyblockEnchantment.enchantments.put(enchantment.getId(), enchantment);
        Reforge.init();
        MinecraftServer.getConnectionManager().setPlayerProvider((connection, profile) -> {
            UUID configId;
            synchronized (_lock) {
                configId = isCracked ? crackedRegistry.getOrSetDefault(profile.name(), ConfigSection.UUID, UUID.randomUUID()) : profile.uuid();
            }
            return new SkyblockPlayer(connection, profile, configId);
        });
        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            Audiences.players().forEachAudience(audience -> {
                if (isCracked) {
                    Main.LOGGER.info("Saving cracked player info");
                    synchronized (_lock) {
                        crackedRegistry.save();
                    }
                }
                SkyblockPlayer player = (SkyblockPlayer) audience;
                player.save();
                player.kick("Server shutting down!");
                Time.save();
            });
            LOGGER.debug("Unloading Worlds...");
            for (SkyblockWorld.WorldProvider provider : SkyblockWorld.getAllWorlds()) {
                provider.remove();
            }
        });
        CommandManager commandManager = MinecraftServer.getCommandManager();
        Reflections reflections = new Reflections("me.carscupcake.sbremake.command");
        for (Class<? extends Command> clazz : reflections.getSubTypesOf(Command.class)) {
            try {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) continue;
                Constructor<? extends Command> constructor = clazz.getConstructor();
                Command instance = constructor.newInstance();
                commandManager.register(instance);
            } catch (Exception e) {
                LOGGER.error("Error while instantiating {}", clazz, e);
            }
        }
        for (var arg : args)
            if (arg.equals("--open-lan")) {
                OpenToLAN.open();
                break;
            }
        try {
            isCracked = Boolean.parseBoolean(args[1]);
        } catch (Exception _) {
        }
        if (!isCracked) MojangAuth.init();
        else crackedRegistry = new ConfigFile("cracked-players");
        System.out.println("Starting...");
        int port = 25565;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception _) {
        }
        server.start("127.0.0.1", port);
        System.out.println("Started Server on port " + (port));
        CONSOLE_THREAD = java.lang.Thread.ofPlatform().name("Console").start(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            ConsoleSender console = new ConsoleSender();
            while (running.get()) {
                try {
                    String in = reader.readLine();
                    if (in == null) continue;
                    if (!running.get()) return;
                    LOGGER.debug(in);
                    if (MinecraftServer.getCommandManager().commandExists(in.split(" ")[0])) {
                        LOGGER.info("Executing " + (in));
                        synchronized (_lock) {
                            MinecraftServer.getCommandManager().execute(console, in);
                        }
                    } else {
                        System.out.println("The command " + (in.split(" ")[0]) + " is not existing!");
                    }
                } catch (Exception e) {
                    if (!running.get()) return;
                    LOGGER.trace("An Error occurred while executing a command", e);

                }
            }
        });
        System.setErr(new PrintStream(new PlayerBrodcastOutputStream(System.err)));
        /*Thread.ofPlatform().name("Error").start(() -> {
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        });*/
        SkyblockPlayer.tickLoop();
        Time.init();
        MinecraftServer.getSchedulerManager().scheduleTask(System::gc, TaskSchedule.seconds(5), TaskSchedule.minutes(5));
        LOGGER.info("Time to start took {}ms", System.currentTimeMillis() - startTime);
        if (isCracked) LOGGER.warn("------ Cracked Enabled - Note that this is not officially supported ------");
    }

    private static void loadBlockDrops() throws URISyntaxException {
        URI uri = Objects.requireNonNull(Main.class.getClassLoader().getResource("assets/loot/blocks")).toURI();
        try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            Path folderRootPath = fileSystem.getPath("assets/loot/blocks");
            try (Stream<Path> stream = Files.walk(folderRootPath)) {
                BlockLootTable.blockLootTables = stream.parallel().filter(b -> Block.fromKey(b.getFileName().toString().replace(".json", "")) != null).map(path -> {
                    try (var s = Files.newInputStream(path)) {
                        return new BlockLootTable(s, Block.fromKey(path.getFileName().toString().replace(".json", "")));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).filter(b -> b.getPools() != null).collect(Collectors.groupingBy(blockLootTable -> blockLootTable.getBlock().registry().id(), new SingleCollector<>()));
                LOGGER.info("Loaded {} block loot tables", BlockLootTable.blockLootTables.size());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Collection a single item
     *
     * @param <T> the type
     */
    public static class SingleCollector<T> implements Collector<T, SingleItem<T>, T> {

        @Override
        public Supplier<SingleItem<T>> supplier() {
            return SingleItem::new;
        }

        @Override
        public BiConsumer<SingleItem<T>, T> accumulator() {
            return (i, t) -> {
                if (i.value == null) i.value = t;
            };
        }

        @Override
        public BinaryOperator<SingleItem<T>> combiner() {
            return (a, a2) -> a.value != null ? a : a2;
        }

        @Override
        public Function<SingleItem<T>, T> finisher() {
            return i -> i.value;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.UNORDERED);
        }
    }

    public static class SingleItem<T> {
        private T value = null;
    }

    public static class SkyblockLoggerProvider implements LoggerProvider {

        private final SkyblockSimpleLogger logger =  new SkyblockSimpleLogger();
        @Override
        public ComponentLogger getLogger(Class<?> clazz) {
            return logger;
        }
    }
}
