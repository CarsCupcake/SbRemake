package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record Lore(List<String> base, HashMap<String, IPlaceHolder> placeHolderHashMap) {

    public Lore(List<String> lore) {
        this(lore, new HashMap<>());
    }

    public Lore(String lore) {
        this(refactorLore(lore));
    }

    public static final Lore EMPTY = new Lore(new ArrayList<>(), new HashMap<>());

    public List<String> build(SbItemStack item, @Nullable SkyblockPlayer player) {
        List<String> lore = new ArrayList<>();
        for (String line : base) {
            if (line.isBlank()) continue;
            for (Map.Entry<String, IPlaceHolder> placeHolderEntry : placeHolderHashMap.entrySet()) {
                if (line.contains(placeHolderEntry.getKey())) line = line.replace(placeHolderEntry.getKey(), placeHolderEntry.getValue().replace(item, player));
            }
            lore.add(line);
        }
        return lore;
    }

    public interface IPlaceHolder {
        String replace(SbItemStack item, @Nullable SkyblockPlayer player);
    }

    private static final Set<Character> chars = Set.of('o', 'r', 'k', 'm', 'l', 'n');
    public static List<String> refactorLore(String string) {
        List<String> lore = new ArrayList<>();
        StringBuilder worker = new StringBuilder();
        String lastColorCode = "";
        String lastFormat = "";
        boolean color = false;
        for (char c : string.toCharArray()) {
            if (color) {
                color = false;
                if (chars.contains(c)) {
                    if (c == 'r')
                        lastFormat = "";
                    else
                        lastFormat += STR."§\{c}";
                }
                else {
                    lastColorCode = STR."§\{c}";
                    lastFormat = "";
                }
            } else if (c == '§') {
                color = true;
            }

            if (worker.chars().count() > 40 && c == ' ') {
                lore.add(worker.toString());
                worker = new StringBuilder(lastColorCode).append(lastFormat);
            } else worker.append(c);
        }
        if (worker.chars().count() != 0) lore.add(worker.toString());
        return lore;
    }
}
