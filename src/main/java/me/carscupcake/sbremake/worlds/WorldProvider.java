package me.carscupcake.sbremake.worlds;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.blocks.impl.Cobblestone;
import me.carscupcake.sbremake.blocks.impl.Stone;
import me.carscupcake.sbremake.blocks.impl.ore.*;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.worlds.impl.PrivateIsle;
import me.carscupcake.sbremake.worlds.region.Region;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityHeadLookPacket;
import net.minestom.server.network.packet.server.play.EntityRotationPacket;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings({"unused", "ignored", "UnusedReturnValue"})
@Getter
public abstract class WorldProvider {
    private boolean isRelight;
    public static final MiningBlock[] VANILLA_ORES = {new Stone(), new Cobblestone(), new CoalOre(), new IronOre(), new GoldOre(), new LapisLazuliOre(), new RedstoneOre(), new EmeraldOre(), new DiamondBlock(), new DiamondOre()};

    private final Set<SkyblockPlayer> players = Collections.synchronizedSet(new HashSet<>());
    private final String id;
    protected AbstractNpc[] npcs;
    private final List<Launchpad> launchpads;
    private boolean loaded = false;
    public List<Runnable> onStart = new ArrayList<>();
    protected HashMap<SkyblockWorld, Pos> customEntry = new HashMap<>();
    @Getter
    public volatile InstanceContainer container;
    private final TaskScheduler npcTask = new TaskScheduler() {
        @Override
        public void run() {
            for (var npc : npcs) {
                for (var player : players) {
                    var dis = npc.getPos().distanceSquared(player.getPosition());
                    if (dis > 8 * 8) continue;
                    var dir = player.getPosition().add(0, player.getEyeHeight(), 0).sub(npc.getEyePosition());
                    var rot = npc.getPos().withDirection(dir);
                    player.sendPacket(new EntityRotationPacket(npc.getEntityId(), rot.yaw(), rot.pitch(), true));
                    player.sendPacket(new EntityHeadLookPacket(npc.getEntityId(), rot.yaw()));
                }
            }
        }
    };

    public void relight() {
        isRelight = true;
        LightingChunk.relight(container, container.getChunks());
        System.gc();
    }

    public WorldProvider(List<Launchpad> launchpads, AbstractNpc... npcs) {
        this.npcs = (npcs == null) ? new Npc[0] : npcs;
        id = (type().getId()) + (SkyblockWorld.getWorlds(type()).size());
        this.launchpads = launchpads;
    }

    protected List<LivingEntity> summonArmorStandFixture(String path) {
        return summonArmorStandFixture(path, null);
    }

    public ChunkLoader getChunkLoader() throws IOException {
        File f = type().updateFiles();
        if (this instanceof PrivateIsle pI)
            f = pI.findWorldFolder();
        return new AnvilLoader(f.toPath());
    }

    protected List<LivingEntity> summonArmorStandFixture(String path, @Nullable Pos offset) {
        List<LivingEntity> entities = new ArrayList<>();
        try {
            var element = JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(path))));
            var baseSection = new ConfigSection(element);
            if (offset == null) {
                offset = baseSection.get("playerPos", ConfigSection.POSITION, Pos.ZERO).asPos().withYaw(0).withPitch(0);
            }
            for (var section : baseSection.get("stands", ConfigSection.SECTION_ARRAY)) {
                var stand = new LivingEntity(EntityType.ARMOR_STAND);
                var meta = (ArmorStandMeta) stand.getEntityMeta();
                meta.setSmall(section.get("small", ConfigSection.BOOLEAN, false));
                meta.setHasNoBasePlate(!section.get("base", ConfigSection.BOOLEAN, false));
                meta.setHasArms(section.get("arms", ConfigSection.BOOLEAN, false));
                meta.setLeftArmRotation(section.get("leftArm", ConfigSection.EULERS_ANGLE, Vec.ZERO));
                meta.setRightArmRotation(section.get("rightArm", ConfigSection.EULERS_ANGLE, Vec.ZERO));
                meta.setLeftLegRotation(section.get("leftLeg", ConfigSection.EULERS_ANGLE, Vec.ZERO));
                meta.setRightLegRotation(section.get("rightLeg", ConfigSection.EULERS_ANGLE, Vec.ZERO));
                meta.setHeadRotation(section.get("head", ConfigSection.EULERS_ANGLE, Vec.ZERO));
                meta.setBodyRotation(section.get("body", ConfigSection.EULERS_ANGLE, Vec.ZERO));
                meta.setInvisible(section.get("invisible", ConfigSection.BOOLEAN, false));
                meta.setMarker(section.get("marker", ConfigSection.BOOLEAN, false));
                var equipment = section.get("equipment", ConfigSection.SECTION);
                stand.setEquipment(EquipmentSlot.HELMET, makeStackFromConfig(equipment.get("helmet", ConfigSection.SECTION)));
                stand.setEquipment(EquipmentSlot.CHESTPLATE, makeStackFromConfig(equipment.get("chestplate", ConfigSection.SECTION)));
                stand.setEquipment(EquipmentSlot.LEGGINGS, makeStackFromConfig(equipment.get("leggings", ConfigSection.SECTION)));
                stand.setEquipment(EquipmentSlot.BOOTS, makeStackFromConfig(equipment.get("boots", ConfigSection.SECTION)));
                stand.setEquipment(EquipmentSlot.MAIN_HAND, makeStackFromConfig(equipment.get("mainHand", ConfigSection.SECTION)));
                stand.setEquipment(EquipmentSlot.OFF_HAND, makeStackFromConfig(equipment.get("offHand", ConfigSection.SECTION)));
                stand.setInstance(container, section.get("relativePos", ConfigSection.POSITION, Pos.ZERO).add(offset));
                if (section.has("name")) {
                    stand.setCustomNameVisible(true);
                    stand.set(DataComponents.CUSTOM_NAME, Component.text(section.get("name", ConfigSection.STRING)));
                }
                stand.setNoGravity(true);
                entities.add(stand);
            }
        } catch (Exception e) {
            Main.LOGGER.trace("Failed to load armor stand fixture for asset " + path, e);
        }
        return entities;
    }

    private ItemStack makeStackFromConfig(ConfigSection section) {
        if (section == null) return ItemStack.AIR;
        var stack = new ItemBuilder(Objects.requireNonNull(Material.fromKey(Key.key(section.get("id", ConfigSection.STRING)))));
        if (stack.getMaterial() == Material.AIR) return ItemStack.AIR;
        if (stack.getMaterial() == Material.PLAYER_HEAD && section.has("textures"))
            stack.setHeadTexture(section.get("textures", ConfigSection.SECTION, new ConfigSection(new JsonObject())).get("value", ConfigSection.STRING));
        stack.setGlint(section.get("glint", ConfigSection.BOOLEAN, false));
        if (section.has("leather_color"))
            stack.setLeatherColor(new Color(section.get("leather_color", ConfigSection.INTEGER, 0)));
        return stack.build();
    }

    public WorldProvider(AbstractNpc... npcs) {
        this(new ArrayList<>(), npcs);
    }

    public abstract SkyblockWorld type();

    public abstract Pair<Pos, Pos> getChunksToLoad();

    public RegistryKey<@NotNull DimensionType> getDimension() {
        return type().getDimension();
    }


    public MiningBlock[] ores(Pos pos) {
        return type().getOres();
    }

    public Future<WorldProvider> initAsync() {
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            return executor.submit(() -> {
                init();
                return this;
            });
        }
    }

    public void init() {
        init(MinecraftServer.getInstanceManager().createInstanceContainer(getDimension()));
    }

    public void init(InstanceContainer container) {
        init(container, null);
    }

    public boolean useCustomMining() {
        return true;
    }


    public void init(@Nullable Runnable after) {
        init(MinecraftServer.getInstanceManager().createInstanceContainer(getDimension()), after, true);
    }

    public void init(InstanceContainer container, @Nullable Runnable after) {
        init(container, after, true);
    }

    protected Pair<Pos, Pos> toMinMaxPair(Pos pos1, Pos pos2) {
        return new Pair<>(new Pos(Math.min(pos1.x(), pos2.x()), 0, Math.min(pos1.z(), pos2.z())),
                new Pos(Math.max(pos1.x(), pos2.x()), 0, Math.max(pos1.z(), pos2.z())));
    }

    protected void init0(InstanceContainer container, @Nullable Runnable after, boolean async) {
        Main.LOGGER.debug("Loading in dimension {}", getDimension().key().value());
        container.setChunkSupplier(LightingChunk::new);
        if (after != null)
            onStart.add(after);
        try {
            container.setChunkLoader(getChunkLoader());
            var chunks = new ArrayList<CompletableFuture<Chunk>>();
            var span = getChunksToLoad();
            for (int chunkX = span.getFirst().chunkX(); chunkX <= span.getSecond().chunkX(); chunkX++)
                for (int chunkZ = span.getFirst().chunkZ(); chunkZ <= span.getSecond().chunkZ(); chunkZ++) {
                    chunks.add(container.loadChunk(chunkX, chunkZ));
                }
            if (async) CompletableFuture.runAsync(() -> {
                CompletableFuture.allOf(chunks.toArray(CompletableFuture[]::new)).join();
                MinecraftServer.getSchedulerManager().buildTask(() -> LightingChunk.relight(container, container.getChunks())).delay(Duration.ofSeconds(1)).schedule();
                MinecraftServer.getSchedulerManager().buildTask(System::gc).delay(Duration.ofSeconds(2)).schedule();
                synchronized (SkyblockWorld._lock) {
                    loaded = true;
                    register();
                    for (Runnable runnable : onStart) runnable.run();
                    container.setTime(Time.tick);
                }
            });
            else {
                CompletableFuture.allOf(chunks.toArray(CompletableFuture[]::new)).join();
                MinecraftServer.getSchedulerManager().buildTask(() -> LightingChunk.relight(container, container.getChunks())).delay(Duration.ofSeconds(1)).schedule();
                MinecraftServer.getSchedulerManager().buildTask(System::gc).delay(Duration.ofSeconds(2)).schedule();
                Main.LOGGER.debug("Relighting {} Chunks", container.getChunks().size());
                loaded = true;
                register();
                container.setTime(Time.tick);
            }
            SkyblockWorld.addWorld(this);
            if (!async)
                synchronized (SkyblockWorld._lock) {
                    for (Runnable runnable : onStart) runnable.run();
                }
            Main.LOGGER.debug("Loaded {} Instance", type().getId());
            loaded = true;
        } catch (Exception e) {
            Main.LOGGER.warn("A world failed to load!");
            e.printStackTrace(System.err);
            Main.LOGGER.trace("An Error occured while loading {}", type().getId(), e);
        }
    }

    public void init(@NotNull InstanceContainer container, @Nullable Runnable after, boolean async) {
        if (loaded) {
            Main.LOGGER.error("Tried to load an already loaded instance!");
            return;
        }
        this.container = container;
        if (async) Thread.ofVirtual().factory().newThread(() -> {
            init0(container, after, true);
            synchronized (SkyblockWorld._lock) {
                for (AbstractNpc npc : npcs)
                    AbstractNpc.npcs.put(npc.getEntityId(), npc);
            }
        }).start();
        else {
            init0(container, after, false);
            for (AbstractNpc npc : npcs)
                AbstractNpc.npcs.put(npc.getEntityId(), npc);
        }
    }

    protected void register() {
    }

    public final void remove() {
        for (SkyblockPlayer player : players)
            player.kick("§cInstance is shutting down!");
        container.getEntities().forEach(Entity::remove);
        unregister();
        for (Chunk c : container.getChunks())
            container.getChunkLoader().unloadChunk(c);
        MinecraftServer.getInstanceManager().unregisterInstance(container);
        SkyblockWorld.removeWorld(this);
        if (shutdownTask != null) shutdownTask.cancel();
        System.gc();
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
            Main.LOGGER.info("Adding {} to {}", ((TextComponent) player.getName()).content(), type().name());
            if (shutdownTask != null) {
                shutdownTask.cancel();
                shutdownTask = null;
            }
            if (!npcTask.isRunning())
                npcTask.repeatTask(1);
            player.setWarping(true);
            if (player.getInstance() != container) {
                player.setInstance(getContainer(), spawn).thenRun(() -> player.spawn(spawn));
            }
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                synchronized (SkyblockWorld._lock) {
                    initPlayer(player, spawn);
                }
            }).delay(TaskSchedule.tick(5)).schedule();
        } else {
            player.sendMessage("§cYou are not allowed!");
        }
    }

    public void initPlayer(SkyblockPlayer player, Pos spawn) {
        players.add(player);
        for (AbstractNpc npc : npcs)
            npc.spawn(player);
    }

    private Task shutdownTask;

    public final void removePlayer(SkyblockPlayer player) {
        players.remove(player);
        List<Integer> ids = new ArrayList<>();
        for (AbstractNpc npc : npcs) ids.add(npc.getEntityId());
        player.sendPacket(new DestroyEntitiesPacket(ids));
        if (players.isEmpty()) {
            shutdownTask = MinecraftServer.getSchedulerManager().buildTask(this::remove).delay(Duration.ofMinutes(5)).schedule();
            if (npcTask.isRunning())
                npcTask.cancel();
        }
    }

    public boolean onPlayerAdd(SkyblockPlayer player) {
        return true;
    }

    public abstract Pos spawn();

    public abstract Region[] regions();
}
