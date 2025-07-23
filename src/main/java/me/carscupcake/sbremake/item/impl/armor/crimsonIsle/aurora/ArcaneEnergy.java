package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorAbilityCounter;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorCommons;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.KuudraArmorType;
import me.carscupcake.sbremake.player.SkyblockPlayer;

import java.util.HashMap;
import java.util.Map;

public class ArcaneEnergy extends FullSetBonus {
    public static final ArcaneEnergy INSTANCE = new ArcaneEnergy();
    public static final String ArcaneEnergy = "Ѫ Arcane Energy";
    public static final Map<SkyblockPlayer, KuudraArmorAbilityCounter> ARCANE_ENERGY_MAP = new HashMap<>();

    public ArcaneEnergy() {
        super("Arcane Energy", 4, 2, true);
    }

    @Override
    public void start(SkyblockPlayer player) {
        var amount = player.getFullSetBonusPieceAmount(INSTANCE);
        var cooldown = (int) (20 * switch (amount) {
            case 3 -> 0.7;
            case 4 -> 0.5;
            default -> 1;
        });
        var looseTimes = 20 * switch (amount) {
            case 3 -> 7;
            case 4 -> 10;
            default -> 4;
        };
        ARCANE_ENERGY_MAP.put(player, new KuudraArmorAbilityCounter(player, looseTimes, cooldown, KuudraArmorCommons.calculateTier(player, KuudraArmorType.Aurora)));
    }

    @Override
    public void stop(SkyblockPlayer player) {
        ARCANE_ENERGY_MAP.remove(player).cancel();
    }

    @Override
    public Lore lore() {
        return new Lore("§7Every §a%time%s§7, dealing §bMagic Damage§7 from an ability grants §c1§7 stack of§6 " + ArcaneEnergy + "§7.\n§8Lose 1 stack after %loss%s of not gaining a stack.", Map.of("%time%", (ignored, player) -> player == null ? "1" : switch (player.getFullSetBonusPieceAmount(INSTANCE)) {
            case 3 -> "0.7";
            case 4 -> "0.5";
            default -> "1";
        }, "%loss%", (ignored, player) -> switch (player == null ? 0 : player.getFullSetBonusPieceAmount(INSTANCE)) {
            case 3 -> "7";
            case 4 -> "10";
            default -> "4";
        }));
    }
}
