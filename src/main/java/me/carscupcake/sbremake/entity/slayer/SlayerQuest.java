package me.carscupcake.sbremake.entity.slayer;

import lombok.Getter;

@Getter
public class SlayerQuest {
    private final PlayerSlayer slayer;
    private final int tier;
    private SlayerEntity entity = null;
    private SlayerQuestStage stage = SlayerQuestStage.XpGathering;
    private final double requiredXp;
    private double xp = 0d;
    private final int completionXp;

    public SlayerQuest(PlayerSlayer slayer, int tier, int requiredXp, int xp) {
        this.slayer = slayer;
        this.tier = tier;
        this.requiredXp = requiredXp;
        this.completionXp = xp;
    }

    public enum SlayerQuestStage {
        XpGathering,
        MobKilling
    }
}
