package me.carscupcake.sbremake.entity.slayer;

import lombok.Getter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.lootTable.ILootTable;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Getter
public abstract class SlayerEntity extends SkyblockEntity {
    protected final SkyblockPlayer owner;
    public SlayerEntity(@NotNull EntityType entityType, ILootTable<SbItemStack> lootTable, SkyblockPlayer owner) {
        super(entityType, lootTable);
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
            owner.sendMessage(STR."§dRng Meter Stored Xp: \{playerSlayer.getMeter().getRngMeterXp()}");
            owner.getSlayerQuest().setStage(SlayerQuest.SlayerQuestStage.Completed);
        }
    }

    @Override
    public Function<SkyblockEntity, String> nameTag() {
        return NameTagType.Slayer;
    }

    public abstract int getTier();

    public abstract ISlayer getSlayer();
}
