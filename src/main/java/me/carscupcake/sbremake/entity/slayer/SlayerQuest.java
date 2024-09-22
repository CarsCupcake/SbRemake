package me.carscupcake.sbremake.entity.slayer;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.particle.Particle;

@Getter
public class SlayerQuest {
    private final PlayerSlayer slayer;
    private final int tier;
    @Setter
    private SlayerEntity entity = null;
    @Setter
    private SlayerQuestStage stage = SlayerQuestStage.XpGathering;
    private final double requiredXp;
    private double xp = 0d;
    private double lastXp = 0;

    public SlayerQuest(PlayerSlayer slayer, int tier, int requiredXp) {
        this.slayer = slayer;
        this.tier = tier;
        this.requiredXp = requiredXp;
    }

    public void addXp(double xp, Pos entityLocation) {
        if (stage != SlayerQuestStage.XpGathering) return;
        lastXp = xp;
        this.xp += xp;
        if (this.xp >= requiredXp) {
            stage = SlayerQuestStage.MobKilling;
            Pos finalEntityLocation = entityLocation.add(0, 0.5, 0);
            new TaskScheduler() {
                int i = 0;
                @Override
                public void run() {
                    ParticleUtils.spawnParticle(slayer.getPlayer().getInstance(), finalEntityLocation, Particle.WITCH, 10, Vec.ZERO,  2);
                    ParticleUtils.spawnParticle(slayer.getPlayer().getInstance(), finalEntityLocation, Particle.INSTANT_EFFECT, 10, Vec.ZERO,  2f);
                    slayer.getPlayer().getInstance().playSound(SoundType.ENTITY_WITHER_SHOOT.create(0.5f, 0.5f + (i / 40f)), finalEntityLocation);
                    if (i == 40) {
                        cancel();
                        entity = slayer.getSlayer().getEntity(tier, slayer.getPlayer());
                        entity.setInstance(slayer.getPlayer().getInstance(), finalEntityLocation);
                        slayer.getPlayer().getInstance().playSound(SoundType.ENTITY_WITHER_SPAWN.create(1, 1), finalEntityLocation);
                        return;
                    }
                    i++;
                }
            }.repeatTask(1);
        }
    }

    public enum SlayerQuestStage {
        XpGathering,
        MobKilling,
        Completed,
        Failed
    }
}
