package me.carscupcake.sbremake.item.collections.impl;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.item.Material;

import java.util.List;

public class OakWoodCollection extends Collection {
    private static final List<Reward> skyblockXpReward = List.of(new SkyblockXpReward(4));

    public OakWoodCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 500, 1_000, 2_000, 5_000, 10_000, 30_000}, List.of(skyblockXpReward, skyblockXpReward, skyblockXpReward, skyblockXpReward, skyblockXpReward, skyblockXpReward, skyblockXpReward, skyblockXpReward, skyblockXpReward));
    }

    @Override
    public Material showItem() {
        return Material.OAK_LOG;
    }

    @Override
    public String getId() {
        return "OAK_WOOD";
    }

    @Override
    public String getName() {
        return "Oak Wood";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item.getId()) {
            case "ENCHANTED_OAK_LOG" -> 160;
            case "OAK_WOOD", "OAK_LOG" -> 1;
            default -> 0;
        };
    }
}
