package me.carscupcake.sbremake.item.collections.impl.combat;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.RecipeReward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.item.Material;

import java.util.List;

public class RottenFleshCollection extends Collection {
    private static List<Reward> sbXpReward = List.of(new SkyblockXpReward(4));
    public RottenFleshCollection(SkyblockPlayer player) {
        super(player, new int[] {50, 100, 250, 1_000, 2_500, 5_000, 10_000, 25_000, 50_000, 100_000},
              List.of(sbXpReward, List.of(new RecipeReward(Recipe.craftingRecipes.get("zombie_pickaxe")), new SkyblockXpReward(4)),
                      sbXpReward, List.of(new RecipeReward(Recipe.craftingRecipes.get("enchanted_rotten_flesh")), new SkyblockXpReward(4)),
                      List.of(new RecipeReward(Recipe.craftingRecipes.get("enchanted_rotten_flesh")), new SkyblockXpReward(4)), sbXpReward,
                      sbXpReward, sbXpReward, sbXpReward, sbXpReward));
    }

    @Override
    public Material showItem() {
        return Material.ROTTEN_FLESH;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int progress(ISbItem item) {
        return 0;
    }
}
