package me.carscupcake.sbremake.player.potion;

import me.carscupcake.sbremake.item.Lore;

import java.util.*;
import java.util.function.Function;

public record PotionLore(List<String> base, Map<String, Function<Integer, String>> placeholder) {
    public PotionLore(List<String> lore) {
        this(lore, new HashMap<>());
    }

    public PotionLore(String lore) {
        this(refactorLore(lore));
    }

    public PotionLore(String lore, Map<String, Function<Integer, String>> placeHolderHashMap) {
        this(refactorLore(lore), placeHolderHashMap);
    }

    public PotionLore(String lore, String key, Function<Integer, String> function) {
        this(refactorLore(lore), Map.of(key, function));
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

    public static List<String> refactorLore(String string) {
        List<String> lore = new ArrayList<>();
        StringBuilder worker = new StringBuilder();
        String lastColorCode = "";
        String lastFormat = "";
        boolean color = false;
        int i = 0;
        for (char c : string.toCharArray()) {
            if (c == '\n' || (i++ > 30 && c == ' ')) {
                lore.add(worker.toString());
                worker = new StringBuilder(lastColorCode).append(lastFormat);
                i = 0;
                continue;
            }
            if (color) {
                color = false;
                if (chars.contains(c)) {
                    if (c == 'r') lastFormat = "";
                    else lastFormat += STR."§\{c}";
                } else {
                    lastColorCode = STR."§\{c}";
                    lastFormat = "";
                }
            } else if (c == '§') {
                i -= 2;
                color = true;
            }
            worker.append(c);
        }
        if (!worker.isEmpty()) lore.add(worker.toString());
        return lore;
    }
}
