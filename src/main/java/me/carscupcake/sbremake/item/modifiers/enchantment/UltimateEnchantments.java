package me.carscupcake.sbremake.item.modifiers.enchantment;

import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import me.carscupcake.sbremake.util.StringUtils;

public enum UltimateEnchantments implements UltimateEnchantment {
    UltimateWise("Ultimate Wise", "ULTIMATE_WISE", 5 ,ItemType.SWORDS) {
        static final Lore l =new Lore(
            "§7Reduces the ability mana cost of this item by §a%d%%§7.", "%d%", new NormalEnchantments.LevelToBonusPlaceholder(UltimateWise, integer -> String.valueOf(
                StringUtils.cleanDouble(100 * EnchantmentUtils.getUltimateWiseBonus(integer))
                ))
                );
        @Override
        public Lore description() {
            return l;
        }
    };

    private final String name;
    private final String id;
    private final int maxLevel;
    private final ItemType[] types;

    UltimateEnchantments(String name, String id, int maxLevel, ItemType... types) {
        this.name = "§d§l" + name;
        this.id = id.toLowerCase();
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
