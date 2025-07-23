package me.carscupcake.sbremake.item.collections.impl.mining;

import me.carscupcake.sbremake.item.BaseSbItem;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedBlockOfCoal;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedDiamond;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.impl.RecipeReward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.item.Material;

import java.util.List;

public class DiamondCollection extends Collection {
    public DiamondCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 1_000, 2_500, 5_000, 10_000, 25_000, 50_000}, List.of(
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("enchanted_diamond_star"))),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4),
                        new RecipeReward(Recipe.craftingRecipes.get("hardened_diamond_helmet")),
                        new RecipeReward(Recipe.craftingRecipes.get("hardened_diamond_chestplate")),
                        new RecipeReward(Recipe.craftingRecipes.get("hardened_diamond_leggings")),
                        new RecipeReward(Recipe.craftingRecipes.get("hardened_diamond_boots"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("enchanted_diamond_block_star"))),
                List.of(new SkyblockXpReward(4),
                        new RecipeReward(Recipe.craftingRecipes.get("perfect_helmet_1")),
                        new RecipeReward(Recipe.craftingRecipes.get("perfect_chestplate_1")),
                        new RecipeReward(Recipe.craftingRecipes.get("perfect_leggings_1")),
                        new RecipeReward(Recipe.craftingRecipes.get("perfect_boots_1")))));
    }

    @Override
    public Material showItem() {
        return Material.DIAMOND;
    }

    @Override
    public String getId() {
        return "DIAMOND";
    }

    @Override
    public String getName() {
        return "Diamond";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item) {
            case BaseSbItem i -> {
                if (i.getMaterial() == Material.DIAMOND) yield 1;
                yield 0;
            }
            case EnchantedDiamond ignored -> 160;
            case EnchantedBlockOfCoal ignored -> 160 * 160;
            case null, default -> 0;
        };
    }
}
