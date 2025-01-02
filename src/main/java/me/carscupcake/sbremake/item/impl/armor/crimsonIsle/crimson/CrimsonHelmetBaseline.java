package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmor;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneItem;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public abstract class CrimsonHelmetBaseline implements ISbItem, ISbItem.StatProvider, HeadWithValue, Upgradable, GemstoneSlots, KuudraArmor {
    @Override
    public KuudraArmorType armorType() {
        return KuudraArmorType.Crimson;
    }
    @Override
    public String getId() {
        return STR."\{armorTier().getId()}CRIMSON_HELMET";
    }

    @Override
    public String getName() {
        return STR."\{armorTier().getName()} Crimson Helmet";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public int getMaxStars() {
        return 10;
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
    public Cost[] upgradeCost(SbItemStack item, int star) {
        return armorTier().getCosts()[star];
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
    public List<Requirement> requirements() {
        return List.of(new SkillRequirement(Skill.Combat, 22));
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(DominusAbility.INSTANCE);
    }
}
