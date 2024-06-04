package me.carscupcake.sbremake.player;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.event.HealthRegenEvent;
import me.carscupcake.sbremake.event.ManaRegenEvent;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.ServerFlag;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.projectile.ArrowMeta;
import net.minestom.server.entity.metadata.projectile.ProjectileMeta;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.EntityFireEvent;
import net.minestom.server.event.entity.EntityShootEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.*;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

@Getter
public class SkyblockPlayer extends Player {
    private static final EventNode<Event> PLAYER_NODE = EventNode.all("player.events")
            .addListener(EntityAttackEvent.class, event -> {
                final Entity source = event.getEntity();
                final Entity entity = event.getTarget();

                if (entity instanceof SkyblockPlayer target) {
                    if (source.getEntityType() == EntityType.PLAYER) return;
                    return;
                }
            })
            .addListener(PlayerPacketEvent.class, event -> {
                SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
                if (event.getPacket() instanceof ClientAnimationPacket packet) {
                    if (packet.hand() != Hand.MAIN) return;
                    long time = System.currentTimeMillis();
                    long delta = time - player.lastAttack;
                    if (delta < player.attackCooldown()) return;
                    Entity result = player.getLineOfSightEntity(player.getStat(Stat.SwingRange), entity -> entity.getEntityType() != EntityType.PLAYER && entity instanceof LivingEntity);
                    if (result == null) return;
                    if (!(result instanceof SkyblockEntity entity)) return;
                    if (entity.getEntityType() == EntityType.PLAYER) return;
                    player.lastAttack = time;
                    entity.damage(player);
                    return;
                }
                if (event.getPacket() instanceof ClientHeldItemChangePacket) {
                    player.bowStartPull = -1;
                }
                if (event.getPacket() instanceof ClientPlayerDiggingPacket packet) {
                    if (packet.status() == ClientPlayerDiggingPacket.Status.UPDATE_ITEM_STATE) {
                        if (player.bowStartPull < 0) return;
                        if (player.getItemInHand(Hand.MAIN).material() != Material.BOW) return;
                        long chargingTime = System.currentTimeMillis() - player.bowStartPull;
                        double chargingSeconds = Math.min(1d, chargingTime / 1000d);
                        player.bowStartPull = -1;
                        Instance i = player.getInstance();
                        EntityProjectile projectile = new EntityProjectile(player, EntityType.ARROW) {
                            @Override
                            public void tick(long L) {
                                if (this.instance == null) {
                                    if (!this.isRemoved()) this.remove();
                                    return;
                                }
                                super.tick(L);
                            }
                        };
                        var pos = player.getPosition().add(0D, player.getEyeHeight(), 0D);
                        if (i == null) {
                            System.out.println("HUH");
                            return;
                        }
                        projectile.setInstance(i, pos);
                        projectile.setVelocity(player.getPosition().direction().normalize().mul(60 * chargingSeconds));
                        projectile.setView((float)Math.toDegrees(Math.atan2(projectile.getVelocity().x(), projectile.getVelocity().z())), (float)Math.toDegrees(Math.atan2(projectile.getVelocity().y(), Math.sqrt(projectile.getVelocity().x() * projectile.getVelocity().x() + projectile.getVelocity().z() * projectile.getVelocity().z()))));
                        projectile.setNoGravity(false);
                        final boolean crits = chargingSeconds >= 1;
                        projectile.scheduler().buildTask(() -> {
                            if (projectile.isRemoved() || projectile.getInstance() == null || projectile.getVelocity() == Vec.ZERO) {
                                return;
                            }
                            for (Player pl : projectile.getInstance().getPlayers().stream().filter(player1 -> player1.getDistance(projectile) < 16d * 8d).toList()) {
                                pl.sendPacket(new EntityVelocityPacket(projectile.getEntityId(), projectile.getVelocity().mul((double)(8000.0F / (float) ServerFlag.SERVER_TICKS_PER_SECOND))));
                            }
                            if(crits) ParticleUtils.spawnParticle(projectile.getInstance(), projectile.getPosition(), Particle.CRIT, 1);
                        }).repeat(Duration.ofMillis(50)).schedule();
                    }
                    return;
                }
                if (event.getPacket() instanceof ClientUseItemPacket packet) {
                    if (packet.hand() != Hand.MAIN) return;
                    if (player.getItemInHand(Hand.MAIN).material() == Material.BOW) {
                        player.bowStartPull = System.currentTimeMillis();
                    }
                }

            })
            .addListener(ProjectileCollideWithBlockEvent.class, event -> {
                event.getEntity().remove();
                event.setCancelled(true);
            })
            .addListener(ProjectileCollideWithEntityEvent.class, event -> {
                Entity shooter = ((ProjectileMeta) event.getEntity().getEntityMeta()).getShooter();
                Entity target = event.getTarget();
                event.getEntity().remove();

                if (shooter instanceof SkyblockPlayer &&  target instanceof SkyblockPlayer) return;

                if (shooter instanceof SkyblockPlayer player) {
                    //TODO Player to Entity Projectile Hit
                    return;
                }

                if (target instanceof SkyblockPlayer player) {
                    //TODO Entity to Player Projectile Hit
                    return;
                }

            })
            .addListener(PickupItemEvent.class, event -> {
                final Entity entity = event.getLivingEntity();
                if (entity instanceof SkyblockPlayer player) {
                    // Cancel event if player does not have enough inventory space
                    final ItemStack itemStack = event.getItemEntity().getItemStack();
                    SbItemStack sbItemStack = SbItemStack.from(itemStack);
                    event.setCancelled(!player.getInventory().addItemStack(sbItemStack.update().item()));
                }
            });
    public static Task regenTask;
    private SkyblockWorld.WorldProvider worldProvider = null;
    @Setter
    public UpdateHealthPacket lastHealthPacket = null;
    @Getter
    public double mana = 100;
    private final ActionBar actionBar = new ActionBar(this);
    private boolean login = true;

    @Getter
    private double sbHealth;


    @Getter
    private long bowStartPull = -1;

    private long lastAttack;

    public SkyblockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
        login = false;
        sbHealth = getMaxSbHealth();
    }

    /**
     * This is to setup stuff, when the player gets spawned (respawn or server join)
     */
    public void spawn() {
        super.spawn();
        setHealth(getMaxHealth());
        teleport(worldProvider.spawn());
    }

    public void setWorldProvider(SkyblockWorld.WorldProvider provider) {
        if (worldProvider != null && provider != worldProvider) {
            provider.addPlayer(this);
        }
        this.worldProvider = provider;
    }

    public static void statsLoop() {
        MinecraftServer.getGlobalEventHandler().addChild(PLAYER_NODE);
        regenTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            Audiences.players().forEachAudience(audience -> {
                SkyblockPlayer player = (SkyblockPlayer) audience;
                double maxSbHp = player.getMaxSbHealth();
                if (player.getSbHealth() < maxSbHp) {
                    double healthGained = (1.5 + maxSbHp / 100d) * player.getStat(Stat.HealthRegen) / 100;
                    HealthRegenEvent event = new HealthRegenEvent(player, healthGained);
                    MinecraftServer.getGlobalEventHandler().call(event);
                    player.addSbHealth((float) event.getRegenAmount());
                }
                double maxHealth = getMaxHearts(player.getMaxSbHealth());
                if (maxHealth != player.getMaxHealth())
                    player.getAttribute(Attribute.MAX_HEALTH).setBaseValue((float) maxHealth);

                double maxManaPool = player.getManaPool();
                if (maxManaPool > player.getMana()) {
                    double manaGained = maxManaPool * 0.02d;
                    ManaRegenEvent e = new ManaRegenEvent(player, manaGained);
                    MinecraftServer.getGlobalEventHandler().call(e);
                    player.addMana(e.getRegenAmount() * e.getMultiplier());
                }

                ActionBarPacket packet = new ActionBarPacket(Component.text(player.actionBar.build()));
                player.sendPacket(packet);
            });
        }).repeat(TaskSchedule.seconds(2)).schedule();
    }

    public double getStat(Stat stat) {
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
        if (item != null) {
            event.modifiers().add(new PlayerStatEvent.BasicModifier(STR."\{stat.getPrefix()}\{stat.getSymbol()} \{item.displayName()}", item.getStat(stat), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.ItemHeld));
        }
        MinecraftServer.getGlobalEventHandler().call(event);
        double value = event.calculate();
        if (stat.getMaxValue() > 0 && stat.getMaxValue() < value) value = stat.getMaxValue();
        if (value < 0) value = 0;
        return value;
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
        double health = getMaxHealth() * (sbHealth/getMaxSbHealth());
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

    public void damage(LivingEntity entity) {

    }

    public void damage(double damage, double trueDamage) {

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
}
