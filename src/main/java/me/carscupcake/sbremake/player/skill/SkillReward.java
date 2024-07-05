package me.carscupcake.sbremake.player.skill;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public interface SkillReward {
    void reward(SkyblockPlayer player);
    Lore lore();
}
