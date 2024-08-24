package me.carscupcake.sbremake.item.collections.impl.mining;

import me.carscupcake.sbremake.item.BaseSbItem;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedBlockOfCoal;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedCoal;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.RecipeReward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.item.Material;

import java.util.List;

public class CoalCollection extends Collection {
    public CoalCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 1_000, 2_500, 5_000, 10_000, 25_000, 50_000, 100_000}, List.of(List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("enchanted_coal_star"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("enchanted_charcoal"))),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("enchanted_coal_block_star"))),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4))));
    }

    @Override
    public Material showItem() {
        return Material.COAL;
    }

    @Override
    public String getId() {
        return "COAL";
    }

    @Override
    public String getName() {
        return "Coal";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item) {
            case BaseSbItem i -> {
                if (i.getMaterial() == Material.COAL) yield 1;
                yield 0;
            }
            case EnchantedCoal _ -> 160;
            case EnchantedBlockOfCoal _ -> 160 * 160;
            case null, default -> 0;
        };
    }
}
