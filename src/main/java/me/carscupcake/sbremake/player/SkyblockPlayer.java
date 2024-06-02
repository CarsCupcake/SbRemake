package me.carscupcake.sbremake.player;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.HealthRegenEvent;
import me.carscupcake.sbremake.event.ManaRegenEvent;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.ActionBarPacket;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class SkyblockPlayer extends Player {
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
        regenTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            Audiences.players().forEachAudience(audience -> {
                SkyblockPlayer player = (SkyblockPlayer) audience;
                double healthGained = (1.5 + player.getMaxSbHealth()/100d) * player.getStat(Stat.HealthRegen)/100;
                HealthRegenEvent event = new HealthRegenEvent(player, healthGained);
                MinecraftServer.getGlobalEventHandler().call(event);
                player.addSbHealth((float) event.getRegenAmount());
                double maxHealth = getMaxHearts(player.getMaxSbHealth());
                if (maxHealth != player.getMaxHealth())
                    player.getAttribute(Attribute.MAX_HEALTH).setBaseValue((float) maxHealth);

                double manaGained = player.mana * 0.02;
                ManaRegenEvent e = new ManaRegenEvent(player, manaGained);
                MinecraftServer.getGlobalEventHandler().call(e);
                player.setMana(e.getRegenAmount() * e.getMultiplier());

                ActionBarPacket packet = new ActionBarPacket(Component.text(player.actionBar.build()));
                player.sendPacket(packet);
            });
        }).repeat(TaskSchedule.seconds(2)).schedule();
    }

    public double getStat(Stat stat) {
        //TODO implement
        double value = stat.getBaseValue();
        for (EquipmentSlot slot : EquipmentSlot.armors()) {
            SbItemStack item = SbItemStack.from(getEquipment(slot));
            if (item == null) continue;
            value += item.getStat(stat);
        }
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
        sbHealth = health;
        updateHpBar();
    }

    private void updateHpBar() {
        double health = getMaxHealth() * (sbHealth/getMaxSbHealth());
        if (health != getHealth()) setHealth((float) health);
    }

    public void setMana(double value) {
        mana = Math.max(0, Math.min(value, getManaPool()));
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
