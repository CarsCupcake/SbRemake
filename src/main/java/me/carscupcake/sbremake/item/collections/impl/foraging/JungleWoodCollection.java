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

public class JungleWoodCollection extends Collection {
    private static final List<Reward> skyblockXpReward = List.of(new SkyblockXpReward(4));

    public JungleWoodCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 500, 1_000, 2_000, 5_000, 10_000, 25_000}, List.of(skyblockXpReward, skyblockXpReward,
                                                                                                List.of(new RecipeReward(Recipe.craftingRecipes.get(
                                                                                                        "enchanted_jungle_log")),
                                                                                                        new SkyblockXpReward(4)), skyblockXpReward,
                                                                                                 skyblockXpReward, skyblockXpReward,
                                                                                                 List.of(new RecipeReward(Recipe.craftingRecipes.get(
                                                                                                                 "treecapitator_axe")),
                                                                                                         new SkyblockXpReward(4)), skyblockXpReward,
                                                                                                 skyblockXpReward));
    }

    @Override
    public Material showItem() {
        return Material.JUNGLE_LOG;
    }

    @Override
    public String getId() {
        return "JUNGLE_WOOD";
    }

    @Override
    public String getName() {
        return "Jungle Wood";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item.getId()) {
            case "ENCHANTED_JUNGLE_LOG" -> 160;
            case "JUNGLE_WOOD", "JUNGLE_LOG" -> 1;
            default -> 0;
        };
    }
}
