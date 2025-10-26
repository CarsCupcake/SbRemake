package me.carscupcake.sbremake.entity.slayer;

import lombok.Getter;
import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.lootTable.ILootTable;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

@Getter
public abstract class SlayerEntity extends SkyblockEntity {
    public static final EventNode<@NotNull Event> LISTENER = EventNode.all("slayer-entity")
            .addListener(PlayerDeathEvent.class, event -> {
                var player = (SkyblockPlayer) event.getPlayer();
                if (player.getSlayerQuest() != null && player.getSlayerQuest().getEntity() != null) {
                    player.getSlayerQuest().getEntity().remove();
                    player.getSlayerQuest().setEntity(null);
                    player.getSlayerQuest().setStage(SlayerQuest.SlayerQuestStage.Failed);
                    player.sendMessage("§c§lSLAYER QUEST FAILED!");
                    player.sendMessage("§5 » §7You died! What a noob!");
                }
            });

    protected final SkyblockPlayer owner;

    public SlayerEntity(@NotNull EntityType entityType, ILootTable<SbItemStack> lootTable, SkyblockPlayer owner, MobType... mobTypes) {
        super(entityType, lootTable, mobTypes);
        this.owner = owner;
        if (owner.getSlayerQuest() == null) {
            owner.setSlayerQuest(new SlayerQuest(owner.getSlayers().get(getSlayer()), getTier(), 0));
            owner.getSlayerQuest().setStage(SlayerQuest.SlayerQuestStage.MobKilling);
            owner.getSlayerQuest().setEntity(this);
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (owner != null) {
            PlayerSlayer playerSlayer = owner.getSlayers().get(getSlayer());
            int xp = getSlayer().getSlayerXp(getTier());
            playerSlayer.addXp(xp);
            playerSlayer.getMeter().setRngMeterXp(playerSlayer.getMeter().getRngMeterXp() + xp);
            owner.sendMessage("§dRng Meter Stored Xp: " + (playerSlayer.getMeter().getRngMeterXp()));
            owner.getSlayerQuest().setStage(SlayerQuest.SlayerQuestStage.Completed);
        }
    }

    @Override
    public Function<SkyblockEntity, String> nameTag() {
        return NameTagType.Slayer;
    }

    protected EntityAIGroup slayerTarget() {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getGoalSelectors().addAll(List.of(new MeleeAttackGoal(this, 1.6, 20, TimeUnit.SERVER_TICK)
        ));
        aiGroup.getTargetSelectors().addAll(List.of(new LastEntityDamagerTarget(this, 64), new ClosestEntityTarget(this, 64,
                                                                                                                        entity1 -> entity1.equals(owner))));
        return aiGroup;
    }

    public abstract int getTier();

    public abstract ISlayer getSlayer();
}
