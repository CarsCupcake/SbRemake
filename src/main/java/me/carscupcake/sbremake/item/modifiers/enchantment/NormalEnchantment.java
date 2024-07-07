package me.carscupcake.sbremake.item.modifiers.enchantment;

import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;

import java.util.Map;

public enum NormalEnchantment implements SkyblockEnchantment {
    Sharpness("Sharpness", "sharpness", 7, ItemType.Sword, ItemType.Longsword) {
        @Override
        public Lore description() {
            return new Lore("§7Increases melee damage dealt by §a%bonus%%§7.", Map.of("%bonus%", (item, _) ->
                    StringUtils.cleanDouble(100 * EnchantmentUtils.getSharpnessBonus(item.getEnchantmentLevel(this)))));
        }
    },
    Efficiency("Efficiency", "efficiency", 10, ItemType.Pickaxe, ItemType.Drill) {
        @Override
        public Lore description() {
            return new Lore("§7Grants §6+%a% Mining Speed", Map.of("%a%", (item, _) -> String.valueOf(10 + (item.getEnchantmentLevel(this) * 20))));
        }

        @Override
        public ItemStack apply(ItemStack item, int level) {
            item = super.apply(item, level);
            EnchantmentList list = item.get(ItemComponent.ENCHANTMENTS);
            if (list == null) list = new EnchantmentList(Enchantment.EFFICIENCY, level);
            else list = list.with(Enchantment.EFFICIENCY, level);
            return item.with(ItemComponent.ENCHANTMENTS, list.withTooltip(false));
        }
    };

    private final String name;
    private final String id;
    private final int maxLevel;
    private final ItemType[] types;
    NormalEnchantment(String name, String id, int maxLevel, ItemType... types) {
        this.name = name;
        this.id = id;
        this.maxLevel = maxLevel;
        this.types = types;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ItemType[] types() {
        return types;
    }
}
