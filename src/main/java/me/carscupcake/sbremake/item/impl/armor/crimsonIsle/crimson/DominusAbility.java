package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmor;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.CountMap;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.entity.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public class DominusAbility extends FullSetBonus {
    public static final DominusAbility INSTANCE = new DominusAbility();
    public static final CountMap<SkyblockPlayer> DOMINUS_STACKS = new CountMap<>();
    public DominusAbility() {
        super("Dominus", 4, 2, true);
    }
    public static final Map<SkyblockPlayer, DominusCounter> task = new HashMap<>();
    @Override
    public void start(SkyblockPlayer player) {
        DOMINUS_STACKS.put(player, 0);
        task.put(player, new DominusCounter(player, player.getFullSetBonusPieceAmount(this), calculateTier(player)));
    }

    @Override
    public void stop(SkyblockPlayer player) {
        task.remove(player).cancel();
        DOMINUS_STACKS.remove(player);
    }

    @Getter
    public static class DominusCounter extends TaskScheduler {
        private final SkyblockPlayer player;
        private final int resetTime;
        int i;
        public int cooldown = 10;
        private final KuudraArmorTier tier;
        private final int cooldownTicks;
        private long lastGain = System.currentTimeMillis();
        public DominusCounter(SkyblockPlayer player, int pieces, KuudraArmorTier tier) {
            this.resetTime = 20 * switch (pieces) {
                case 3 -> 7;
                case 4 -> 10;
                default -> 4;
            };
            this.cooldownTicks = switch (pieces) {
                case 3 -> 30;
                case 4 -> 20;
                default -> 10;
            };
            this.tier = tier;
            i = resetTime;
            this.player = player;
            repeatTask(1, 1);
        }

        @Override
        public void run() {
            if (cooldown != 0) cooldown--;
            if (i == 0) {
                i = resetTime;
                if (DOMINUS_STACKS.get(player) > 0) DOMINUS_STACKS.subtract(player, 1);
            } else i--;
        }

        public void add() {
            if (System.currentTimeMillis() - lastGain > cooldownTicks * 50L) {
                lastGain = System.currentTimeMillis();
                if (DOMINUS_STACKS.getOrDefault(player, 0) != 10)
                    DOMINUS_STACKS.add(player, 1);
                i = resetTime;
            }
        }
    }

    @Override
    public Lore lore() {
        return new Lore(STR."§7For every melee kill gain §c1§7 stack of §6Dominus ᝐ§7.\n \nEach §6Dominus ᝐ stack grants %b%\n \n§7At §c10§7 stacks also §bswipe§7 in a random direction hitting every enemy in the path of the swipe.\n §8\n§7Lose 1 stack after §c%s%s§7 of not gaining a stack.",
                Map.of("%s%", (_, player) -> player == null ? "4" :
                switch (player.getFullSetBonusPieceAmount(this)) {
                    case 3 -> "7";
                    case 4 -> "10";
                    default -> "4";
                },
                        "%b%", (s, _) -> switch (((KuudraArmor) s.sbItem()).armorTier()) {
                            case Base -> STR."§e+0.05 \{Stat.SwingRange}";
                            case Hot -> STR."§e+0.1 \{Stat.SwingRange}";
                            case Burning -> STR."§e+0.1 \{Stat.SwingRange} §7and §c+1 \{Stat.Ferocity}";
                            case Fiery -> STR."§e+0.1 \{Stat.SwingRange} §7and §c+2 \{Stat.Ferocity}";
                            case Infernal -> STR."§e+0.2 \{Stat.SwingRange}§7, §c+2 \{Stat.Ferocity} \n§7and §c10% \{Stat.Strength.getSymbol()} \{Stat.Damage}";
                        }));
    }

    private static KuudraArmorTier calculateTier(SkyblockPlayer player) {
        KuudraArmorTier tier = null;
        for (var slot : EquipmentSlot.armors()) {
            if (SbItemStack.from(player.getEquipment(slot)).sbItem() instanceof KuudraArmor kuudraArmor) {
                if (kuudraArmor.armorType() != KuudraArmorType.Crimson) continue;
                if (tier == null) tier = kuudraArmor.armorTier();
                else if (tier.ordinal() > kuudraArmor.armorTier().ordinal()) tier = kuudraArmor.armorTier();
            }
        }
        return tier;
    }

    public static void applyStatBonus(SkyblockPlayer player, KuudraArmorTier tier, PlayerStatEvent event) {
        int stacks = DOMINUS_STACKS.getOrDefault(player, 0);
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
                } else
                if (event.stat() == Stat.Ferocity) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                }
            }
            case Fiery -> {
                if (event.stat() == Stat.SwingRange) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.1, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                } else
                if (event.stat() == Stat.Ferocity) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 2, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                }
            }
            case Infernal -> {
                if (event.stat() == Stat.SwingRange) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.2, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                } else
                if (event.stat() == Stat.Ferocity) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 2, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
                } else
                if (event.stat() == Stat.Damage) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.1, PlayerStatEvent.Type.AddativeMultiplier, PlayerStatEvent.StatsCategory.Ability));
                }
            }
        }
    }
}
