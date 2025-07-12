package me.carscupcake.sbremake.item.minion;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.ability.Ability;
import net.minestom.server.item.Material;

import java.util.List;

public record MinionItem(String name, String id, String head, List<Ability> placeAbility) implements ISbItem, HeadWithValue {
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Minion;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return placeAbility;
    }

    @Override
    public String value() {
        return head;
    }
}
