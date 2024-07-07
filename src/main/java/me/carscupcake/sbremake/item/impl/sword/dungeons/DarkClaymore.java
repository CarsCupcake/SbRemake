package me.carscupcake.sbremake.item.impl.sword.dungeons;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class DarkClaymore implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "DARK_CLAYMORE";
    }

    @Override
    public String getName() {
        return "Dark Claymore";
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Longsword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }


    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 500d, Stat.Strength, 100d, Stat.CritDamage, 30d, Stat.SwingRange, 2d);
    }

    @Override
    public Lore getLore() {
        return new Lore("§7§oThat thing wa too big to be called a sword, it was more like a hunk of stone");
    }
}
