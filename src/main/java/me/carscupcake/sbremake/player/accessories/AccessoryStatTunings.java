package me.carscupcake.sbremake.player.accessories;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;

@Getter
public enum AccessoryStatTunings {
    Health(Stat.Health, 5),
    Defense(Stat.Defense, 1),
    Speed(Stat.Speed, 1.5),
    Strength(Stat.Strength, 1),
    CritDamage(Stat.CritDamage, 1),
    CritChance(Stat.CritChance, 0.2),
    AttackSpeed(Stat.AttackSpeed, 0.3),
    Intelligence(Stat.Intelligence, 1);
    private final Stat stat;
    private final double perPoint;
    AccessoryStatTunings(Stat stat, double perPoint) {
        this.stat = stat;
        this.perPoint = perPoint;
    }
}
