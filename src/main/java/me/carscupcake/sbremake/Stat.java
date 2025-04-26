package me.carscupcake.sbremake;

import lombok.Getter;

@Getter
public enum Stat {
    Health("Health", "❤", "§c", false, 100d, -1d),
    Defense("Defense", "❈", "§a", false),
    Speed("Speed", "✦", "§f", false, 100, 500),
    Strength("Strength", "❁", "§c", false),
    Intelligence("Intelligence", "✎", "§b", false),
    CritChance("Crit Chance", "☣", "§9", true, 30, -1),
    CritDamage("Crit Damage", "☠", "§9", true, 50, -1),
    AttackSpeed("Bonus Attack Speed", "⚔", "§e", true, 0, 100),
    AbilityDamage("Ability Damage", "๑", "§c", true),
    MagicFind("Magic Find", "✯", "§b", false, 0, 900),
    PetLuck("Pet Luck", "♣", "§d", false),
    TrueDefense("True Defense", "❂", "§f", false),
    SeaCreatureChance("Sea Creature Chance", "α", "§3", true, 20, -1),
    Ferocity("Ferocity", "⫽", "§c", false, 0, 500),
    MiningSpeed("Mining Speed", "⸕", "§6", false),
    MiningFortune("Mining Fortune", "☘", "§6", false),
    FarmingFortune("Farming Fortune", "☘", "§6", false),
    WheatFortune(true, "Wheat Fortune", "☘", "§6"),
    CarrotFortune(true, "Carrot Fortune", "☘", "§6"),
    PotatoFortune(true, "Potato Fortune", "☘", "§6"),
    PumpkinFortune(true, "Pumpkin Fortune", "☘", "§6"),
    MelonFortune(true, "Melon Fortune", "☘", "§6"),
    MushroomFortune(true, "Mushroom Fortune", "☘", "§6"),
    CactusFortune(true, "Cactus Fortune", "☘", "§6"),
    SugarCaneFortune(true, "Sugar Cane Fortune", "☘", "§6"),
    NetherWartFortune(true, "Nether Wart Fortune", "☘", "§6"),
    CocoaBeansFortune(true, "Cocoa Beans Fortune", "☘", "§6"),
    ForagingFortune("Foraging Fortune", "☘", "§6", false),
    BreakingPower("Breaking Power", "Ⓟ", "§2", false),
    Pristine("Pristine", "✧", "§5", false),
    FishingSpeed("Fishing Speed", "☂", "§b", false, 0, 500),
    ColdResistance("Cold Resistance", "❄", "§b", false),
    BonusPestChance("Bonus Pest Chance", "❄", "§2", false),
    HealthRegen("Health Regen", "❣", "§c", false, 100, -1),
    Vitality("Vitality", "♨", "§4", false, 100, -1),
    Mending("Mending", "☄", "§a", false, 100, -1),
    SwingRange("Swing Range", "Ⓢ", "§e", false, 3, 15),
    CombatWisdom("Combat Wisdom", "☯", "§3", false, 0, -1),
    MiningWisdom("Mining Wisdom", "☯", "§3", false, 0, -1),
    FarmingWisdom("Farming Wisdom", "☯", "§3", false, 0, -1),
    ForagingWisdom("Foraging Wisdom", "☯", "§3", false, 0, -1),
    FishingWisdom("Fishing Wisdom", "☯", "§3", false, 0, -1),
    EnchantingWisdom("Enchanting Wisdom", "☯", "§3", false, 0, -1),
    AlchemyWisdom("Alchemy Wisdom", "☯", "§3", false, 0, -1),
    CarpentryWisdom("Carpentry Wisdom", "☯", "§3", false, 0, -1),
    RunecraftingWisdom("Runecrafting Wisdom", "☯", "§3", false, 0, -1),
    SocialWisdom("Social Wisdom", "☯", "§3", false, 0, -1),
    TamingWisdom("Taming Wisdom", "☯", "§3", false, 0, -1),
    Damage(true, "Damage", "❁", "§c");
    private final String name;
    private final String symbol;
    private final String prefix;
    private final boolean percentValue;
    private final double baseValue;
    private final double maxValue;
    private boolean hiddenStat;

    Stat(String name, String symbol, String prefix, boolean percentValue, double baseValue, double maxValue) {
        this.name = name;
        this.symbol = symbol;
        this.prefix = prefix;
        this.percentValue = percentValue;
        this.baseValue = baseValue;
        this.maxValue = maxValue;
        hiddenStat = false;
    }

    Stat(boolean hiddenStat, String name, String symbol, String prefix) {
        this(name, symbol, prefix, false);
        this.hiddenStat = hiddenStat;
    }

    Stat(String name, String symbol, String prefix, boolean isPercentValue) {
        this(name, symbol, prefix, isPercentValue, 0, -1);
    }

    @Override
    public String toString() {
        return (prefix) + (symbol) + " " + (name);
    }
}
