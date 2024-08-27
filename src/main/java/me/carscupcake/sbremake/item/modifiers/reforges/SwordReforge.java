package me.carscupcake.sbremake.item.modifiers.reforges;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.modifiers.RarityStat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum SwordReforge implements Reforge {
    Epic(Map.of(Stat.Strength, new RarityStat(15, 20, 25, 32, 40, 50),
            Stat.CritDamage, new RarityStat(10, 15, 20, 27, 35, 45),
            Stat.AttackSpeed, new RarityStat(1, 2, 4, 7, 10, 15))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Fair(Map.of(Stat.Strength, new RarityStat(2, 3, 4, 7, 10, 12),
            Stat.CritDamage, new RarityStat(2, 3, 4, 7, 10, 12),
            Stat.CritChance, new RarityStat(2, 3, 4, 7, 10, 12),
            Stat.AttackSpeed, new RarityStat(2, 3, 4, 7, 10, 12),
            Stat.Intelligence, new RarityStat(2, 3, 4, 7, 10, 12))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Fast(Map.of(Stat.AttackSpeed, new RarityStat(10, 20, 30, 40, 50, 60))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Gentle(Map.of(Stat.AttackSpeed, new RarityStat(8, 10, 15, 20, 25, 30),
            Stat.Strength, new RarityStat(3, 5, 7, 10, 15, 20))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Heroic(Map.of(Stat.AttackSpeed, new RarityStat(1, 2, 2, 3, 5, 7),
            Stat.Intelligence, new RarityStat(40, 50, 65, 80, 100, 125),
            Stat.Strength, new RarityStat(15, 20, 25, 32, 40, 50))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Legendary(Map.of(Stat.AttackSpeed, new RarityStat(2, 3, 5, 7, 10, 15),
            Stat.Strength, new RarityStat(3, 7, 12, 18, 25, 32),
            Stat.CritChance, new RarityStat(5, 7, 9, 12, 15, 18),
            Stat.CritDamage, new RarityStat(5, 10, 15, 22, 28, 36),
            Stat.Intelligence, new RarityStat(5, 8, 12, 18, 25, 35))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Odd(Map.of(Stat.CritChance, new RarityStat(10, 12, 15, 20, 25, 30),
            Stat.CritDamage, new RarityStat(5, 10, 15, 22, 30, 40),
            Stat.Intelligence, new RarityStat(-5, -10, -18, -32, -50, -75))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Sharp(Map.of(Stat.CritChance, new RarityStat(10, 12, 14, 17, 20, 25),
            Stat.CritDamage, new RarityStat(20, 30, 40, 55, 75, 90))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Spicy(Map.of(Stat.AttackSpeed, new RarityStat(2, 3, 5, 7, 10, 15),
            Stat.Strength, new RarityStat(2, 3, 4, 7, 10, 12),
            Stat.CritChance, new RarityStat(1),
            Stat.CritDamage, new RarityStat(25, 35, 45, 60, 80, 100))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Coldfused(Map.of(Stat.Strength, new RarityStat(15, 20, 25, 35, 45, 55),
            Stat.CritDamage, new RarityStat(20, 30, 40, 50, 60, 75),
            Stat.MagicFind, new RarityStat(2))) {
    },
    Dirty(Map.of(Stat.AttackSpeed, new RarityStat(2, 3, 5, 10, 15, 20),
            Stat.Strength, new RarityStat(2, 4, 6, 10, 12, 15),
            Stat.Ferocity, new RarityStat(2, 3, 6, 9, 12, 15))) {
        @Override
        public ItemType[] allowedTypes() {
            return SWORDS_FISHING;
        }
    },
    Fabled(Map.of(Stat.Strength, new RarityStat(30, 35, 40, 50, 60, 75),
            Stat.CritDamage, new RarityStat(15, 20, 25, 32, 40, 50)), new Lore("§7Critical hits have a chance to deal up to §a+15%§7 extra damage.")),
    Withered(new HashMap<>(), new Lore(STR."§7Grants §a+1 \{Stat.Strength} §7per §cCatacombs §7level.")) {
        private final RarityStat strength = new RarityStat(60, 75, 90, 110, 135, 170);

        @Override
        public double getStat(Stat stat, ItemRarity rarity, @Nullable SkyblockPlayer player) {
            if (stat == Stat.Strength) {
                if (player != null)
                    return strength.fromRarity(rarity) + player.getSkill(Skill.Dungeneering).getLevel();
                else return strength.fromRarity(rarity);
            }
            return super.getStat(stat, rarity, player);
        }
    };


    private static final ItemType[] SWORDS = {ItemType.Sword, ItemType.Longsword};
    private static final ItemType[] SWORDS_FISHING = {ItemType.Sword, ItemType.Longsword, ItemType.FishingRod};
    private final String name;
    private final String id;
    private final List<Requirement> requirements;
    private final Lore lore;
    private final Map<Stat, RarityStat> stats;

    SwordReforge(Map<Stat, RarityStat> stats, Requirement... requirement) {
        this(stats, Lore.EMPTY, requirement);

    }

    SwordReforge(Map<Stat, RarityStat> stats, Lore lore, Requirement... requirement) {
        this.name = name();
        this.id = name().toUpperCase().replace(' ', '_');
        this.requirements = (requirement != null) ? List.of(requirement) : new ArrayList<>(0);
        this.lore = lore;
        this.stats = stats;
    }

    SwordReforge(String name, String id, Map<Stat, RarityStat> stats, Lore lore, Requirement... requirement) {
        this.name = name;
        this.id = id;
        requirements = (requirement != null) ? List.of(requirement) : new ArrayList<>(0);
        this.lore = lore;
        this.stats = stats;
    }

    SwordReforge(String name, String id, Map<Stat, RarityStat> stats, Requirement... requirement) {
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
        return SWORDS;
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
