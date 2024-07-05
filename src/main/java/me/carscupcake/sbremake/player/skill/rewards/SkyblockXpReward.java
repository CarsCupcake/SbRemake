package me.carscupcake.sbremake.player.skill.rewards;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.SkillReward;

public record SkyblockXpReward(int xp) implements SkillReward {
    @Override
    public void reward(SkyblockPlayer player) {

    }

    @Override
    public Lore lore() {
        return new Lore("§8+ §b20 Skyblock XP");
    }
}
