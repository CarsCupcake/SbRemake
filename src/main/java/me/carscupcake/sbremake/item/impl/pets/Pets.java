package me.carscupcake.sbremake.item.impl.pets;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerSkillXpEvent;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.ability.PetAbility;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

public enum Pets implements IPet {
    BlueWhale("Blue Whale", PetType.Fishing, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFiNzc5YmJjY2M4NDlmODgyNzNkODQ0ZThjYTJmM2E2N2ExNjk5Y2IyMTZjMGExMWI0NDMyNmNlMmNjMjAifX19",
            Map.of(Stat.Health, new PetStat(2)), new PetAbility("Ingest", "§7All potions heal §a+%h%" + (Stat.Health.getSymbol()) + ".", Map.of("%h%", new PetStat(0.5)))),
    Ghoul("Ghoul", PetType.Combat, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc5MzQ1NjViZjUyMmY2ZjQ3MjZjZGZlMTI3MTM3YmUxMWQzN2MzMTBkYjM0ZDhjNzAyNTMzOTJiNWZmNWIifX19",
            Map.of(Stat.Health, new PetStat(1), Stat.Intelligence, new PetStat(0.75), Stat.Ferocity, new PetStat(0.05), Stat.Vitality, new PetStat(0.25), Stat.Mending, new PetStat(0.25)),
            new PetAbility("Undead Slayer", new Lore("§7Gain §b%b%x§7 Combat XP against §aZombies§7.", "%b%", (item, _) -> StringUtils.cleanDouble(1 + (0.005 * petLevel(item)), 3))),
            new PetAbility("Army of the Dead", new Lore("§7Increases the amount of souls you can store by §a2§7 and the chance of getting a mob's soul by §a%b%%§7.", "%b%", (item, _) -> StringUtils.cleanDouble((0.2 * petLevel(item)), 1))),
            new PetAbility("Reaper Soul",
                    new Lore("§7Reduces the summoning cost of mobs by §a%a%%§7 and increases their damage output by §a%b%%§7. Increases the health of all summoned mobs by §a%c%%§7.",
                            Map.of("%b%", (item, _) -> StringUtils.cleanDouble((0.2 * petLevel(item)), 1), "%a%", (item, _) -> StringUtils.cleanDouble((0.3 * petLevel(item)), 1), "%c%", (item, _) -> String.valueOf(petLevel(item))))));
    private final String name;
    private final String id;
    private final String skullValue;
    private final PetAbility[] ability;
    private final Map<Stat, PetStat> petStats;
    private final PetType petType;

    public static final EventNode<Event> events = EventNode.all("pets").addListener(PlayerSkillXpEvent.class, event -> {
        if (event.getSkill() == Skill.Combat) {
            if (event.getPlayer().getPet() != null && event.getPlayer().getPet().getPet() == Ghoul) {
                event.setMultiplier(event.getMultiplier() * (1 + (event.getPlayer().getPet().getLevel() * 0.005d)));
            }
        }
    });

    Pets(String name, PetType petType, String skullValue, Map<Stat, PetStat> petStats, PetAbility... ability) {
        this(name, name.toUpperCase().replace(' ', '_'), petType, skullValue, petStats, ability);
    }
    Pets(String name, String id, PetType petType, String skullValue, Map<Stat, PetStat> petStats, PetAbility... ability) {
        this.name = name;
        this.id = id;
        this.skullValue = skullValue;
        this.ability = ability;
        this.petStats = petStats;
        this.petType = petType;
    }

    private static int petLevel(SbItemStack item) {
        if (item == null) return 1;
        Pet.PetInfo petInfo = item.getModifier(Modifier.PET_INFO);
        return petInfo.level();
    }

    @Override
    public double getStat(Stat stat, Pet.PetInfo petInfo) {
        PetStat s = petStats.get(stat);
        if (s != null) return s.level0 + (s.increment * petInfo.level());
        return 0;
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
    public String skullValue() {
        return skullValue;
    }

    @Override
    public PetAbility[] getAbility(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON, UNCOMMON -> Arrays.copyOf(ability, Math.min(1, ability.length));
            case RARE, EPIC -> Arrays.copyOf(ability, Math.min(2, ability.length));
            case LEGENDARY -> Arrays.copyOf(ability, Math.min(3, ability.length));
            default -> ability;
        };
    }

    public record PetStat(double level0, double increment) implements Lore.IPlaceHolder {
        public PetStat(double increment) {
            this(0, increment);
        }

        @Override
        public String replace(SbItemStack item, @Nullable SkyblockPlayer player) {
            return StringUtils.cleanDouble(level0 + (item.getModifier(Modifier.PET_INFO).level() * increment));
        }
    }

    @Override
    public PetType getPetType() {
        return petType;
    }
}
