package me.carscupcake.sbremake.item.modifiers.reforges;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.modifiers.RarityStat;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum ArmorReforge implements Reforge {
    Clean(Map.of(Stat.Health, new RarityStat(5, 7, 10, 15, 20, 25)
    ,Stat.Defense, new RarityStat(5, 7, 10, 15, 20, 25), Stat.CritChance, LINEAR_TWO)),
    Fierce(Map.of(Stat.Strength, LINEAR_TWO,
            Stat.CritChance, new RarityStat(2, 3, 4, 5, 6, 8),
            Stat.CritDamage, new RarityStat(4, 7, 10, 14, 18, 24))),
    Heavy(Map.of(Stat.Defense, new RarityStat(25, 35, 50, 65, 80, 110),
            Stat.CritDamage, new RarityStat(-1, -2, -2, -3, -5, -7),
            Stat.Speed, new RarityStat(-1))),
    Light(Map.of(Stat.Health, new RarityStat(5, 7, 10, 15, 20, 25),
            Stat.Defense, LINEAR_ONE,
            Stat.CritChance, new RarityStat(1, 1, 2, 2, 3, 3),
            Stat.CritDamage,  LINEAR_ONE,
            Stat.Speed, LINEAR_ONE,
            Stat.AttackSpeed, LINEAR_ONE)),
    Mythic(Map.of(Stat.Health, new RarityStat(2, 3, 4, 6, 8, 10),
            Stat.Defense, new RarityStat(2, 3, 4, 6, 8, 10),
            Stat.Strength, new RarityStat(2, 3, 4, 6, 8, 10),
            Stat.CritChance, LINEAR_TWO,
            Stat.CritDamage, new RarityStat(2, 3, 4, 6, 8, 10),
            Stat.Speed, new RarityStat(1),
            Stat.Intelligence, new RarityStat(2, 3, 4, 6, 8, 10),
            Stat.AttackSpeed, new RarityStat(1, 1, 2, 3, 4, 5))),
    Titanic(Map.of(Stat.Health, new RarityStat(10, 15, 20, 25, 35, 50),
            Stat.Defense, new RarityStat(10, 15, 20, 25, 35, 50))),
    Smart(Map.of(Stat.Health, new RarityStat(4, 6, 9, 12, 15, 20),
            Stat.Defense, new RarityStat(4, 6, 9, 12, 15, 20),
            Stat.Intelligence, new RarityStat(20, 40, 60, 80, 100, 120))),
    Wise(Map.of(Stat.Health, new RarityStat(6, 8, 10, 12, 15, 20),
            Stat.Speed, new RarityStat(1, 1, 1, 2, 2, 3),
            Stat.Intelligence, new RarityStat(25, 50, 75, 100, 125, 150))),
    Renowned(Map.of(Stat.Strength, new RarityStat(3, 4, 6, 8, 10, 12),
            Stat.CritChance, LINEAR_TWO,
            Stat.CritDamage, new RarityStat(3, 4, 6, 8, 10, 12),
            Stat.AttackSpeed, new RarityStat(1, 1, 2, 3, 4, 5),
            Stat.Health, new RarityStat(2, 3, 4, 6, 8, 10),
            Stat.Defense, new RarityStat(2, 3, 4, 6, 8, 10),
            Stat.Speed, new RarityStat(1),
            Stat.Intelligence, new RarityStat(3, 4, 6, 8, 10, 12)), new Lore("§7Increases most stats by §a1%")),
    Ancient(Map.of(Stat.Strength, new RarityStat(4, 8, 12, 18, 25, 35),
            Stat.CritChance, new RarityStat(3, 5, 7, 9, 12, 15),
            Stat.Health, new RarityStat(7),
            Stat.Defense, new RarityStat(7),
            Stat.Intelligence, new RarityStat(6, 9, 12, 16, 20, 25)),
            new Lore(STR."§7Grants §a+1 \{Stat.CritDamage}§7 per §cCatacombs§7 level."), new SkillRequirement(Skill.Mining, 30)) {
        @Override
        public double getStat(Stat stat, ItemRarity rarity, @Nullable SkyblockPlayer player) {
            if (stat == Stat.CritDamage) {
                return player == null ? 0 : player.getSkill(Skill.Dungeneering).getLevel();
            }
            return super.getStat(stat, rarity, player);
        }
    },
    Necrotic(Map.of(Stat.Intelligence, new RarityStat(30, 60, 90, 120, 150, 200))),
    Loving(Map.of(Stat.Health, new RarityStat(4, 5, 6, 8, 10, 14),
            Stat.Defense, new RarityStat(4, 5, 6, 7, 10, 14),
            Stat.Intelligence, new RarityStat(20, 40, 60, 80, 100, 120)), new Lore("Increases ability damage by §a+5%§7."));
    private static final ItemType[] ARMOR = new ItemType[]{ItemType.Helmet, ItemType.Chestplate, ItemType.Leggings, ItemType.Boots};
    private final String name;
    private final String id;
    private final List<Requirement> requirements;
    private final Lore lore;
    private final Map<Stat, RarityStat> stats;

    ArmorReforge(Map<Stat, RarityStat> stats, Requirement... requirement) {
        this(stats, Lore.EMPTY, requirement);

    }

    ArmorReforge(Map<Stat, RarityStat> stats, Lore lore, Requirement... requirement) {
        this.name = name();
        this.id = name().toUpperCase().replace(' ', '_');
        this.requirements = (requirement != null) ? List.of(requirement) : new ArrayList<>(0);
        this.lore = lore;
        this.stats = stats;
    }

    ArmorReforge(String name, String id, Map<Stat, RarityStat> stats, Lore lore, Requirement... requirement) {
        this.name = name;
        this.id = id;
        requirements = (requirement != null) ? List.of(requirement) : new ArrayList<>(0);
        this.lore = lore;
        this.stats = stats;
    }

    ArmorReforge(String name, String id, Map<Stat, RarityStat> stats, Requirement... requirement) {
        this(name, id, stats, Lore.EMPTY, requirement);
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
    public List<Requirement> requirements() {
        return requirements;
    }

    @Override
    public ItemType[] allowedTypes() {
        return ARMOR;
    }

    @Override
    public Lore getLore() {
        return lore;
    }

    @Override
    public double getStat(Stat stat, ItemRarity rarity, @Nullable SkyblockPlayer player) {
        RarityStat reforgeStat = stats.get(stat);
        return reforgeStat == null ? 0 : reforgeStat.fromRarity(rarity);
    }
}
