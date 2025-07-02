package me.carscupcake.sbremake.item.impl.armor.crimsonIsle;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.KuudraArmorType;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneItem;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import net.minestom.server.entity.EquipmentSlot;

import java.util.List;

public abstract class KuudraArmorCommons implements ISbItem, ISbItem.StatProvider, StarUpgradable, GemstoneSlots, KuudraArmor {
    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public int getMaxStars() {
        return armorTier() == KuudraArmorTier.Infernal ? 15 : 10;
    }

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return new GemstoneSlotType[]{GemstoneSlotType.Combat, GemstoneSlotType.Combat};
    }

    @Override
    public boolean[] getUnlocked() {
        return new boolean[]{false, false};
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[][]{{new CoinsCost(250_000), new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Jasper).get(Gemstone.Quality.Flawless).asItem(), 1)
                , new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Sapphire).get(Gemstone.Quality.Flawless).asItem(), 1), new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Ruby).get(Gemstone.Quality.Flawless).asItem(), 1)
                , new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Amethyst).get(Gemstone.Quality.Flawless).asItem(), 1)}, {new CoinsCost(250_000), new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Jasper).get(Gemstone.Quality.Flawless).asItem(), 1)
                , new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Sapphire).get(Gemstone.Quality.Flawless).asItem(), 1), new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Ruby).get(Gemstone.Quality.Flawless).asItem(), 1)
                , new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Amethyst).get(Gemstone.Quality.Flawless).asItem(), 1)}};
    }

    @Override
    public Cost[] upgradeCost(SbItemStack item, int star) {
        return armorTier().getCosts()[star];
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SkillRequirement(Skill.Combat, switch (armorTier()) {
            case Base -> 22;
            case Hot -> 27;
            case Burning -> 32;
            case Fiery -> 37;
            case Infernal -> 42;
        }));
    }

    public static KuudraArmorTier calculateTier(SkyblockPlayer player, KuudraArmorType armorType) {
        KuudraArmorTier tier = null;
        for (var slot : EquipmentSlot.armors()) {
            var item = player.getSbEquipment(slot);
            if (item.sbItem() instanceof KuudraArmor kuudraArmor) {
                if (kuudraArmor.armorType() != armorType) continue;
                if (tier == null) tier = kuudraArmor.armorTier();
                else if (tier.ordinal() > kuudraArmor.armorTier().ordinal()) tier = kuudraArmor.armorTier();
            }
        }
        return tier;
    }
}
