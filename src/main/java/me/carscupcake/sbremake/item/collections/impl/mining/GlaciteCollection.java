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

public class GlaciteCollection extends Collection {
    public GlaciteCollection(SkyblockPlayer player) {
        super(player, new int[]{1_000, 2_500, 10_000, 25_000, 100_000, 250_000, 500_000, 750_000, 1000_000}, List.of(List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)), 
                List.of(new SkyblockXpReward(4)), new RecipeReward(Recipe.craftingRecipes.get("ENCHANTED_GLACITE"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("GLACITE_AMALGAMATION"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("ENCHANTED_ENDSTONE"))),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("FRIGID_HUSK"))),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)),
                List.of(new RecipeReward(Recipe.craftingRecipes.get("GLACITE_GOLEM"))),
                List.of(new SkyblockXpReward(4))));
               
        
        
        
    }

    @Override
    public Material showItem() {
        return Material.PACKED_ICE;
    }

    @Override
    public String getId() {
        return "Glacite";
    }

    @Override
    public String getName() {
        return "GLACITE";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item) {
            case BaseSbItem i -> {
                if (i.getMaterial() == Material.Glacite) yield 1;
                yield 0;
            }
            case EnchantedGlacite _ -> 160;
            case null, default -> 0;
        };
    }
        }
