package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Getter
@Setter
public class PlayerToEntityMageDamage implements PlayerEvent, CancellableEvent {
    public String damageTagPrefix = "§7";
    public String damageTagSuffix = "";
    private double additiveMultiplier = 1;
    private double multiplicativeMultiplier = 1;
    private double bonusModifier = 0;
    private boolean cancelled = false;
    private final SkyblockPlayer player;
    private final SkyblockEntity target;
    private double baseAbilityDamage;
    private double intelligence;
    private double abilityScaling;

    public PlayerToEntityMageDamage(SkyblockPlayer player, SkyblockEntity target, double baseAbilityDamage, double intelligence, double abilityScaling) {
        this.player = player;
        this.target = target;
        this.baseAbilityDamage = baseAbilityDamage;
        this.intelligence = intelligence;
        this.abilityScaling = abilityScaling;
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

    public double calculateHit() {
        return baseAbilityDamage * (1 + (intelligence / 100) * abilityScaling) * additiveMultiplier * multiplicativeMultiplier + bonusModifier;
    }

    private static final String[] prefixes = {"f§", "e§", "6§", "c§", "f§"};

    public String getDamageTag() {
        return damageTagPrefix + StringUtils.toFormatedNumber((int) calculateHit()) +
                damageTagSuffix;
    }
}
