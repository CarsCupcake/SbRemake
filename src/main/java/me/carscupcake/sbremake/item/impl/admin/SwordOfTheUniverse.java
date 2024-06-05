package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class SwordOfTheUniverse implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "NOVA_SWORD";
    }

    @Override
    public String getName() {
        return "S§cw§6o§er§ad §bo§3f §9t§dh§9e §3U§bn§ai§ev§6e§cr§4s§ce";
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
        return ItemRarity.ADMIN;
    }

    @Override
    public Map<Stat, Double> stats() {
        return Map.of(Stat.Damage, 99_999_999d, Stat.SwingRange, 99_999_999d);
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
