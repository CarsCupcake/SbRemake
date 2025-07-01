package me.carscupcake.sbremake.item.impl.armor.crimsonIsle;

import lombok.Getter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TaskScheduler;

@Getter
public class KuudraArmorAbilityCounter extends TaskScheduler {
    private final SkyblockPlayer player;
    private final int resetTime;
    private final int cooldownTicks;
    public int cooldown = 10;
    int i;
    private long lastGain = System.currentTimeMillis();
    private int stacks = 0;
    private final KuudraArmorTier tier;

    public KuudraArmorAbilityCounter(SkyblockPlayer player, int resetTime, int cooldownTicks, KuudraArmorTier tier) {
        this.resetTime = resetTime;
        this.tier = tier;
        this.cooldownTicks = cooldownTicks;
        i = resetTime;
        this.player = player;
        repeatTask(1, 1);
    }

    @Override
    public void run() {
        if (cooldown != 0) cooldown--;
        if (i == 0) {
            i = resetTime;
            if (stacks > 0) stacks--;
        } else i--;
    }

    public void add() {
        if (System.currentTimeMillis() - lastGain > cooldownTicks * 50L) {
            lastGain = System.currentTimeMillis();
            if (stacks != 10) stacks++;
            i = resetTime;
        }
    }
}
