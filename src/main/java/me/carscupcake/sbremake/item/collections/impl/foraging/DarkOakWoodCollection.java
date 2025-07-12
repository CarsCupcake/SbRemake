package me.carscupcake.sbremake.item.collections.impl.foraging;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.RecipeReward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.item.Material;

import java.util.List;

public class DarkOakWoodCollection extends Collection {
    private static final List<Reward> skyblockXpReward = List.of(new SkyblockXpReward(4));

    public DarkOakWoodCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 500, 1_000, 2_000, 5_000, 10_000, 30_000}, List.of(skyblockXpReward, skyblockXpReward,
                List.of(new RecipeReward(Recipe.craftingRecipes.get("enchanted_dark_oak_log")),
                        new SkyblockXpReward(4)), skyblockXpReward, skyblockXpReward, skyblockXpReward, skyblockXpReward, skyblockXpReward,
                                                                                                 List.of(new RecipeReward(Recipe.craftingRecipes.get("growth_helmet")),
                                                                                                         new RecipeReward(Recipe.craftingRecipes.get("growth_chestplate")),
                                                                                                         new RecipeReward(Recipe.craftingRecipes.get("growth_leggings")),
                                                                                                         new RecipeReward(Recipe.craftingRecipes.get("growth_boots")),
                                                                                                         new SkyblockXpReward(4))));
    }

    @Override
    public Material showItem() {
        return Material.DARK_OAK_LOG;
    }

    @Override
    public String getId() {
        return "DARK_OAK_WOOD";
    }

    @Override
    public String getName() {
        return "Dark Oak Wood";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item.getId()) {
            case "ENCHANTED_DARK_OAK_LOG" -> 160;
            case "DARK_OAK_WOOD", "DARK_OAK_LOG" -> 1;
            default -> 0;
        };
    }
}
