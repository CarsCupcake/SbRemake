package me.carscupcake.sbremake.item.modifiers.enchantment;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;
import java.util.Set;

public enum NormalEnchantment implements SkyblockEnchantment {
    Sharpness("Sharpness", "sharpness", 7, ItemType.Sword, ItemType.Longsword) {
        @Override
        public Lore description() {
            return new Lore("§7Increases melee damage dealt by §a%bonus%%§7.", Map.of("%bonus%", (item, _) ->
                    StringUtils.cleanDouble(100 * EnchantmentUtils.getSharpnessBonus(item.getEnchantmentLevel(this)))));
        }
    },
    Smite("Smite", "smite", 7, ItemType.Sword, ItemType.Longsword) {
        @Override
        public Lore description() {
            return new Lore("§7Increases damage dealt to Skeletons, Zombie Pigmen, Withers and Zombies by §a%bonus%%§7.", Map.of("%bonus%", (item, _) ->
                    StringUtils.cleanDouble(100 * EnchantmentUtils.getSharpnessBonus(item.getEnchantmentLevel(this)))));
        }
    },
    Cleave("Cleave", "cleave", 6, ItemType.Sword, ItemType.Longsword) {
        @Override
        public Lore description() {
            return new Lore("§7Deals §a%a%%§7 of your damage dealt to other monsters within §a%b%§7 blocks of the target.", Map.of("%a%", (item, _) -> StringUtils.cleanDouble(100 * EnchantmentUtils.getCleaveBonus(item.getEnchantmentLevel(this))),
                    "%b%", (item, _) -> StringUtils.cleanDouble(3 + (item.getEnchantmentLevel(this)) * 0.03)));
        }
    },
    Efficiency("Efficiency", "efficiency", 10, ItemType.Pickaxe, ItemType.Drill) {
        @Override
        public Lore description() {
            return new Lore("§7Grants §6+%a% " + (Stat.MiningSpeed), Map.of("%a%", (item, _) -> String.valueOf(10 + (item.getEnchantmentLevel(this) * 20))));
        }

       /* @Override
        public ItemStack apply(ItemStack item, int level) {
            item = super.apply(item, level);
            EnchantmentList list = item.get(ItemComponent.ENCHANTMENTS);
            if (list == null) list = new EnchantmentList(Enchantment.EFFICIENCY, Math.min(255, level));
            else list = list.with(Enchantment.EFFICIENCY, Math.min(255, level));
            return item.with(ItemComponent.ENCHANTMENTS, list.withTooltip(false));
        }*/
    },
    Fortune("Fortune", "fortune", 4, ItemType.Pickaxe, ItemType.Drill) {
        @Override
        public Lore description() {
            return new Lore("§7Grants §6+%a% " + (Stat.MiningFortune), Map.of("%a%", (item, _) -> String.valueOf(EnchantmentUtils.getFortuneBonus(item.getEnchantmentLevel(this)))));
        }
    },
    Pristine("Pristine", "pristine", 5, ItemType.Pickaxe, ItemType.Drill) {
        @Override
        public Lore description() {
            return new Lore("§7Grants §5+%a% " + (Stat.Pristine) + "§7, which increases the chance to improve the quality of dropped §dGemstones§7.", Map.of("%a%", (item, _) -> String.valueOf(item.getEnchantmentLevel(this))));
        }
    },
    SilkTouch("Silk Touch", "silk_touch", 1, ItemType.Pickaxe, ItemType.Drill) {
        @Override
        public Lore description() {
            return new Lore("§7Allows you to collect normally unobtainable block drops.");
        }

        @Override
        public Set<SkyblockEnchantment> conflicts() {
            return Set.of(SmeltingTouch);
        }
    },
    SmeltingTouch("Smelting Touch", "smelting_touch", 1, ItemType.Pickaxe, ItemType.Drill) {
        @Override
        public Lore description() {
            return new Lore("§7Automatically smelts broken blocks into their smelted form.");
        }

        @Override
        public Set<SkyblockEnchantment> conflicts() {
            return Set.of(SilkTouch);
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
