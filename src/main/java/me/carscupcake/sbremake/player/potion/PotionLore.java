package me.carscupcake.sbremake.player.potion;

import me.carscupcake.sbremake.item.Lore;

import java.util.*;
import java.util.function.Function;

public record PotionLore(List<String> base, Map<String, Function<Integer, String>> placeholder) {
    public PotionLore(List<String> lore) {
        this(lore, new HashMap<>());
    }

    public PotionLore(String lore) {
        this(Lore.refactorLore(lore));
    }

    public PotionLore(String lore, Map<String, Function<Integer, String>> placeHolderHashMap) {
        this(Lore.refactorLore(lore), placeHolderHashMap);
    }

    public PotionLore(String lore, String key, Function<Integer, String> function) {
        this(Lore.refactorLore(lore), Map.of(key, function));
    }

    public static final Lore EMPTY = new Lore(new ArrayList<>(0), new HashMap<>(0));

    public List<String> build(int potionLevel) {
        List<String> lore = new ArrayList<>();
        for (String line : base) {
            if (line.isBlank()) continue;
            for (Map.Entry<String, Function<Integer, String>> placeHolderEntry : placeholder.entrySet()) {
                if (line.contains(placeHolderEntry.getKey()))
                    line = line.replace(placeHolderEntry.getKey(), placeHolderEntry.getValue().apply(potionLevel));
            }
            lore.add(line);
        }
        return lore;
    }

    private static final Set<Character> chars = Set.of('o', 'r', 'k', 'm', 'l', 'n');
}
