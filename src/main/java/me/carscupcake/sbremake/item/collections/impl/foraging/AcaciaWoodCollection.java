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

public class AcaciaWoodCollection extends Collection {
    private static final List<Reward> skyblockXpReward = List.of(new SkyblockXpReward(4));

    public AcaciaWoodCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 500, 1_000, 2_000, 5_000, 10_000, 25_000}, List.of(skyblockXpReward, skyblockXpReward,
                                                                                                 List.of(new SkyblockXpReward(4),
                                                                                                         new RecipeReward(Recipe.craftingRecipes.get("enchanted_acacia_log"))),
                                                                                                         skyblockXpReward,
                                                                                                        skyblockXpReward,
                                                                                                         skyblockXpReward,
                                                                                                         skyblockXpReward, skyblockXpReward,
                                                                                                         skyblockXpReward));
    }

    @Override
    public Material showItem() {
        return Material.ACACIA_LOG;
    }

    @Override
    public String getId() {
        return "ACACIA_WOOD";
    }

    @Override
    public String getName() {
        return "Acacia Wood";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item.getId()) {
            case "ENCHANTED_ACACIA_LOG" -> 160;
            case "ACACIA_WOOD", "ACACIA_LOG" -> 1;
            default -> 0;
        };
    }
}
