package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmor;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorAbilityCounter;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.CountMap;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.entity.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;

import static me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorCommons.calculateTier;

@Getter
public class DominusAbility extends FullSetBonus {
    public static final DominusAbility INSTANCE = new DominusAbility();

    public DominusAbility() {
        super("Dominus", 4, 2, true);
    }

    public static final Map<SkyblockPlayer, KuudraArmorAbilityCounter> task = new HashMap<>();

    @Override
    public void start(SkyblockPlayer player) {
        var pieces = player.getFullSetBonusPieceAmount(this);
        var resetTime = 20 * switch (pieces) {
            case 3 -> 7;
            case 4 -> 10;
            default -> 4;
        };
        var cooldownTicks = switch (pieces) {
            case 3 -> 30;
            case 4 -> 20;
            default -> 10;
        };
        task.put(player, new KuudraArmorAbilityCounter(player, resetTime, cooldownTicks, calculateTier(player, KuudraArmorType.Crimson)));
    }

    @Override
    public void stop(SkyblockPlayer player) {
        task.remove(player).cancel();
    }

    @Override
    public Lore lore() {
        return new Lore("§7For every melee kill gain §c1§7 stack of §6Dominus ᝐ§7.\n \nEach §6Dominus ᝐ stack grants %b%\n \n§7At §c10§7 stacks also §bswipe§7 in a random direction hitting every enemy in the path of the swipe.\n §8\n§7Lose 1 stack after §c%s%s§7 of not gaining a stack.",
                Map.of("%s%", (ignored, player) -> player == null ? "4" :
                                switch (player.getFullSetBonusPieceAmount(this)) {
                                    case 3 -> "7";
                                    case 4 -> "10";
                                    default -> "4";
                                },
                        "%b%", (s, ignored) -> switch (((KuudraArmor) s.sbItem()).armorTier()) {
                            case Base -> "§e+0.05 " + (Stat.SwingRange);
                            case Hot -> "§e+0.1 " + (Stat.SwingRange);
                            case Burning -> "§e+0.1 " + (Stat.SwingRange) + " §7and §c+1 " + (Stat.Ferocity);
                            case Fiery -> "§e+0.1 " + (Stat.SwingRange) + " §7and §c+2 " + (Stat.Ferocity);
                            case Infernal ->
                                    "§e+0.2 " + (Stat.SwingRange) + "§7, §c+2 " + (Stat.Ferocity) + " §7and §c10% " + (Stat.Strength.getSymbol()) + " " + (Stat.Damage);
                        }));
    }

    public static void applyStatBonus(SkyblockPlayer player, KuudraArmorTier tier, PlayerStatEvent event) {
        int stacks = task.get(player).getStacks();
        if (stacks == 0) return;
        switch (tier) {
            case Base -> {
                if (event.stat() == Stat.SwingRange) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.05, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                }
            }
            case Hot -> {
                if (event.stat() == Stat.SwingRange) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.1, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                }
            }
            case Burning -> {
                if (event.stat() == Stat.SwingRange) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.1, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                } else if (event.stat() == Stat.Ferocity) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                }
            }
            case Fiery -> {
                if (event.stat() == Stat.SwingRange) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.1, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                } else if (event.stat() == Stat.Ferocity) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 2, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                }
            }
            case Infernal -> {
                if (event.stat() == Stat.SwingRange) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.2, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                } else if (event.stat() == Stat.Ferocity) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 2, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                } else if (event.stat() == Stat.Damage) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.1, PlayerStatEvent.Type.AddativeMultiplier, PlayerStatEvent.StatsCategory.Ability));
                }
            }
        }
    }
}
