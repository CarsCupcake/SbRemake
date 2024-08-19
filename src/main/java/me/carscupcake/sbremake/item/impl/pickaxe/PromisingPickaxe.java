package me.carscupcake.sbremake.item.impl.pickaxe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.List;
import java.util.Map;

public class PromisingPickaxe implements ISbItem, ISbItem.StatProvider, NpcSellable {
    @Override
    public String getId() {
        return "PROMISING_PICKAXE";
    }

    @Override
    public String getName() {
        return "Promising Pickaxe";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_PICKAXE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Pickaxe;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 20, Stat.MiningSpeed, 190, Stat.BreakingPower, 3);
    }

    @Override
    public int sellPrice() {
        return 10;
    }

    @Override
    public Lore getLore() {
        return new Lore(List.of("§7Gains §ßEfficiency I§7 after breaking §a50", "§ablocks§7!", " ", "§7Blocks Broken: §a%b%"), Map.of("%b%", (item, _) -> String.valueOf(((CompoundBinaryTag) item.item().getTag(Tag.NBT("ExtraAttributes"))).getInt("blocks", 0))));
    }
}
