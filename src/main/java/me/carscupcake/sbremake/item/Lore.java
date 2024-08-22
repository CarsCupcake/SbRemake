package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record Lore(List<String> base, Map<String, IPlaceHolder> placeHolderHashMap) {

    public Lore(List<String> lore) {
        this(lore, new HashMap<>());
    }

    public Lore(String lore) {
        this(refactorLore(lore));
    }

    public Lore(String lore, Map<String, IPlaceHolder> placeHolderHashMap) {
        this(refactorLore(lore), placeHolderHashMap);
    }

    public static final Lore EMPTY = new Lore(new ArrayList<>(0), new HashMap<>(0));

    public List<String> build(SbItemStack item, @Nullable SkyblockPlayer player) {
        List<String> lore = new ArrayList<>();
        for (String line : base) {
            if (line.isBlank()) continue;
            for (Map.Entry<String, IPlaceHolder> placeHolderEntry : placeHolderHashMap.entrySet()) {
                if (line.contains(placeHolderEntry.getKey()))
                    line = line.replace(placeHolderEntry.getKey(), placeHolderEntry.getValue().replace(item, player));
            }
            lore.add(line);
        }
        return lore;
    }

    public interface IPlaceHolder {
        String replace(SbItemStack item, @Nullable SkyblockPlayer player);
    }

    public record AbilityDamagePlaceholder(double baseAbilityDamage, double abilityScaling) implements IPlaceHolder {

        @Override
        public String replace(SbItemStack item, @Nullable SkyblockPlayer player) {
            if (player == null) {
                return StringUtils.toFormatedNumber((int) baseAbilityDamage);
            }
            return StringUtils.toFormatedNumber((int) (baseAbilityDamage * (1 + abilityScaling * (player.getStat(Stat.Intelligence) / 100d))));
        }
    }

    private static final Set<Character> chars = Set.of('o', 'r', 'k', 'm', 'l', 'n');

    public static List<String> refactorLore(String string) {
        String[] split = string.split(" ");
        List<String> lore = new ArrayList<>();
        StringBuilder worker = new StringBuilder();
        String lastColorCode = "";
        String lastFormat = "";
        /*for (char c : string.toCharArray()) {
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
                color = true;
            }

            if (worker.chars().count() > 35 && c == ' ') {
                lore.add(worker.toString());
                worker = new StringBuilder(lastColorCode).append(lastFormat);
            } else worker.append(c);
        }*/
        int i = 0;
        for (String s : split) {
            if (i >= 35) {
                i = 0;
                lore.add(worker.toString());
                worker = new StringBuilder(lastColorCode).append(lastFormat);
            }
            i += s.length() + 1;
            worker.append(s).append(" ");
            boolean color = false;
            for (char c : s.toCharArray()) {
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
            }
        }
        if (worker.chars().count() != 0) lore.add(worker.toString());
        return lore;
    }
}
