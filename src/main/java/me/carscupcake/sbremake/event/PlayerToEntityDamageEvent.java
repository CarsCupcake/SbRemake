package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class PlayerToEntityDamageEvent implements PlayerEvent, CancellableEvent {
    public String damageTagPrefix;
    public String damageTagSuffix;
    private double additiveMultiplier = 1;
    private double multiplicativeMultiplier = 1;
    private double bonusModifier = 0;
    private final boolean crit;
    private boolean cancelled = false;
    private final SkyblockPlayer player;
    private final SkyblockEntity target;
    private double weaponDamage;
    private double strength;
    private double critDamage;
    private double critChance;
    private double ferocity;
    private boolean canDoFerocity = true;
    private final Set<Consumer<PlayerToEntityDamageEvent>> postEvent = new HashSet<>();

    public PlayerToEntityDamageEvent(SkyblockPlayer player, SkyblockEntity target, double weaponDamage, double strength, double critDamage, double critChance, double ferocity) {
        this.player = player;
        this.target = target;
        this.weaponDamage = weaponDamage;
        this.strength = strength;
        this.critDamage = critDamage;
        this.critChance = critChance;
        this.ferocity = ferocity;
        crit = new Random().nextDouble() <= (critChance / 100d);
        if (crit) {
            damageTagPrefix = "§f✧";
            damageTagSuffix = "✧";
        } else {
            damageTagPrefix = "§7";
            damageTagSuffix = "";
        }
    }

    @NotNull
    @Override
    public SkyblockPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void addMultiplicativeMultiplier(double d) {
        multiplicativeMultiplier *= d;
    }
    public void addAdditiveMultiplier(double d) {
        additiveMultiplier += d;
    }

    public double calculateCritHit() {
        return (5 + weaponDamage) * (1 + (strength / 100)) * (1 + (critDamage / 100)) * additiveMultiplier * multiplicativeMultiplier + bonusModifier;
    }

    public double calculateHit() {
        return (5 + weaponDamage) * (1 + (strength / 100)) * additiveMultiplier * multiplicativeMultiplier + bonusModifier;
    }

    public Pos damagerPos() {
        return player.getPosition();
    }

    private static final String[] prefixes = {"f§", "e§", "6§", "c§", "f§"};

    public String getDamageTag() {
        StringBuilder builder = new StringBuilder(damageTagPrefix);
        if (crit) {
            int prefixCounter = 0;
            String str = StringUtils.cleanDouble(calculateCritHit(), 0);
            StringBuilder sb = new StringBuilder(str);
            sb.reverse();
            str = sb.toString();
            StringBuilder newString = new StringBuilder();
            int digitRunner = 0;
            for (int i = 0; i < str.length(); i++) {
                newString.append(str.toCharArray()[i]);
                digitRunner++;
                if (digitRunner == 3 && (i + 1) != str.length()) {
                    digitRunner = 0;
                    newString.append(",");
                }
                newString.append(prefixes[prefixCounter]);
                prefixCounter++;
                if (prefixCounter >= prefixes.length) prefixCounter = 0;
            }
            builder.append(newString.reverse());
            builder.append(new StringBuilder(prefixes[prefixCounter]).reverse());
        } else {
            builder.append(StringUtils.toFormatedNumber((int) calculateHit()));
        }
        return builder.append(damageTagSuffix).toString();
    }

    public abstract EntityDeathEvent.Type damageType();
}
