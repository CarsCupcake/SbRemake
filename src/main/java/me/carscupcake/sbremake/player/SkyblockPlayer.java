package me.carscupcake.sbremake.player;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.event.HealthRegenEvent;
import me.carscupcake.sbremake.event.ManaRegenEvent;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.*;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.client.play.ClientAnimationPacket;
import net.minestom.server.network.packet.server.play.ActionBarPacket;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

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
                if (event.getPacket() instanceof ClientAnimationPacket packet) {
                    if (packet.hand() != Hand.MAIN) return;
                    SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
                    long time = System.currentTimeMillis();
                    long delta = time - player.lastAttack;
                    if (delta < player.attackCooldown()) return;
                    Entity result = player.getLineOfSightEntity(player.getStat(Stat.SwingRange), entity -> entity.getEntityType() != EntityType.PLAYER && entity instanceof LivingEntity);
                    if (result == null) return;
                    if (!(result instanceof SkyblockEntity entity)) return;
                    if (entity.getEntityType() == EntityType.PLAYER) return;
                    player.lastAttack = time;
                    entity.damage(player);
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
