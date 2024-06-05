package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.SkyblockPlayerArrow;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Getter
@Setter
public class PlayerProjectileDamageEntityEvent implements PlayerEvent, CancellableEvent {
    private final SkyblockPlayer player;
    private final SkyblockEntity target;
    private final SkyblockPlayerArrow projectile;
    private double additiveMultiplier = 1;
    private double multiplicativeMultiplier = 1;
    private double bonusModifier = 0;
    private final boolean crit;
    private boolean cancelled = false;
    public String damageTagPrefix;
    public String damageTagSuffix;

    public PlayerProjectileDamageEntityEvent(SkyblockEntity target, SkyblockPlayerArrow projectile) {
        this.player = (SkyblockPlayer) projectile.getShooter();
        this.target = target;
        this.projectile = projectile;
        crit = new Random().nextDouble() <= (projectile.getCritChance() / 100d) && projectile.isCanCrit();
        if (crit) {
            damageTagPrefix = "§f✧";
            damageTagSuffix = "✧";
        } else {
            damageTagPrefix = "§7";
            damageTagSuffix = "";
        }
    }

    public void addMultiplicativeMultiplier(double d) {
        multiplicativeMultiplier *= d;
    }

    public double calculateCritHit() {
        return (5 + projectile.getWeaponDamage()) * (1+(projectile.getStrength()/100)) * (1+(projectile.getCritDamage()/100)) * additiveMultiplier * multiplicativeMultiplier + bonusModifier;
    }

    public double calculateHit() {
        return (5 + projectile.getWeaponDamage()) * (1+(projectile.getStrength()/100)) * additiveMultiplier * multiplicativeMultiplier + bonusModifier;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    private static final String[] prefixes = {"f§", "e§", "6§", "c§", "f§"};

    public String getDamageTag() {
        StringBuilder builder = new StringBuilder(damageTagPrefix);
        if (crit) {
            int prefixCounter = 0;
            String str = StringUtils.cleanDouble(calculateHit(), 0);
            StringBuilder sb = new StringBuilder(str);
            sb.reverse();
            str = sb.toString();
            StringBuilder newString = new StringBuilder();
            int digitRunner = 0;
            for (int i = 0; i < str.toCharArray().length; i++) {
                newString.append(str.toCharArray()[i]);
                digitRunner++;
                if (digitRunner == 3 && (i + 1) != str.toCharArray().length) {
                    digitRunner = 0;
                    newString.append(",");
                }
                newString.append(prefixes[prefixCounter]);
                prefixCounter++;
            }
            builder.append(newString.reverse());
            builder.append(new StringBuilder(prefixes[prefixCounter]).reverse());
        } else {
            builder.append(StringUtils.toFormatedNumber((int) calculateHit()));
        }
        return builder.append(damageTagSuffix).toString();
    }
}
