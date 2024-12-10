package me.carscupcake.sbremake;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.pets.Pets;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantment;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import me.carscupcake.sbremake.listeners.*;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.potion.IPotion;
import me.carscupcake.sbremake.player.potion.Potion;
import me.carscupcake.sbremake.player.skill.impl.*;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import me.carscupcake.sbremake.util.PlayerBrodcastOutputStream;
import me.carscupcake.sbremake.util.SkyblockSimpleLogger;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.worlds.Time;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.lan.OpenToLAN;
import net.minestom.server.network.packet.client.play.ClientDebugSampleSubscriptionPacket;
import net.minestom.server.network.packet.server.play.DebugSamplePacket;
import net.minestom.server.registry.Registry;
import net.minestom.server.timer.TaskSchedule;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author CarsCupcake
 */
public class Main {
    public static final Object _lock = new Object();
    public static volatile AtomicBoolean running = new AtomicBoolean(true);
    public static Thread CONSOLE_THREAD;
    public static volatile SkyblockSimpleLogger LOGGER;
    static long tickDelay = -1;

    public static void main(String[] args) throws Exception {
        MinecraftServer server = MinecraftServer.init();
        MinecraftServer.setBrandName("CarsCupcakes Skyblock Remake");
        LOGGER = new SkyblockSimpleLogger();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println(STR."Error occured on thread \{t.getName()}");
            LOGGER.error("", e);
        });
        ISbItem.init();
        LOGGER.info("Loading Recipes");
        long time = System.currentTimeMillis();
        Recipe.init();
        LOGGER.info(STR."Loaded \{Recipe.craftingRecipes.size()} Crafting Recipes in \{System.currentTimeMillis() - time}ms");
        MiningBlock.init();
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
        MinecraftServer.getGlobalEventHandler().addListener(ServerTickMonitorEvent.class, serverTickMonitorEvent -> tickDelay = (long) serverTickMonitorEvent.getTickMonitor().getTickTime());
        for (Potion potion : Potion.values()) IPotion.potions.put(potion.getId(), potion);
        MinecraftServer.getPacketListenerManager().setPlayListener(ClientDebugSampleSubscriptionPacket.class, (_, player) -> {
            //TODO return Debug Sample Packet
            player.sendPacket(new DebugSamplePacket(new long[]{tickDelay, tickDelay, 0, 50 - tickDelay}, DebugSamplePacket.Type.TICK_TIME));
        });
        for (SkyblockEnchantment enchantment : NormalEnchantment.values())
            SkyblockEnchantment.enchantments.put(enchantment.getId(), enchantment);
        Reforge.init();
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);
        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            Audiences.players().forEachAudience(audience -> {
                SkyblockPlayer player = (SkyblockPlayer) audience;
                player.save();
                player.kick("Server shutting down!");
                Time.save();
            });
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
                e.printStackTrace(System.err);
            }
        }
        for (var arg : args)
            if (arg.equals("--open-lan")) {
                OpenToLAN.open();
                break;
            }
        boolean cracked = false;
        try {
            cracked = Boolean.parseBoolean(args[1]);
        } catch (Exception _) {
        }
        if (!cracked) MojangAuth.init();
        System.out.println("Starting...");
        int port = 25565;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception _) {
        }
        server.start("127.0.0.1", port);
        System.out.println(STR."Started Server on port \{port}");
        CONSOLE_THREAD = java.lang.Thread.ofPlatform().name("Console").start(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            ConsoleSender console = new ConsoleSender();
            while (running.get()) {
                try {
                    String in = reader.readLine();
                    if (!running.get()) return;
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
    }
}
