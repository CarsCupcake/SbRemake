package me.carscupcake.sbremake.item.modifiers.enchantment;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import me.carscupcake.sbremake.util.Lazy;
import me.carscupcake.sbremake.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public enum NormalEnchantments implements SkyblockEnchantment {
    Sharpness("Sharpness", "sharpness", 5, 7, ItemType.Sword, ItemType.Longsword) {
        @Override
        public Lore description() {
            return new Lore("§7Increases melee damage dealt by §a%bonus%%§7.", Map.of("%bonus%", (item, _) ->
                    StringUtils.cleanDouble(100 * EnchantmentUtils.getSharpnessBonus(item.getEnchantmentLevel(this)))));
        }
    },
    Smite("Smite", "smite", 5,7, ItemType.Sword, ItemType.Longsword) {
        @Override
        public Lore description() {
            return new Lore("§7Increases damage dealt to Skeletons, Zombie Pigmen, Withers and Zombies by §a%bonus%%§7.", Map.of("%bonus%", (item, _) ->
                    StringUtils.cleanDouble(100 * EnchantmentUtils.getSharpnessBonus(item.getEnchantmentLevel(this)))));
        }
    },
    Cleave("Cleave", "cleave", 5, 6, ItemType.Sword, ItemType.Longsword) {
        @Override
        public Lore description() {
            return new Lore("§7Deals §a%a%%§7 of your damage dealt to other monsters within §a%b%§7 blocks of the target.", Map.of("%a%", (item, _) -> StringUtils.cleanDouble(100 * EnchantmentUtils.getCleaveBonus(item.getEnchantmentLevel(this))),
                    "%b%", (item, _) -> StringUtils.cleanDouble(3 + (item.getEnchantmentLevel(this)) * 0.03)));
        }
    },
    BaneOfArthropods("Bane Of Arthropods", "BANE_OF_ARTHROPODS", 5, 7, ItemType.SWORDS){
        private static final Lore lore = new Lore("§7Increases damage dealt to Cave Spiders, Spiders, and Silverfish by §a%d%§7.",
                "%d%", new LevelToBonusPlaceholder(BaneOfArthropods, integer -> StringUtils.cleanDouble(100 * EnchantmentUtils.getBaneOfArthropodsBonus(integer))));
        @Override
        public Lore description() {
            return lore;
        }

        private static final Set<SkyblockEnchantment> conflicts = Set.of(NormalEnchantments.Sharpness, NormalEnchantments.Smite);

        @Override
        public Set<SkyblockEnchantment> conflicts() {
            return conflicts;
        }
    },
    Critical("Critical", "CRITICAL", 5, 7, ItemType.SWORDS){
        private static final Lore lore = new Lore("§7Increases %s§7 by §a!d!%%".formatted(Stat.CritDamage),
                "!d!", new LevelToBonusPlaceholder(EnchantmentUtils::getCriticalBonus, Critical));
        @Override
        public Lore description() {
            return lore;
        }
    },
    Cubism("Cubism", "Cubism", 5, 6, ItemType.Sword, ItemType.Longsword, ItemType.FishingRod){
        private static final Lore lore = new Lore("§7Increases damage dealt to Magma Cubes, Creepers, and Slimes by §a!d!%.",
                "!d!", new LevelToBonusPlaceholder(Cubism, integer -> StringUtils.cleanDouble(100 * EnchantmentUtils.getCubismBonus(integer))));
        @Override
        public Lore description() {
            return lore;
        }
    },
    DivineGift("Divine Gift", "DIVINE_GIFT", 0, 3, ItemType.Sword, ItemType.Longsword, ItemType.Bow, ItemType.FishingRod){
        private static final Lore lore = new Lore("§7Grants +!d! %s§7.".formatted(Stat.MagicFind),
                "!d!", new LevelToBonusPlaceholder(EnchantmentUtils::getDivineGiftBonus, DivineGift));
        @Override
        public Lore description() {
            return lore;
        }
    },
    DragonHunter("Dragon Hunter", "DRAGON_HUNTER", 5, ItemType.Sword, ItemType.Longsword, ItemType.Bow, ItemType.Gauntlet, ItemType.FishingRod) {
        private static final Lore lore = new Lore("§7Increases damage dealt to Ender Dragons by §a!d!%§7.",
                "!d!", new LevelToBonusPlaceholder(DragonHunter, integer -> StringUtils.cleanDouble(100 * EnchantmentUtils.getDragonHunterBonus(integer))));
        @Override
        public Lore description() {
            return lore;
        }
    },
    EnderSlayer("Ender Slayer", "ENDER_SLAYER", 5, 7, ItemType.Sword, ItemType.Longsword, ItemType.Gauntlet, ItemType.FishingRod) {
        private static final Lore lore = new Lore("§7Increases damage dealt to Ender Dragons, Endermen, and Endermites by §a!d!%§7.",
                "!d!", new LevelToBonusPlaceholder(DragonHunter, integer -> StringUtils.cleanDouble(100 * EnchantmentUtils.getEnderSlayerBonus(integer))));
        @Override
        public Lore description() {
            return lore;
        }
    },
    Execute("Execute", "Execute", 5, 6, ItemType.Sword, ItemType.Longsword, ItemType.Gauntlet, ItemType.FishingRod) {
        private static final Lore lore = new Lore("§7Increases damage dealt by §a!d!%§7 for each percent of health missing on your target.",
                "!d!", new LevelToBonusPlaceholder(DragonHunter, integer -> StringUtils.cleanDouble(100 * EnchantmentUtils.getExecuteBonus(integer))));
        @Override
        public Lore description() {
            return lore;
        }

        private static final Lazy<Set<SkyblockEnchantment>> conflicts = new Lazy<>(() -> Set.of(Prosecute));

        @Override
        public Set<SkyblockEnchantment> conflicts() {
            return conflicts.get();
        }
    },
    Prosecute("Prosecute", "Prosecute", 5, 6, ItemType.Sword, ItemType.Longsword, ItemType.Gauntlet, ItemType.FishingRod) {
        private static final Lore lore = new Lore("§7Increases damage dealt by §a!d!%§7 for each percent of health your target has.",
                "!d!", new LevelToBonusPlaceholder(DragonHunter, integer -> StringUtils.cleanDouble(100 * EnchantmentUtils.getProsecuteBonus(integer))));
        @Override
        public Lore description() {
            return lore;
        }

        private static final Lazy<Set<SkyblockEnchantment>> conflicts = new Lazy<>(() -> Set.of(Execute));

        @Override
        public Set<SkyblockEnchantment> conflicts() {
            return conflicts.get();
        }
    },
    //----------------------- Pickaxe Enchantments -----------------------
    Efficiency("Efficiency", "efficiency", 5, 10, ItemType.Pickaxe, ItemType.Drill) {
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
    Fortune("Fortune", "fortune", 3, 4, ItemType.Pickaxe, ItemType.Drill) {
        @Override
        public Lore description() {
            return new Lore("§7Grants §6+%a% " + (Stat.MiningFortune), Map.of("%a%", (item, _) -> String.valueOf(EnchantmentUtils.getFortuneBonus(item.getEnchantmentLevel(this)))));
        }
    },
    Pristine("Pristine", "pristine", 0, 5, ItemType.Pickaxe, ItemType.Drill) {
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
    private final int maxEnchantmentTableLevel;
    private final ItemType[] types;

    NormalEnchantments(String name, String id, int maxLevel, ItemType... types) {
        this(name, id, maxLevel, maxLevel, types);
    }

    NormalEnchantments(String name, String id, int maxEnchantmentTableLevel, int maxLevel, ItemType... types) {
        this.name = name;
        this.id = id.toLowerCase();
        this.maxLevel = maxLevel;
        this.maxEnchantmentTableLevel = maxEnchantmentTableLevel;
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

    public record LevelToBonusPlaceholder(SkyblockEnchantment enchantment, Function<Integer, String> converter) implements Lore.IPlaceHolder {

        public LevelToBonusPlaceholder(Function<Integer, Integer> converter, SkyblockEnchantment enchantment) {
            this(enchantment, integer -> String.valueOf(converter.apply(integer)));
        }

        @Override
        public String replace(SbItemStack item, @Nullable SkyblockPlayer player) {
            if (item == null) return converter.apply(1);
            var level = item.getEnchantmentLevel(enchantment);
            return converter.apply(Math.max(1, level));
        }
    }
}
