package me.carscupcake.sbremake;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantment;
import me.carscupcake.sbremake.item.modifiers.enchantment.SkyblockEnchantment;
import me.carscupcake.sbremake.listeners.*;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.skill.impl.CombatSkill;
import me.carscupcake.sbremake.player.skill.impl.FarmingSkill;
import me.carscupcake.sbremake.player.skill.impl.ForagingSkill;
import me.carscupcake.sbremake.player.skill.impl.MiningSkill;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.lan.OpenToLAN;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static final Object _lock = new Object();
    public static volatile AtomicBoolean running = new AtomicBoolean(true);
    public static volatile org.slf4j.Logger LOGGER;

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        MinecraftServer.setBrandName("CarsCupcakes Skyblock Remake");
        LOGGER = MinecraftServer.LOGGER;
        System.out.println("Server Initiated");/*
        SkyblockWorld.Hub.get().init(MinecraftServer.getInstanceManager().createInstanceContainer(), null, true);*/
        System.out.println("Fetch Manager");
        ISbItem.init();
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
        MinecraftServer.getGlobalEventHandler().addChild(Region.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(EnchantmentUtils.LISTENER);
        MinecraftServer.getGlobalEventHandler().addChild(HotmUpgrade.LISTENER);
        for (SkyblockEnchantment enchantment : NormalEnchantment.values())
            SkyblockEnchantment.enchantments.put(enchantment.getId(), enchantment);
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);
        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            Audiences.players().forEachAudience(audience -> {
                SkyblockPlayer player = (SkyblockPlayer) audience;
                player.save();
                player.kick("Server shutting down!");
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

        OpenToLAN.open();
        MojangAuth.init();
        System.out.println("Starting...");
        int port = 25565;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ignore) {
        }
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
        SkyblockPlayer.tickLoop();
    }
}
