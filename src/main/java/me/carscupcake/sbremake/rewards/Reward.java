package me.carscupcake.sbremake.rewards;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public interface Reward {
    void reward(SkyblockPlayer player);
    Lore lore();
}
