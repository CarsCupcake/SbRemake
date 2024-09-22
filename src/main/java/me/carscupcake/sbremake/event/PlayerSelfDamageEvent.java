package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.event.trait.CancellableEvent;

import java.time.Duration;

@Setter
@Getter
public class PlayerSelfDamageEvent implements CancellableEvent {
    private final SkyblockPlayer player;
    private double normalDamage;
    private double trueDamage;
    private boolean cancelled = false;
    public PlayerSelfDamageEvent(SkyblockPlayer player, double damage, double trueDamage) {
        this.player = player;
        normalDamage = damage;
        this.trueDamage = trueDamage;
    }
    private double cachedDamage;
    public double calculateDamage() {
        double defense = player.getStat(Stat.Defense);
        double trueDefense = player.getStat(Stat.TrueDefense);
        double damageReduction = defense / (defense + 100d);
        double trueDamageReduction = trueDefense / (trueDefense + 100);
        cachedDamage = (normalDamage * (1d - damageReduction)) + (trueDamage * (1d - trueDamageReduction));
        return cachedDamage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public void spawnDamageTag() {
        LivingEntity e = new LivingEntity(EntityType.ARMOR_STAND);
        e.setCustomName(Component.text(STR."§7\{StringUtils.cleanDouble(cachedDamage, 0)}"));
        e.setCustomNameVisible(true);
        e.setInvisible(true);
        e.setNoGravity(true);
        e.setInvulnerable(true);
        ArmorStandMeta meta = (ArmorStandMeta) e.getEntityMeta();
        meta.setHasNoBasePlate(true);
        meta.setMarker(true);
        e.setInstance(player.getInstance(), player.getPosition().add(0, player.getEyeHeight() / 2, 0));
        e.scheduleRemove(Duration.ofSeconds(2));
    }
}
