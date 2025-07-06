package me.carscupcake.sbremake.item.collections.impl.combat;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.Reward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.item.Material;

import java.util.List;

public class BlazeRodCollection extends Collection {
    private static final List<Reward> skyblockXpReward = List.of(new SkyblockXpReward(4));

    public BlazeRodCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 250, 1_000, 2_500, 5_000, 10_000, 25_000, 50_000}, List.of(skyblockXpReward, skyblockXpReward,
                                                                                                    skyblockXpReward,
                                                                                                         skyblockXpReward,
                                                                                                        skyblockXpReward,
                                                                                                         skyblockXpReward,
                                                                                                         skyblockXpReward, skyblockXpReward));
    }

    @Override
    public Material showItem() {
        return Material.BLAZE_ROD;
    }

    @Override
    public String getId() {
        return "BLAZE_ROD";
    }

    @Override
    public String getName() {
        return "Blaze Rod";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item.getId()) {
            case "ENCHANTED_BLAZE_ROD", "ENCHANTED_BLAZE_POWDER" -> 160;
            case "BLAZE_ROD", "BLAZE_POWDER" -> 1;
            default -> 0;
        };
    }
}
