package me.carscupcake.sbremake.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Lore(List<String> base, HashMap<String, IPlaceHolder> placeHolderHashMap) {
    public static final Lore EMPTY = new Lore(new ArrayList<>(), new HashMap<>());

    public List<String> build(SbItemStack item) {
        List<String> lore = new ArrayList<>();
        for (String line : base) {
            if (line.isBlank()) continue;
            for (Map.Entry<String, IPlaceHolder> placeHolderEntry : placeHolderHashMap.entrySet()) {
                if (line.contains(placeHolderEntry.getKey())) line = line.replace(placeHolderEntry.getKey(), placeHolderEntry.getValue().replace(item));
            }
            lore.add(line);
        }
        return lore;
    }

    public interface IPlaceHolder {
        String replace(SbItemStack item);
    }
}
