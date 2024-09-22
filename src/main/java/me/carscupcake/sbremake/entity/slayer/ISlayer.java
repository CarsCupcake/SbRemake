package me.carscupcake.sbremake.entity.slayer;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerRngMeter;

import java.util.List;

public interface ISlayer {
    String getName();

    String getMobName();

    String getId();

    SlayerEntity getEntity(int tier, SkyblockPlayer player);

    int requiredXp(int currentLevel);

    String getTitle(int level);

    default int getMaxLevel() {
        return 9;
    }
    default int getSlayerXp(int bossTier) {
        return switch (bossTier) {
            case 1 -> 5;
            case 2 -> 25;
            case 3 -> 100;
            case 4 -> 500;
            case 5 -> 1_500;
            default -> 0;
        };
    }

    SlayerRngMeter createRngMeter(SkyblockPlayer player);

    List<RngMeterEntry> getRngMeterEntries();

    boolean addXp(SkyblockEntity entity, int tier);

    boolean startSlayerQuest(int tier, SkyblockPlayer player);
}
