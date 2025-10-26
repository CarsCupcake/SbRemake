package me.carscupcake.sbremake.entity.slayer;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.config.*;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.particle.Particle;

@Getter
public class SlayerQuest implements DefaultConfigItem {
    @ConfigField(name = "slayer")
    private final ISlayer iSlayer;
    private final PlayerSlayer slayer;
    @ConfigField
    private final int tier;
    @Setter
    private SlayerEntity entity = null;
    @Setter
    @ConfigField
    private SlayerQuestStage stage = SlayerQuestStage.XpGathering;
    private final double requiredXp;
    @ConfigField
    private double xp = 0d;
    private double lastXp = 0;

    @ConfigConstructor
    public SlayerQuest(@ConfigParameter("slayer") ISlayer slayer, @ConfigParameter("tier") int tier, @ConfigParameter("xp") double xp, @ConfigParameter("stage") SlayerQuestStage stage, @ConfigContext("player") SkyblockPlayer player) {
        this.iSlayer = slayer;
        this.slayer = player.getSlayers().get(slayer);
        this.tier = tier;
        this.xp = xp;
        this.requiredXp = slayer.getRequiredSlayerQuestXp(tier);
        this.stage = stage;
    }

    public SlayerQuest(PlayerSlayer slayer, int tier, int requiredXp) {
        iSlayer = slayer.getSlayer();
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
                    ParticleUtils.spawnParticle(slayer.getPlayer().getInstance(), finalEntityLocation, Particle.WITCH, 10, Vec.ZERO, 2);
                    ParticleUtils.spawnParticle(slayer.getPlayer().getInstance(), finalEntityLocation, Particle.INSTANT_EFFECT, 10, Vec.ZERO, 2f);
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

    public void claim() {
        if (stage != SlayerQuestStage.Completed) {
            throw new IllegalStateException("Slayer quest is not completed.");
        }
        slayer.addXp(slayer.getSlayer().getSlayerXp(tier));
        slayer.getPlayer().sendMessage("§aYou have claimed the quest.");
    }

    public enum SlayerQuestStage {
        XpGathering,
        MobKilling,
        Completed,
        Failed
    }
}
