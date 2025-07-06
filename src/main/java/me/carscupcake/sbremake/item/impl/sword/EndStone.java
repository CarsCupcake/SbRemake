package me.carscupcake.sbremake.item.impl.sword;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.item.ability.ManaRequirement;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.util.Cost;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class EndStoneSword implements ISbItem, ISbItem.StatProvider, GemstoneSlots {

    private final List<Ability> abilities = List.of(
        new ItemAbility<>("EXTREME FOCUS", AbilityType.RIGHT_CLICK, event -> {
            Player player = event.player();
            int mana = SbStatManager.getStat(player, StatType.INTELLIGENCE);

            if (mana < 20) {
                player.sendMessage("§cYou need at least 20 Mana to use this ability!");
                return;
            }

            // Consume all available mana
            SbStatManager.consumeMana(player, mana);

            int setsOfFive = mana / 5;
            double damageReduction = Math.min(setsOfFive * 0.01, 0.50); // Max 50%
            double nextHitBonusDamage = Math.min(setsOfFive * 1.0, 500.0); // Max 500%

            // Apply 5s Damage Reduction effect (custom effect class assumed)
            EffectManager.apply(player, new DamageReductionEffect(damageReduction, 100)); // 5 seconds = 100 ticks

            // Apply one-hit bonus damage (custom effect class assumed)
            EffectManager.apply(player, new OneHitBonusDamageEffect(nextHitBonusDamage));

            player.playSound(Sound.sound(Key.key("minecraft", "block.end_stone.break"), Sound.Source.PLAYER, 1.0f, 1.0f));
            player.sendMessage("§aExtreme Focus activated! §7" +
                "§b-" + (int)(damageReduction * 100) + "% Damage Taken §8(for 5s) §7and §c+" + (int)nextHitBonusDamage + "% Damage §8(next hit)");
        })
    );

    @Override
    public List<Ability> getAbilities() {
        return abilities;
    }
}

    @Override
    public String getId() {
        return "END_STONE_SWORD";
    }

    @Override
    public String getName() {
        return "End Stone Sword";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 120d, Stat.Strength, 80d);
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return abilities;
    }

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return new GemstoneSlotType[]{GemstoneSlotType.Sapphire};
    }

    @Override
    public boolean[] getUnlocked() {
        return new boolean[]{true};
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[0][];
    }
}
