package me.carscupcake.sbremake.item.impl.sword;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.*;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InkWand implements ISbItem {
    private static final List<Ability> abilities = List.of(new ItemAbility<>("Ink Bomb", AbilityType.RIGHT_CLICK, playerInteractEvent -> {

    }, new Lore("§7Shoot an ink bomb in front of you dealing §a%dmg% §7damage and giving blindness!", Map.of("%dmg%", new Lore.AbilityDamagePlaceholder(10_000, 1d))), new ManaRequirement<>(60), new CooldownRequirement<>(30)));

    @Override
    public String getId() {
        return "INK_WAND";
    }

    @Override
    public String getName() {
        return "Ink Wand";
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return abilities;
    }
}
