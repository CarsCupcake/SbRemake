package me.carscupcake.sbremake.item.impl.other;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import net.minestom.server.item.Material;

import java.util.List;

public class SkyblockMenu implements ISbItem {
    @Override
    public String getId() {
        return "SKYBLOCK_MENU";
    }

    @Override
    public String getName() {
        return "Skyblock Menu §7(Click)";
    }

    @Override
    public Material getMaterial() {
        return Material.NETHER_STAR;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public Lore getLore() {
        return new Lore(List.of("§7View all of your Skyblock progress,", "§7including your Skills, Collections,", "§7Recipes, and more!", "§7 ", "§eClick to open!"));
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(new ItemAbility<>("", AbilityType.RIGHT_CLICK, playerInteractEvent -> playerInteractEvent.player().openSkyblockMenu()));
    }
}
