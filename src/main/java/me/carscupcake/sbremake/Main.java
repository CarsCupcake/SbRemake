package me.carscupcake.sbremake;

import me.carscupcake.sbremake.blocks.CauldronHandler;
import me.carscupcake.sbremake.command.*;
import me.carscupcake.sbremake.command.testing.*;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.listeners.*;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.lan.OpenToLAN;
import net.minestom.server.instance.block.Block;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.SocketChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static final Object _lock = new Object();
    public static volatile AtomicBoolean running = new AtomicBoolean(true);
    public static volatile org.slf4j.Logger LOGGER;
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        MinecraftServer.setBrandName("CarsCupcakes Skyblock Remake");
        LOGGER = MinecraftServer.LOGGER;
        System.out.println("Server Initiated");
        System.out.println("Fetch Manager");
        Block.CAULDRON.withHandler(new CauldronHandler());
        SkyblockWorld.Hub.get().init(MinecraftServer.getInstanceManager().createInstanceContainer());
        System.out.println("Create Instance");
        ISbItem.init();
        MinecraftServer.getGlobalEventHandler().addListener(PlayerBlockPlaceEvent.class, new PlayerBlockPlaceListener());
        MinecraftServer.getGlobalEventHandler().addListener(PlayerBlockBreakEvent.class, new PlayerBlockBreakListener());
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, new AsyncPlayerConfigurationListener());
        MinecraftServer.getGlobalEventHandler().addListener(PlayerPacketOutEvent.class, new PacketOutListener());
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, new PlayerSpawnListener());
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChangeHeldSlotEvent.class, new SwapSlotListener());
        MinecraftServer.getGlobalEventHandler().addChild(Ability.ABILITY_NODE);
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        CommandManager commandManager = MinecraftServer.getCommandManager();
        //Commands from the demo Implementation (to lazy to code them by myself lol)
        commandManager.register(new TestCommand());
        commandManager.register(new EntitySelectorCommand());
        commandManager.register(new HealthCommand());
        commandManager.register(new LegacyCommand());
        commandManager.register(new DimensionCommand());
        commandManager.register(new ShutdownCommand());
        commandManager.register(new TeleportCommand());
        commandManager.register(new PlayersCommand());
        commandManager.register(new FindCommand());
        commandManager.register(new TitleCommand());
        commandManager.register(new BookCommand());
        commandManager.register(new ShootCommand());
        commandManager.register(new HorseCommand());
        commandManager.register(new EchoCommand());
        commandManager.register(new SummonCommand());
        commandManager.register(new RemoveCommand());
        commandManager.register(new GiveCommand());
        commandManager.register(new AutoViewCommand());
        commandManager.register(new SaveCommand());
        commandManager.register(new GamemodeCommand());
        commandManager.register(new ExecuteCommand());
        commandManager.register(new RedirectTestCommand());
        commandManager.register(new DebugGridCommand());
        commandManager.register(new DisplayCommand());
        commandManager.register(new NotificationCommand());
        commandManager.register(new TestCommand2());
        commandManager.register(new ConfigCommand());
        commandManager.register(new SidebarCommand());
        commandManager.register(new SetEntityType());
        commandManager.register(new RelightCommand());
        commandManager.register(new KillCommand());
        commandManager.register(new WeatherCommand());

        commandManager.register(new SetHealthCommand());
        commandManager.register(new GetItemCommand());
        commandManager.register(new SpawnDummyCommand());
        commandManager.register(new ToggleCommand("toggle"));

        OpenToLAN.open();
        MojangAuth.init();
        System.out.println("Starting...");
        int port = 25565;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ignore) {}
        server.start("127.0.0.1", port);
        System.out.println(STR."Started Server on port \{port}");
        java.lang.Thread.ofPlatform().daemon(true).name("Console").start(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            ConsoleSender console = new ConsoleSender();
            while (running.get()) {
                try {
                    String in = reader.readLine();
                    LOGGER.debug(in);
                    if (MinecraftServer.getCommandManager().commandExists(in.split(" ")[0])) {
                        LOGGER.info(STR."Executing \{in}");
                        synchronized (_lock) {
                            MinecraftServer.getCommandManager().execute(console, in);
                        }
                    } else {
                        System.out.println(STR."The command \{in.split(" ")[0]} is not existing!");
                    }
                } catch (Exception e) {
                    if (!running.get()) return;
                    LOGGER.trace("An Error occured while executing a command", e);

                }
            }
        });
        SkyblockPlayer.statsLoop();

    }

    public static File getFolderFromResource(String folder)
            throws URISyntaxException, IOException {

        // get path of the current running JAR
        String jarPath = Main.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();

        // file walks JAR
        URI uri = URI.create(STR."jar:file:\{jarPath}");
        try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            File f = new File(fs.getPath(folder).toAbsolutePath().toString());
            System.out.println(f.getAbsolutePath());
            System.out.println(Objects.requireNonNull(f.listFiles()).length);
            return f;
        }



    }
}
