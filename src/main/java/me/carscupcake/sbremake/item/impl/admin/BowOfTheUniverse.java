package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.impl.bow.Shortbow;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class BowOfTheUniverse implements ISbItem, ISbItem.StatProvider, Shortbow {
    @Override
    public String getId() {
        return "UNIVERSE_BOW";
    }

    @Override
    public String getName() {
        return "B§co§6w §eo§af §bt§3h§9e §dU§9n§3i§bv§ae§er§6s§ce";
    }

    @Override
    public Material getMaterial() {
        return Material.BOW;
    }

    @Override
    public ItemType getType() {
        return ItemType.Bow;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 99999999d, Stat.AttackSpeed, 500d);
    }

    @Override
    public Lore getLore() {
        return new Lore(List.of("§eOi you! Yes you. What are you looking", "§eat? Yes this bow has ∞ damage.", "§eKinda overkill? I'm lazy ok.", "§c(╯°□°）╯§f︵§7 ┻━┻"));
    }

    @Override
    public Lore statsReplacement() {
        return new Lore(List.of("§7Damage: §d+∞", "§7Clout: §d+100", "§7Skill: §d-∞"));
    }
}
