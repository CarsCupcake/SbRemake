package me.carscupcake.sbremake.player;

import kotlin.jvm.internal.Reflection;
import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.blocks.Mining;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.SkyblockEntityProjectile;
import me.carscupcake.sbremake.event.*;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.item.impl.arrows.SkyblockArrow;
import me.carscupcake.sbremake.item.impl.bow.BowItem;
import me.carscupcake.sbremake.item.impl.bow.Shortbow;
import me.carscupcake.sbremake.player.protocol.SetEntityEffectPacket;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.metadata.projectile.ProjectileMeta;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.EntityPotionAddEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.client.play.*;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.ServerPacketIdentifier;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Getter
@SuppressWarnings({"unused", "preview", "UnstableApiUsage"})
public class SkyblockPlayer extends Player {
    private static final UUID speedUUID = UUID.randomUUID();
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
                if (delta >= player.attackCooldown()) {
                    Entity result = player.getLineOfSightEntity(player.getStat(Stat.SwingRange), entity -> entity.getEntityType() != EntityType.PLAYER && entity instanceof LivingEntity);
                    if (result instanceof SkyblockEntity entity && entity.getEntityType() != EntityType.PLAYER) {
                        player.lastAttack = time;
                        entity.damage(player);
                        return;
                    }
                }
                SbItemStack item = SbItemStack.from(player.getItemInHand(Hand.MAIN));
                if (item == null) return;
                if (item.sbItem() instanceof Shortbow shortbow && player.shortbowTask == null) {
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
                SkyblockPlayerArrow.shootBow(player, chargingTime, item, (SkyblockArrow) SbItemStack.base(Material.ARROW).sbItem());
            }
            return;
        }
        if (event.getPacket() instanceof ClientUseItemPacket packet) {
            if (packet.hand() != Hand.MAIN) return;
            if (player.getItemInHand(Hand.MAIN).material() == Material.BOW) {
                SbItemStack item = SbItemStack.from(player.getItemInHand(Hand.MAIN));
                if (item.sbItem() instanceof Shortbow shortbow) {
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
            MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player));
            return;
        }

        if (event.getPacket() instanceof ClientPlayerBlockPlacementPacket packet) {
            if (packet.hand() != Hand.MAIN) return;
            long now = System.currentTimeMillis();
            if (now - player.lastInteractPacket < 100) return;
            player.lastInteractPacket = now;
            MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, packet.blockPosition(), packet.blockFace()));
        }

        if (event.getPacket() instanceof ClientInteractEntityPacket packet) {
            if (packet.type() instanceof ClientInteractEntityPacket.Attack) return;
            long now = System.currentTimeMillis();
            if (now - player.lastInteractPacket < 100) return;
            player.lastInteractPacket = now;
            MinecraftServer.getGlobalEventHandler().call(new PlayerInteractEvent(player, player.getInstance().getEntities().stream().filter(entity -> entity.getEntityId() == packet.targetId()).findFirst().orElse(null)));
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
            event.setCancelled(!player.getInventory().addItemStack(sbItemStack.update(player).item()));
        }
    }).addListener(InventoryPreClickEvent.class, event -> {
        if (event.getInventory() != null) return;
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
    private String lastAbility = null;
    private int lastAbilityTicks = 0;
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

    public SkyblockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
        sbHealth = getMaxSbHealth();
        setNoGravity(true);
    }

    public void setLastAbility(String s) {
        lastAbility = s;
        lastAbilityTicks = 2;
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

    /**
     * This is to set up stuff, when the player gets spawned (respawn or server join)
     */

    private int spawnTeleportId = 0;

    public void spawn() {
        super.spawn();
        setHealth(getMaxHealth());
        Pos spawn = worldProvider.spawn();
        instance.loadChunk(spawn.chunkX(), spawn.chunkZ());
        setNoGravity(true);
        spawnTeleportId = getNextTeleportId();
        PlayerPositionAndLookPacket packet = new PlayerPositionAndLookPacket(spawn, (byte) 0, spawnTeleportId);
        sendPacket(packet);
        sendPacket(new ClearTitlesPacket(true));
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
            this.worldProvider = provider;
            provider.addPlayer(this);
        }
        this.worldProvider = provider;
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
                if (maxHealth != player.getMaxHealth())
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((float) maxHealth);
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
            if (player.lastAbility != null) {
                player.lastAbilityTicks--;
                if (player.lastAbilityTicks == 0) player.lastAbility = null;
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
        PlayerStatEvent event = new PlayerStatEvent(this, new ArrayList<>(), stat);
        event.modifiers().add(new PlayerStatEvent.BasicModifier("§aBase Value", stat.getBaseValue(), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Innate));
        for (EquipmentSlot slot : EquipmentSlot.armors()) {
            SbItemStack item = SbItemStack.from(getEquipment(slot));
            if (item == null) continue;
            double value = item.getStat(stat);
            if (value == 0) continue;
            event.modifiers().add(new PlayerStatEvent.BasicModifier(STR."\{stat.getPrefix()}\{stat.getSymbol()} \{item.displayName()}", value, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Armor));
        }
        SbItemStack item = SbItemStack.from(getItemInHand(Hand.MAIN));
        if (item != null && (item.sbItem().getType().isStatsInMainhand() || (isBow && item.sbItem() instanceof BowItem))) {
            event.modifiers().add(new PlayerStatEvent.BasicModifier(STR."\{stat.getPrefix()}\{stat.getSymbol()} \{item.displayName()}", item.getStat(stat), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.ItemHeld));
        }
        MinecraftServer.getGlobalEventHandler().call(event);
        double value = event.calculate();
        if (stat.getMaxValue() > 0 && stat.getMaxValue() < value) value = stat.getMaxValue();
        if (value < 0) value = 0;
        return value;
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

    public void setSbHealth(double health) {
        double newHealth = Math.max(0, Math.min(health, getMaxSbHealth()));
        if (newHealth == getSbHealth()) return;
        sbHealth = newHealth;
        updateHpBar();
    }

    private void updateHpBar() {
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
            for (Ability ability : stack.getAbilities())
                if (ability instanceof FullSetBonus fullSetBonus)
                    fullSetBonuses.put(fullSetBonus, fullSetBonuses.getOrDefault(fullSetBonus, 0) + 1);
        }
        Set<FullSetBonus> bonuses = new HashSet<>(copy.keySet());
        bonuses.addAll(fullSetBonuses.keySet());
        for (FullSetBonus bonus : bonuses) {
            int old = copy.getOrDefault(bonus, 0);
            int neu = fullSetBonuses.getOrDefault(bonus, 0);
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
