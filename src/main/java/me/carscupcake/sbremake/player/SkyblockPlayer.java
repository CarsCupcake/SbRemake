package me.carscupcake.sbremake.player;

import com.google.gson.JsonObject;
import kotlin.Pair;
import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.blocks.Mining;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.SkyblockEntityProjectile;
import me.carscupcake.sbremake.event.*;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.item.impl.arrows.SkyblockArrow;
import me.carscupcake.sbremake.item.impl.bow.BowItem;
import me.carscupcake.sbremake.item.impl.bow.Shortbow;
import me.carscupcake.sbremake.item.impl.other.*;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantment;
import me.carscupcake.sbremake.player.hotm.HeartOfTheMountain;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.player.protocol.SetEntityEffectPacket;
import me.carscupcake.sbremake.player.skill.ISkill;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.*;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.util.item.InventoryBuilder;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import me.carscupcake.sbremake.util.quest.Dialog;
import me.carscupcake.sbremake.worlds.Launchpad;
import me.carscupcake.sbremake.worlds.Npc;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.WarpLocation;
import me.carscupcake.sbremake.worlds.region.Region;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.metadata.projectile.ProjectileMeta;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerRespawnEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;
import net.minestom.server.item.component.Tool;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.client.play.*;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.ServerPacketIdentifier;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Getter
@SuppressWarnings({"unused", "preview", "UnstableApiUsage"})
public class SkyblockPlayer extends Player {
    private static final UUID speedUUID = UUID.randomUUID();
    private static final Set<Class<? extends ISbItem>> COIN_ITEMS = Set.of(CoinItem1.class, CoinItem10.class, CoinItem100.class, CoinItem1000.class, CoinItem2000.class, CoinItem5000.class);
    private static final EventNode<Event> PLAYER_NODE = EventNode.all("player.events").addListener(EntityAttackEvent.class, event -> {
        final Entity source = event.getEntity();
        final Entity entity = event.getTarget();

        if (entity instanceof SkyblockPlayer target) {
            if (source.getEntityType() == EntityType.PLAYER) return;
            if (!(source instanceof SkyblockEntity sbEntity)) return;
            target.damage(sbEntity);
        }
    }).addListener(PlayerPacketEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        //Packet Logger
                /*if (!(event.getPacket() instanceof ClientChunkBatchReceivedPacket || event.getPacket() instanceof ClientPlayerPositionPacket || event.getPacket() instanceof ClientPlayerRotationPacket || event.getPacket() instanceof ClientPlayerPositionAndRotationPacket))
                    System.out.println(event.getPacket());*/
        if (event.getPacket() instanceof ClientTeleportConfirmPacket confirmPacket) {
            if (player.spawnTeleportId == confirmPacket.teleportId()) {
                player.scheduler().buildTask(() -> player.setNoGravity(false)).delay(TaskSchedule.tick(5)).schedule();
                player.spawnTeleportId = -1;
            }
        }
        if (event.getPacket() instanceof ClientAnimationPacket packet) {
            if (packet.hand() == Hand.MAIN) {
                long time = System.currentTimeMillis();
                long delta = time - player.lastAttack;
                Pos eyePos = player.getPosition().add(0, player.getEyeHeight(), 0);
                Set<Entity> entities = EntityUtils.getEntitiesInLine(eyePos, eyePos.add(player.getPosition().direction().normalize().mul(player.getStat(Stat.SwingRange)).asPosition()), player.getInstance());
                double low = Double.MAX_VALUE;
                SkyblockEntity entity = null;
                for (Entity e : entities) {
                    if (e instanceof SkyblockEntity sbEntity) {
                        double distance = sbEntity.getDistance(eyePos);
                        if (distance < low) {
                            low = distance;
                            entity = sbEntity;
                        }
                    }
                }
                if (entity != null && EntityUtils.blocksInSight(player.getInstance(), eyePos, eyePos.direction(), low)) {
                    entity = null;
                }
                        /*Entity result = player.getLineOfSightEntity(player.getStat(Stat.SwingRange), entity -> entity.getEntityType() != EntityType.PLAYER && entity instanceof LivingEntity);
                        SkyblockEntity entity = (result instanceof SkyblockEntity sb) ? sb : null;*/
                MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, entity, PlayerInteractEvent.Interaction.Left));
                if (delta >= player.attackCooldown()) {
                    if (entity != null && entity.getEntityType() != EntityType.PLAYER) {
                        player.lastAttack = time;
                        entity.damage(player);
                        return;
                    }
                }
                SbItemStack item = SbItemStack.from(player.getItemInHand(Hand.MAIN));
                if (item == null) return;
                if (item.sbItem() instanceof Shortbow shortbow && player.shortbowTask == null) {
                    for (Requirement requirement : item.sbItem().requirements())
                        if(!requirement.canUse(player, item.item())) {
                            player.sendMessage("§cYou cannot use this item!");
                            return;
                        }
                    if (delta < shortbow.getShortbowCooldown(player.getStat(Stat.AttackSpeed, true))) return;
                    player.lastAttack = time;
                    SkyblockPlayerArrow.shootBow(player, 1000L, item, (SkyblockArrow) SbItemStack.base(Material.ARROW).sbItem());
                    player.bowStartPull = -1;
                    return;
                }
            }
            return;
        }
        if (event.getPacket() instanceof ClientHeldItemChangePacket) {
            player.bowStartPull = -1;
            if (player.shortbowTask != null) {
                player.shortbowTask.cancel();
                player.shortbowTask = null;
            }
        }
        if (event.getPacket() instanceof ClientPlayerDiggingPacket packet) {
            if (packet.status() == ClientPlayerDiggingPacket.Status.UPDATE_ITEM_STATE) {
                if (player.bowStartPull < 0) return;
                if (player.getItemInHand(Hand.MAIN).material() != Material.BOW) return;
                long chargingTime = System.currentTimeMillis() - player.bowStartPull;
                player.bowStartPull = -1;
                SbItemStack item = SbItemStack.from(player.getItemInHand(Hand.MAIN));
                if (item == null || chargingTime < 0 || !(item.sbItem() instanceof BowItem)) return;
                for (Requirement requirement : item.sbItem().requirements())
                    if(!requirement.canUse(player, item.item())) {
                        player.sendMessage("§cYou cannot use this item!");
                        return;
                    }
                SkyblockPlayerArrow.shootBow(player, chargingTime, item, (SkyblockArrow) SbItemStack.base(Material.ARROW).sbItem());
            }
            if (packet.status() == ClientPlayerDiggingPacket.Status.STARTED_DIGGING) {
                MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, packet.blockPosition(), packet.blockFace(), PlayerInteractEvent.Interaction.Left));
                if (!player.getWorldProvider().useCustomMining()) {
                    Block block = player.getInstance().getBlock(packet.blockPosition());
                    double hardness = block.registry().hardness();
                    if (hardness > 0) {
                        ItemStack item = player.getItemInHand(Hand.MAIN);
                        Tool tool = item.get(ItemComponent.TOOL);
                        if (tool != null) {
                            for (Tool.Rule rule : tool.rules()) {
                                if (rule.blocks().test(block)) {
                                    if (rule.speed() == null) continue;
                                    double mult = rule.speed();
                                    SbItemStack sbItem = SbItemStack.from(item);
                                    int lvl = sbItem.getEnchantmentLevel(NormalEnchantment.Efficiency);
                                    if (lvl > 0) mult += Math.pow(lvl, 2) + 1;
                                    //TODO: add Haste
                                    if (player.getInstance().getBlock(player.getPosition()).isLiquid()) mult /= 5;
                                    if (!player.isOnGround()) mult /= 5;
                                    double damage = mult / hardness;
                                    damage /= 30;
                                    if (damage > 1) {
                                        event.setCancelled(player.getInstance().breakBlock(player, packet.blockPosition(), packet.blockFace(), true));
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            return;
        }
        if (event.getPacket() instanceof ClientUseItemPacket packet) {
            if (packet.hand() != Hand.MAIN) return;
            if (player.getItemInHand(Hand.MAIN).material() == Material.BOW) {
                SbItemStack item = SbItemStack.from(player.getItemInHand(Hand.MAIN));
                if (item.sbItem() instanceof Shortbow shortbow) {
                    for (Requirement requirement : item.sbItem().requirements())
                        if(!requirement.canUse(player, item.item())) {
                            player.sendMessage("§cYou cannot use this item!");
                            return;
                        }
                    long shootCd = shortbow.getShortbowCooldown(player.getStat(Stat.AttackSpeed, true));
                    player.lastShortbowKeepAlive = System.currentTimeMillis();
                    if (player.shortbowTask != null) {
                        return;
                    }
                    player.makeShortbowTask(shootCd, item);
                    return;
                }
                player.bowStartPull = System.currentTimeMillis();
                return;
            }
            long now = System.currentTimeMillis();
            if (now - player.lastInteractPacket < 100) return;
            player.lastInteractPacket = now;
            MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, PlayerInteractEvent.Interaction.Right));
            return;
        }

        if (event.getPacket() instanceof ClientPlayerBlockPlacementPacket packet) {
            if (packet.hand() != Hand.MAIN) return;
            long now = System.currentTimeMillis();
            if (now - player.lastInteractPacket < 100) return;
            player.lastInteractPacket = now;
            MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, packet.blockPosition(), packet.blockFace(), PlayerInteractEvent.Interaction.Right));
        }

        if (event.getPacket() instanceof ClientInteractEntityPacket packet) {
            if (packet.type() instanceof ClientInteractEntityPacket.Attack) {
                if (player.getDialog() != null) return;
                Npc npc = Npc.npcs.get(packet.targetId());
                if (npc != null) {
                    if (npc.getInteraction() != null)
                        npc.getInteraction().interact(player, PlayerInteractEvent.Interaction.Left);
                }
                return;
            }
            long now = System.currentTimeMillis();
            if (now - player.lastInteractPacket < 100) return;
            player.lastInteractPacket = now;
            Npc npc = Npc.npcs.get(packet.targetId());
            if (npc != null) {
                if (player.getDialog() != null) return;
                if (npc.getInteraction() != null)
                    npc.getInteraction().interact(player, PlayerInteractEvent.Interaction.Right);
            } else
                MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, player.getInstance().getEntities().stream().filter(entity -> entity.getEntityId() == packet.targetId()).findFirst().orElse(null), PlayerInteractEvent.Interaction.Right));
        }

    }).addListener(ProjectileCollideWithBlockEvent.class, event -> {
        event.getEntity().remove();
        event.setCancelled(true);
    }).addListener(ProjectileCollideWithEntityEvent.class, event -> {
        Entity shooter = ((ProjectileMeta) event.getEntity().getEntityMeta()).getShooter();
        Entity target = event.getTarget();
        event.getEntity().remove();

        if (!(target instanceof SkyblockEntity) && !(target instanceof SkyblockPlayer)) {
            event.setCancelled(true);
            return;
        }

        if (shooter instanceof SkyblockPlayer && target instanceof SkyblockPlayer) return;

        if (shooter instanceof SkyblockPlayer) {
            if (!(event.getEntity() instanceof SkyblockPlayerArrow projectile)) {
                Main.LOGGER.warn("Illegal Arrow Found!");
                event.getEntity().remove();
                return;
            }
            ((SkyblockEntity) target).damage(projectile);
            return;
        }

        if (target instanceof SkyblockPlayer player) {
            if (!(event.getEntity() instanceof SkyblockEntityProjectile projectile)) {
                event.setCancelled(true);
                return;
            }
            player.damage(projectile);
        }

    }).addListener(PickupItemEvent.class, event -> {
        final Entity entity = event.getLivingEntity();
        if (entity instanceof SkyblockPlayer player) {
            // Cancel event if player does not have enough inventory space
            final ItemStack itemStack = event.getItemEntity().getItemStack();
            SbItemStack sbItemStack = SbItemStack.from(itemStack);
            event.setCancelled(!player.addItem(sbItemStack.update()));
        }
    }).addListener(InventoryPreClickEvent.class, event -> {
        if (event.getInventory() != null) return;
        if (((SkyblockPlayer) event.getPlayer()).getGui() != null) return;
        if (event.getSlot() == 8) {
            event.setCancelled(true);
            ((SkyblockPlayer) event.getPlayer()).openSkyblockMenu();
            return;
        }
        if (event.getSlot() < 41 || event.getSlot() > 44) {
            if (event.getClickType() == ClickType.START_SHIFT_CLICK) {
                SbItemStack clicked = SbItemStack.from(event.getClickedItem());
                if (clicked == null) return;
                int slot = getSlot(clicked.sbItem().getType());
                if (slot < 0) {
                    event.setCancelled(event.getClickedItem().material().isArmor());
                    return;
                }
                if (!event.getPlayer().getInventory().getItemStack(slot).isAir()) {
                    return;
                }
                event.setCancelled(true);
                event.getPlayer().getInventory().setItemStack(slot, clicked.item());
                event.getPlayer().getInventory().setItemStack(event.getSlot(), ItemStack.of(Material.AIR));
                ((SkyblockPlayer) event.getPlayer()).recalculateArmor();
            }
            return;
        }
        SbItemStack cursor = SbItemStack.from(event.getCursorItem());
        if (cursor == null) {
            return;
        }
        if (getSlot(cursor.sbItem().getType()) != event.getSlot()) {
            event.setCancelled(true);
        }
    }).addListener(InventoryClickEvent.class, event -> {
        if (event.getInventory() != null || (event.getSlot() < 41 || event.getSlot() > 44)) return;
        ((SkyblockPlayer) event.getPlayer()).recalculateArmor();
    }).addListener(PlayerDisconnectEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        player.save();
    }).addListener(PlayerRespawnEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        event.setRespawnPosition(player.getWorldProvider().spawn());
        player.setSbHealth(player.getMaxSbHealth());
    }).addListener(ItemDropEvent.class, event -> {
        SbItemStack stack = SbItemStack.from(event.getItemStack());
        if (stack.sbItem() instanceof SkyblockMenu) {
            event.setCancelled(true);
            ((SkyblockPlayer) event.getPlayer()).openSkyblockMenu();
        }
        //Todo make item drop
    }).addListener(PlayerMoveEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        for (Launchpad launchpad : player.getWorldProvider().getLaunchpads())
            if (launchpad.inBox(player))
                launchpad.launch(player);

    });

    private static int getSlot(ItemType type) {
        return switch (type) {
            case Helmet -> EquipmentSlot.HELMET.armorSlot();
            case Chestplate -> EquipmentSlot.CHESTPLATE.armorSlot();
            case Leggings -> EquipmentSlot.LEGGINGS.armorSlot();
            case Boots -> EquipmentSlot.BOOTS.armorSlot();
            default -> -1;
        };
    }

    public static Task regenTask;
    private SkyblockWorld.WorldProvider worldProvider = null;
    @Setter
    public UpdateHealthPacket lastHealthPacket = null;
    @Getter
    public double mana = 100;
    private final ActionBar actionBar = new ActionBar(this);
    @Getter
    private double sbHealth;
    @Getter
    private long bowStartPull = -1;
    private long lastAttack = System.currentTimeMillis();
    private long lastShortbowKeepAlive = System.currentTimeMillis();
    private Task shortbowTask = null;
    @Getter
    private String defenseString = null;
    private int defenseStringTicks = 0;
    @Getter
    private boolean notEnoughMana = false;
    private int notEnoughManaTicks = 0;
    private boolean oftick = false;
    private long lastInteractPacket = 0;
    private final Map<FullSetBonus, Integer> fullSetBonuses = new HashMap<>();
    @Getter
    @Setter
    private Function<SkyblockPlayer, String[]>[] scoreboardDisplay = DefaultScoreboard.values();
    @Getter
    public final Sidebar sidebar = new Sidebar(Component.text("§6§lSKYBLOCK"));
    @Getter
    @Setter
    public Mining blockBreakScheduler = null;
    @Getter
    @Setter
    @Range(from = 0, to = Long.MAX_VALUE)
    private double coins;
    @Getter
    private Gui gui = null;
    @Getter
    @Setter
    private Region region = null;
    @Getter
    private final List<me.carscupcake.sbremake.item.collections.Collection> collections = new LinkedList<>();
    private final Map<Skill, ISkill> skills = new HashMap<>();
    @Getter
    @Setter
    private Dialog dialog = null;
    @Getter
    private final List<String> tags;
    private final Map<Powder, Integer> powder = new HashMap<>();
    @Getter
    @Setter
    private String powderString = null;
    @Getter
    private final HeartOfTheMountain hotm;
    @Getter
    private final MapList<Stat, Pair<PlayerStatEvent.PlayerStatModifier, TaskSchedule>> temporaryModifiers = new MapList<>() {
        @Override
        public void add(Stat key, Pair<PlayerStatEvent.PlayerStatModifier, TaskSchedule> value) {
            super.add(key, value);
            MinecraftServer.getSchedulerManager().buildTask(() -> removeFromList(key, value)).delay(value.getSecond()).schedule();
        }
    };
    @Getter
    @Setter
    private boolean onLaunchpad = false;
    @Getter
    @Setter
    private SkyblockWorld previous = null;
    @Getter
    @Setter
    private boolean warping;
    @Getter
    private final Deque<Pair<SbItemStack, Integer>> sellHistory = new ArrayDeque<>() {
        @Override
        public void push(@NotNull Pair<SbItemStack, Integer> sbItemStackIntegerPair) {
            super.push(sbItemStackIntegerPair);
            //We track up to 15 items
            if (size() > 15) removeLast();
        }
    };

    public SkyblockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
        ConfigFile file = new ConfigFile("defaults", this);
        coins = file.get("coins", ConfigSection.DOUBLE, 0d);
        tags = new ArrayList<>(List.of(file.get("tags", ConfigSection.STRING_ARRAY, new String[0])));
        for (Skill skill : Skill.values())
            skills.put(skill, skill.instantiate(this));
        ConfigSection section = file.get("powder", ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        for (Powder p : Powder.values()) {
            powder.put(p, section.get(p.getId(), ConfigSection.INTEGER, 0));
        }
        sbHealth = getMaxSbHealth();
        setNoGravity(true);
        Reflections reflections = new Reflections("me.carscupcake.sbremake.item.collections.impl");
        for (Class<? extends me.carscupcake.sbremake.item.collections.Collection> clazz : reflections.getSubTypesOf(me.carscupcake.sbremake.item.collections.Collection.class)) {
            try {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) continue;
                Constructor<? extends me.carscupcake.sbremake.item.collections.Collection> constructor = clazz.getConstructor(SkyblockPlayer.class);
                collections.add(constructor.newInstance(this));
            } catch (Exception e) {
                kick("§cError while loading");
                throw new RuntimeException(e);
            }
        }
        this.hotm = new HeartOfTheMountain(this);
    }

    public void openSkyblockMenu() {
        InventoryBuilder inventoryBuilder = new InventoryBuilder(6, "Skyblock Menu").fill(TemplateItems.EmptySlot.getItem());
        PlayerSkin s = getSkin();
        ItemBuilder profileItem = new ItemBuilder(Material.PLAYER_HEAD);
        if (s != null)
            profileItem.setHeadTexture(s.textures());
        profileItem.setName("§aYour Skyblock Profile").addAllLore("§7View your equipment, stats,").addAllLore("§7and more!").addAllLore("§7 ");
        for (Stat stat : Stat.values())
            profileItem.addLoreRow(STR."\{stat} §f\{getStat(stat)}\{stat.isPercentValue() ? "%" : ""}");
        inventoryBuilder.setItem(profileItem.addAllLore("§7  ").addLoreRow("§eClick to view!").build(), 13).setItem(new ItemBuilder(Material.DIAMOND_SWORD).setName("§aYour Skills").addAllLore("§7View your skills progression", "§7and rewards.", "§7 ", STR."§6\{StringUtils.cleanDouble(skillsAverage())} Skill Avearage", "§e ", "§eClick to view!").build(), 19).setItem(new ItemBuilder(Material.PAINTING).setName("§aCollections §c§lWIP").build(), 20).setItem(new ItemBuilder(Material.BOOK).setName("§aRecipe Book §c§lWIP").build(), 21).setItem(new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdkODg1YjMyYjBkZDJkNmI3ZjFiNTgyYTM0MTg2ZjhhNTM3M2M0NjU4OWEyNzM0MjMxMzJiNDQ4YjgwMzQ2MiJ9fX0=").setName("§aSkyblock Leveling §c§lWIP").build(), 22).setItem(new ItemBuilder(Material.WRITABLE_BOOK).setName("§aQuest Log §c§lWIP").build(), 23).setItem(new ItemBuilder(Material.CLOCK).setName("§aCalendar and Events §c§lWIP").build(), 24).setItem(new ItemBuilder(Material.CHEST).setName("§aStorage §c§lWIP").build(), 25).setItem(new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYxYTkxOGMwYzQ5YmE4ZDA1M2U1MjJjYjkxYWJjNzQ2ODkzNjdiNGQ4YWEwNmJmYzFiYTkxNTQ3MzA5ODVmZiJ9fX0=").setName("§aYour Bags §c§lWIP").build(), 29).setItem(new ItemBuilder(Material.BONE).setName("§aPets §c§lWIP").build(), 30).setItem(new ItemBuilder(Material.CRAFTING_TABLE).setName("§aCrafting Table §c§lWIP").build(), 31).setItem(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(new DyedItemColor(0x3e05af)).setName("§aWardrobe §c§lWIP").build(), 32).setItem(new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=").setName("§aPersonal Bank §c§lWIP").build(), 33).setItem(new ItemBuilder(Material.BARRIER).setName("§cClose").build(), 49);
        Gui gui = new Gui(inventoryBuilder.build());
        gui.setCancelled(true);
        gui.getClickEvents().add(31, clickType -> {
            Recipe.openCraftingGui(SkyblockPlayer.this);
            return true;
        });
        gui.showGui(this);
    }

    public double skillsAverage() {
        int i = 0;
        double total = 0;
        for (ISkill skill : skills.values()) {
            if (skill.isCosmetic()) continue;
            i++;
            total += skill.getLevel();
        }
        return total / i;
    }

    public void save() {
        ConfigFile configFile = new ConfigFile("inventory", this);
        configFile.setRawElement(new JsonObject());
        for (int i = 0; i < this.getInventory().getSize(); i++) {
            SbItemStack item = SbItemStack.from(this.getInventory().getItemStack(i));
            if (item == null) continue;
            configFile.set(STR."\{i}", item, ConfigSection.ITEM);
        }
        configFile.save();
        Main.LOGGER.info(STR."Saved profile from \{((TextComponent) this.getName()).content()}");
        ConfigFile defaults = new ConfigFile("defaults", this);
        defaults.set("world", this.getWorldProvider().type().getId(), ConfigSection.STRING);
        defaults.set("coins", this.coins, ConfigSection.DOUBLE);
        defaults.set("tags", tags.toArray(new String[0]), ConfigSection.STRING_ARRAY);
        ConfigSection powders = defaults.get("powder", ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        for (Map.Entry<Powder, Integer> powderIntegerEntry : powder.entrySet()) {
            powders.set(powderIntegerEntry.getKey().getId(), powderIntegerEntry.getValue(), ConfigSection.INTEGER);
        }
        defaults.set("powder", powders, ConfigSection.SECTION);
        defaults.save();
        for (ISkill skill : this.skills.values()) skill.save();
        collections.forEach(me.carscupcake.sbremake.item.collections.Collection::save);
        hotm.save();
    }

    public ISkill getSkill(Skill skill) {
        return skills.get(skill);
    }

    public void closeGui() {
        setGui(null);
    }

    public int getPowder(Powder type) {
        return powder.get(type);
    }

    public void addPowder(Powder type, int amount) {
        int newAmount = powder.get(type) + amount;
        setPowder(type, newAmount);
        powderString = STR."\{type.getColor()}᠅ §f\{type.getName()}: \{type.getColor()}\{StringUtils.toFormatedNumber(newAmount)}";
    }

    public void setPowder(Powder type, @Range(from = 0, to = Integer.MAX_VALUE) int amount) {
        powder.put(type, amount);
    }

    public void removePowder(Powder type, int amount) {
        int current = powder.get(type);
        setPowder(type, current - amount);
    }

    public void setGui(Gui gui) {
        if (gui == null) {
            if (this.gui == null) return;
            closeInventory();
            this.gui.getPlayers().remove(this);
            this.gui = null;
            return;
        }
        openInventory(gui.getInventory());
        this.gui = gui;
    }

    public void setDefenseString(String s) {
        defenseString = s;
        defenseStringTicks = 2;
    }

    public void setNotEnoughMana() {
        notEnoughMana = true;
        notEnoughManaTicks = 2;
    }

    //For some reason the default implementation does not work :/
    @Override
    public void sendMessage(@NotNull String message) {
        SystemMessagePackage chatMessage = new SystemMessagePackage(message, false);
        sendPacket(chatMessage);
    }

    @Override
    public void sendMessage(final @NotNull Component message) {
        SystemMessagePackage messagePackage = new SystemMessagePackage(message, false);
        sendPacket(messagePackage);
    }

    /**
     * This is to set up stuff, when the player gets spawned (respawn or server join)
     */

    private int spawnTeleportId = 0;

    public void spawn() {
        spawn((previous == null) ? worldProvider.spawn() : worldProvider.getCustomEntry().getOrDefault(previous, worldProvider.spawn()));
    }

    public void spawn(Pos spawn) {
        super.spawn();
        recalculateArmor();
        setHealth(getMaxHealth());
        setSbHealth(getMaxSbHealth());
        instance.loadChunk(spawn.chunkX(), spawn.chunkZ());
        setNoGravity(true);
        spawnTeleportId = getNextTeleportId();
        PlayerPositionAndLookPacket packet = new PlayerPositionAndLookPacket(spawn, (byte) 0, spawnTeleportId);
        sendPacket(packet);
        sendPacket(new ClearTitlesPacket(true));
        getInventory().setItemStack(8, ISbItem.get(SkyblockMenu.class).create().item());
        SbItemStack item = SbItemStack.from(getItemInHand(Hand.MAIN));
        warping = false;
        if (item != null) setItemInHand(Hand.MAIN, item.update().item());
        clearEffects();
        if (worldProvider.useCustomMining()) {
            sendPacket(new SetEntityEffectPacket(getEntityId(), PotionEffect.MINING_FATIGUE.id(), 255, -1, (byte) 0));
            sendPacket(new SetEntityEffectPacket(getEntityId(), PotionEffect.HASTE.id(), 0, -1, (byte) 0));
        } else {
            sendPacket(new RemoveEntityEffectPacket(getEntityId(), PotionEffect.MINING_FATIGUE));
            sendPacket(new RemoveEntityEffectPacket(getEntityId(), PotionEffect.HASTE));
        }
        if (!sidebar.isViewer(this)) sidebar.addViewer(this);

    }

    public float getMaxHealth() {
        return (float) getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }

    public void setWorldProvider(SkyblockWorld.WorldProvider provider) {
        if (worldProvider != null && provider != worldProvider) {
            worldProvider.removePlayer(this);
            previous = worldProvider.type();
            this.worldProvider = provider;
            provider.addPlayer(this, previous);
        }
        this.worldProvider = provider;
        onLaunchpad = false;
    }

    public void setWorldProvider(SkyblockWorld.WorldProvider provider, WarpLocation location) {
        if (worldProvider != null && provider != worldProvider) {
            worldProvider.removePlayer(this);
            previous = location.getWorld();
            this.worldProvider = provider;
            provider.addPlayer(this, location.getSpawn());
        }
        this.worldProvider = provider;
        onLaunchpad = false;
    }

    public void addCoins(int i) {
        coins += i;
    }

    public void removeCoins(int i) {
        if (coins - i < 0) throw new IllegalStateException("Coins are not allowed to be negative");
        coins -= i;
    }

    private void makeShortbowTask(long shortbowCd, SbItemStack item) {
        long now = System.currentTimeMillis();
        Shortbow sb = (Shortbow) item.sbItem();
        this.shortbowTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (System.currentTimeMillis() - lastShortbowKeepAlive > 249 || isRemoved() || isDead()) {
                shortbowTask.cancel();
                shortbowTask = null;
                return;
            }
            long cd = sb.getShortbowCooldown(getStat(Stat.AttackSpeed, true));
            if (cd != shortbowCd) {
                shortbowTask.cancel();
                makeShortbowTask(cd, item);
                return;
            }
            long n = System.currentTimeMillis();
            if (n - lastAttack < shortbowCd) {
                Main.LOGGER.warn("Unusual Shortbow behaviour!");
                return;
            }
            lastAttack = System.currentTimeMillis();
            SkyblockPlayerArrow.shootBow(SkyblockPlayer.this, 1000L, item, (SkyblockArrow) SbItemStack.base(Material.ARROW).sbItem());
        }).delay(TaskSchedule.millis((now - lastAttack < shortbowCd) ? shortbowCd - (now - lastAttack) : 0)).repeat(TaskSchedule.millis(shortbowCd)).schedule();
    }


    public static void tickLoop() {
        MinecraftServer.getGlobalEventHandler().addChild(PLAYER_NODE);
        regenTask = MinecraftServer.getSchedulerManager().buildTask(() -> Audiences.players().forEachAudience(audience -> {
            SkyblockPlayer player = (SkyblockPlayer) audience;
            if (!player.oftick) {
                player.oftick = true;
                double maxSbHp = player.getMaxSbHealth();
                if (player.getSbHealth() != maxSbHp) {
                    double healthGained = (1.5 + maxSbHp / 100d) * player.getStat(Stat.HealthRegen) / 100;
                    HealthRegenEvent event = new HealthRegenEvent(player, healthGained);
                    MinecraftServer.getGlobalEventHandler().call(event);
                    player.addSbHealth((float) event.getRegenAmount());
                }
                double maxHealth = getMaxHearts(player.getMaxSbHealth());
                if (maxHealth != player.getMaxHealth()) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((float) maxHealth);
                    player.updateHpBar();
                }
            } else player.oftick = false;

            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue((float) (0.1 * (player.getStat(Stat.Speed) / 100d)));

            double maxManaPool = player.getManaPool();
            if (maxManaPool > player.getMana()) {
                double manaGained = maxManaPool * 0.02d;
                ManaRegenEvent e = new ManaRegenEvent(player, manaGained);
                MinecraftServer.getGlobalEventHandler().call(e);
                player.addMana(e.getRegenAmount() * e.getMultiplier());
            }

            ActionBarPacket packet = new ActionBarPacket(Component.text(player.actionBar.build()));
            if (player.defenseString != null) {
                player.defenseStringTicks--;
                if (player.defenseStringTicks == 0) player.defenseString = null;
            }
            if (player.notEnoughMana) {
                player.notEnoughManaTicks--;
                if (player.notEnoughManaTicks == 0) player.notEnoughMana = false;
            }
            player.sendPacket(packet);
            int lines = 0;
            Set<Sidebar.ScoreboardLine> lineCopie = new HashSet<>(player.sidebar.getLines());
            for (Function<SkyblockPlayer, String[]> display : player.scoreboardDisplay) {
                String[] l = display.apply(player);
                for (String s : l) {
                    Component text = Component.text(s);
                    String id = STR."\{15 - lines}";
                    if (lines >= lineCopie.size())
                        player.sidebar.createLine(new Sidebar.ScoreboardLine(id, text, 15 - lines));
                    else {
                        Sidebar.ScoreboardLine line = player.sidebar.getLine(id);
                        assert line != null;
                        if (!line.getContent().equals(text)) player.sidebar.updateLineContent(id, text);
                    }
                    lines++;
                }
            }
            for (int i = lines; i < lineCopie.size(); i++) {
                String id = STR."\{15 - lines}";
                player.sidebar.removeLine(id);
            }
        })).repeat(TaskSchedule.seconds(1)).schedule();
    }

    public double getStat(Stat stat) {
        return getStat(stat, false);
    }

    public double getStat(Stat stat, boolean isBow) {
        double value = getStatModifiers(stat, isBow).calculate();
        if (stat.getMaxValue() > 0 && stat.getMaxValue() < value) value = stat.getMaxValue();
        if (value < 0) value = 0;
        return value;
    }

    public PlayerStatEvent getStatModifiers(Stat stat, boolean isBow) {
        PlayerStatEvent event = new PlayerStatEvent(this, new ArrayList<>(), stat);
        event.modifiers().add(new PlayerStatEvent.BasicModifier("Base Value", stat.getBaseValue(), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Innate));
        for (EquipmentSlot slot : EquipmentSlot.armors()) {
            SbItemStack item = SbItemStack.from(getEquipment(slot));
            if (item == null) continue;
            double value = item.getStat(stat, this);
            if (value == 0) continue;
            boolean canUse = true;
            for (Requirement requirement : item.sbItem().requirements())
                if (!requirement.canUse(this, item.item())) {
                    canUse = false;
                    break;
                }
            if (canUse)
                event.modifiers().add(new PlayerStatEvent.BasicModifier(STR."\{item.displayName()}", value, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Armor));
        }
        for (Pair<PlayerStatEvent.PlayerStatModifier, ?> pair : temporaryModifiers.get(stat))
            event.modifiers().add(pair.getFirst());
        SbItemStack item = SbItemStack.from(getItemInHand(Hand.MAIN));
        if (item != null && (item.sbItem().getType().isStatsInMainhand() || (isBow && item.sbItem() instanceof BowItem))) {
            boolean canUse = true;
            for (Requirement requirement : item.sbItem().requirements())
                if (!requirement.canUse(this, item.item())) {
                    canUse = false;
                    break;
                }
            if (canUse)
                event.modifiers().add(new PlayerStatEvent.BasicModifier(STR."\{item.displayName()}", item.getStat(stat, this), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.ItemHeld));
        }
        MinecraftServer.getGlobalEventHandler().call(event);
        return event;
    }

    public int getFullSetBonusPieceAmount(FullSetBonus bonus) {
        return fullSetBonuses.getOrDefault(bonus, 0);
    }

    public double getMaxSbHealth() {
        double val = getStat(Stat.Health);
        return (val >= Float.MAX_VALUE) ? Float.MAX_VALUE : (float) val;
    }

    public double getManaPool() {
        return getStat(Stat.Intelligence) + 100d;
    }

    public void addSbHealth(double health, boolean ignoreVitality) {
        if (ignoreVitality) {
            setSbHealth(health + getHealth());
            return;
        }
        double vitalityPers = getStat(Stat.Vitality) / 100;
        setSbHealth((getSbHealth() + (health * vitalityPers)));
    }

    public void addSbHealth(double health) {
        addSbHealth(health, false);
    }

    public boolean addItem(ItemStack item) {
        return addItem(SbItemStack.from(item), true);
    }

    public boolean addItem(SbItemStack item) {
        return addItem(item, true);
    }

    public boolean addItem(SbItemStack item, boolean isCollection) {
        if (COIN_ITEMS.contains(item.sbItem().getClass())) {
            if (!item.item().hasTag(Tag.Double("coinamount"))) return true;
            double amount = item.item().getTag(Tag.Double("coinamount"));
            setCoins(getCoins() + amount);
            return true;
        }
        if (!getInventory().addItemStack(item.item())) return false;
        if (isCollection) {
            for (me.carscupcake.sbremake.item.collections.Collection collection : collections) {
                int amount = collection.progress(item.sbItem());
                if (amount > 0) {
                    collection.addProgress(amount * item.item().amount());
                    break;
                }
            }
        }
        return true;
    }

    public void setSbHealth(double health) {
        double newHealth = Math.max(0, Math.min(health, getMaxSbHealth()));
        if (newHealth == getSbHealth()) return;
        sbHealth = newHealth;
        updateHpBar();
    }

    public void updateHpBar() {
        double health = getMaxHealth() * (sbHealth / getMaxSbHealth());
        if (health != getHealth()) setHealth((float) health);
    }

    public void addMana(double value) {
        setMana(value + getMana());
    }

    public void setMana(double value) {
        mana = Math.max(0, Math.min(value, getManaPool()));
    }

    public long attackCooldown() {
        double attackSpeedMultiplier = 1d - (0.5d * (getStat(Stat.AttackSpeed) / 100d));
        return (long) (attackSpeedMultiplier * 500d);
    }

    public void damage(SkyblockEntity entity) {
        EntityMeleeDamagePlayerEvent event = new EntityMeleeDamagePlayerEvent(entity, this);
        MinecraftServer.getGlobalEventHandler().call(event);
        if (event.isCancelled()) return;
        sendPacket(new DamageEventPacket(getEntityId(), 0, entity.getEntityId(), 0, getPosition()));
        setSbHealth(getSbHealth() - event.calculateDamage());
        if (hasKb()) {
            this.takeKnockback(0.4f, Math.sin(entity.getPosition().yaw() * 0.017453292), -Math.cos(entity.getPosition().yaw() * 0.017453292));
        }
    }

    public void damage(SkyblockEntityProjectile entity) {
        ProjectileDamagePlayerEvent event = new ProjectileDamagePlayerEvent(entity, this);
        MinecraftServer.getGlobalEventHandler().call(event);
        if (event.isCancelled()) return;
        sendPacket(new DamageEventPacket(getEntityId(), 0, entity.getEntityId(), 0, getPosition()));
        setSbHealth(getSbHealth() - event.calculateDamage());
        if (hasKb()) {
            this.takeKnockback(0.4f, Math.sin(entity.getPosition().yaw() * 0.017453292), -Math.cos(entity.getPosition().yaw() * 0.017453292));
        }
    }

    public void damage(double damage, double trueDamage) {

    }

    public boolean hasKb() {
        return true;
    }

    public void recalculateArmor() {
        Map<FullSetBonus, Integer> copy = new HashMap<>(fullSetBonuses);
        fullSetBonuses.clear();
        for (EquipmentSlot slot : EquipmentSlot.armors()) {
            SbItemStack stack = SbItemStack.from(getInventory().getItemStack(slot.armorSlot()));
            if (stack == null) continue;
            for (Ability ability : stack.getAbilities(this))
                if (ability instanceof FullSetBonus fullSetBonus)
                    fullSetBonuses.put(fullSetBonus, fullSetBonuses.getOrDefault(fullSetBonus, 0) + 1);
        }
        Set<FullSetBonus> bonuses = new HashSet<>(copy.keySet());
        bonuses.addAll(fullSetBonuses.keySet());
        for (FullSetBonus bonus : bonuses) {
            int old = copy.getOrDefault(bonus, 0);
            int neu = fullSetBonuses.getOrDefault(bonus, 0); // neu (ger. new)
            if (neu == old) continue;
            if (neu < bonus.getMinPieces() && old >= bonus.getMinPieces()) {
                bonus.stop(this);
                continue;
            }
            if (old < bonus.getMinPieces() && neu >= bonus.getMinPieces()) {
                bonus.start(this);
                continue;
            }
            if (neu >= bonus.getMinPieces()) {
                bonus.stop(this);
                bonus.start(this);
            }
        }
        for (EquipmentSlot slot : EquipmentSlot.armors()) {
            SbItemStack stack = SbItemStack.from(getInventory().getItemStack(slot.armorSlot()));
            if (stack == null) continue;
            getInventory().setItemStack(slot.armorSlot(), stack.update(this).item());
        }
    }

    public void playSound(SoundType type, Sound.Source source, float volume, float pitch) {
        playSound(Sound.sound(type.getKey(), source, volume, pitch));
    }

    public void playSound(SoundType type, Sound.Source source, float volume, float pitch, Pos location) {
        playSound(Sound.sound(type.getKey(), source, volume, pitch), location);
    }

    private static float getMaxHearts(double maxHealth) {
        float health = 0;
        if (maxHealth < 125) {
            health = 20;
        } else if (maxHealth < 165) {
            health = 22;
        } else if (maxHealth < 230) {
            health = 24;
        } else if (maxHealth < 300) {
            health = 26;
        } else if (maxHealth < 400) {
            health = 28;
        } else if (maxHealth < 500) {
            health = 30;
        } else if (maxHealth < 650) {
            health = 32;
        } else if (maxHealth < 800) {
            health = 34;
        } else if (maxHealth < 1000) {
            health = 36;
        } else if (maxHealth < 1250) {
            health = 38;
        } else if (maxHealth >= 1250) {
            health = 40;
        }
        return health;
    }

    public record DisguisedChatMessage(Component message, ChatType chatType, Component senderName,
                                       boolean hasTargetName,
                                       @Nullable Component targetname) implements ServerPacket.Play, ServerPacket.ComponentHolding {
        public DisguisedChatMessage(String message) {
            this(Component.text(message), ChatType.Chat, Component.text(""));
        }

        public DisguisedChatMessage(Component message, ChatType chatType, Component senderName) {
            this(message, chatType, senderName, false, null);
        }

        @Override
        public int playId() {
            return ServerPacketIdentifier.DISGUISED_CHAT;
        }

        @Override
        public void write(@NotNull NetworkBuffer networkBuffer) {
            networkBuffer.write(NetworkBuffer.COMPONENT, message);
            networkBuffer.write(NetworkBuffer.VAR_INT, chatType.id);
            networkBuffer.write(NetworkBuffer.COMPONENT, senderName);
            networkBuffer.write(NetworkBuffer.BOOLEAN, hasTargetName);
            if (hasTargetName) {
                assert targetname != null;
                networkBuffer.write(NetworkBuffer.COMPONENT, targetname);
            }

        }

        @Override
        @Unmodifiable
        public @NotNull Collection<Component> components() {
            final ArrayList<Component> list = new ArrayList<>();
            list.add(message);
            list.add(senderName);
            if (targetname != null) list.add(targetname);
            return List.copyOf(list);
        }

        @Override
        public @NotNull ServerPacket copyWithOperator(@NotNull UnaryOperator<Component> unaryOperator) {
            return new DisguisedChatMessage(unaryOperator.apply(message), chatType, unaryOperator.apply(senderName), hasTargetName, unaryOperator.apply(targetname));
        }

        public enum ChatType {
            Chat(7), MsgIn(3), MsgOut(4), TeamMessageIn(6), TeamMessageOut(5), SayCommand(1), Narration(0);
            private final int id;

            ChatType(int id) {
                this.id = id;
            }
        }
    }

    public record SystemMessagePackage(Component message, boolean actionbar) implements ServerPacket.Play {
        public SystemMessagePackage(String message, boolean actionbar) {
            this(Component.text(message), actionbar);
        }

        @Override
        public int playId() {
            return ServerPacketIdentifier.SYSTEM_CHAT;
        }

        @Override
        public void write(@NotNull NetworkBuffer networkBuffer) {
            networkBuffer.write(NetworkBuffer.COMPONENT, message);
            networkBuffer.write(NetworkBuffer.BOOLEAN, actionbar);
        }
    }
}
