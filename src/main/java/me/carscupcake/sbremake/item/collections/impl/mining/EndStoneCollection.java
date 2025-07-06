package me.carscupcake.sbremake.item.collections.impl.mining;

import me.carscupcake.sbremake.item.BaseSbItem;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedBlockOfCoal;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedCoal;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.impl.RecipeReward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.item.Material;

import java.util.List;

public class EndStoneCollection extends Collection {
    public EndStoneCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 1_000, 2_500, 5_000, 10_000, 25_000, 50_000}, List.of(List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)), 
                List.of(new SkyblockXpReward(4)), new RecipeReward(Recipe.craftingRecipes.get("END_BIOME_STICK"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("PORTAL_TO_THE_END"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("ENCHANTED_ENDSTONE"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("SILENCE_BLOCK"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("HASTE_BLOCK"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("ENDERMITE"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("END_TRAVEL_SCROLL"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("CATALYST"))),
                List.of(new RecipeReward(Recipe.craftingRecipes.get("END_STONE_SWORD"))),
                List.of(new SkyblockXpReward(4))));
               
        
        
        
    }

    @Override
    public Material showItem() {
        return Material.END_STONE;
    }

    @Override
    public String getId() {
        return "ENDSTONE";
    }

    @Override
    public String getName() {
        return "ENDSTONE";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item) {
            case BaseSbItem i -> {
                if (i.getMaterial() == Material.END_STONE) yield 1;
                yield 0;
            }
            case EnchantedEndstone _ -> 160;
            case null, default -> 0;
        };
    }
}
