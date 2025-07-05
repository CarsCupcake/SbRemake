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

public class SpruceWoodCollection extends Collection {
    private static final List<Reward> skyblockXpReward = List.of(new SkyblockXpReward(4));

    public SpruceWoodCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 500, 1_000, 2_000, 5_000, 10_000, 25_000}, List.of(skyblockXpReward,
                                                                                                List.of(new RecipeReward(Recipe.craftingRecipes.get(
                                                                                                        "spruce_axe")),
                                                                                                        new SkyblockXpReward(4)),
                                                                                                 List.of(new RecipeReward(Recipe.craftingRecipes.get(
                                                                                                                 "enchanted_spruce_log")),
                                                                                                         new SkyblockXpReward(4)),
                                                                                                        skyblockXpReward,
                                                                                                 skyblockXpReward, skyblockXpReward,
                                                                                                 List.of(new RecipeReward(Recipe.craftingRecipes.get(
                                                                                                                 "treecapitator_axe")),
                                                                                                         new SkyblockXpReward(4)), skyblockXpReward,
                                                                                                 skyblockXpReward));
    }

    @Override
    public Material showItem() {
        return Material.SPRUCE_LOG;
    }

    @Override
    public String getId() {
        return "SPRUCE_WOOD";
    }

    @Override
    public String getName() {
        return "Spruce Wood";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item.getId()) {
            case "ENCHANTED_SPRUCE_LOG" -> 160;
            case "SPRUCE_WOOD", "SPRUCE_LOG" -> 1;
            default -> 0;
        };
    }
}
