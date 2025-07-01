package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record PetAbility(String name, Lore lore) implements Ability {
    public PetAbility(String name, String lore) {
        this(name, new Lore(lore));
    }

    public PetAbility(String name, String lore, Map<String, Lore.IPlaceHolder> placeHolderHashMap) {
        this(name, new Lore(lore, placeHolderHashMap));
    }

    @Override
    public String headline(SbItemStack item, @Nullable SkyblockPlayer player) {
        return "§6" + (name);
    }
}
