package me.carscupcake.sbremake.item.collections.impl.mining;

import me.carscupcake.sbremake.item.BaseSbItem;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedBlockOfCoal;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedCoal;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedCobblestone;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.impl.RecipeReward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.item.Material;

import java.util.List;

public class CobblestoneCollection extends Collection {
    public CobblestoneCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 1_000, 2_500, 5_000, 10_000, 25_000, 40_000, 70_000}, List.of(List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("enchanted_cobblestone_star"))),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4),
                        new RecipeReward(Recipe.craftingRecipes.get("miners_outfit_helmet")),
                        new RecipeReward(Recipe.craftingRecipes.get("miners_outfit_chestplate")),
                        new RecipeReward(Recipe.craftingRecipes.get("miners_outfit_leggings")),
                        new RecipeReward(Recipe.craftingRecipes.get("miners_outfit_boots"))),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4))));
    }

    @Override
    public Material showItem() {
        return Material.COBBLESTONE;
    }

    @Override
    public String getId() {
        return "COBBLESTONE";
    }

    @Override
    public String getName() {
        return "Cobblestone";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item) {
            case BaseSbItem i -> {
                if (i.getMaterial() == Material.COBBLESTONE) yield 1;
                yield 0;
            }
            case EnchantedCobblestone _ -> 160;
            case null, default -> 0;
        };
    }
}
