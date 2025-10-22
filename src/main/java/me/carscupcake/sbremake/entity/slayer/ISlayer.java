package me.carscupcake.sbremake.entity.slayer;

import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.config.KeyClass;
import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Characters;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerRngMeter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public interface ISlayer extends KeyClass {
    @Override
    default String key() {
        return getId();
    }

    String getName();

    String getMobName();

    String getId();

    MobType getMobType();

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

    SlayerRngMeter createRngMeter(SkyblockPlayer player, ConfigSection section);

    List<RngMeterEntry> getRngMeterEntries();

    int getRequiredSlayerQuestXp(int tier);

    boolean addXp(SkyblockEntity entity, int tier);

    boolean startSlayerQuest(int tier, SkyblockPlayer player);

    Material getMaterial();

    List<String> getLore();

    default ItemStack getDisplayItem(int tier) {
        return new ItemBuilder(getMaterial())
                .setName("§c" + Characters.Skull + " §e" + getMobName())
                .addLoreRow(getMobType().toString())
                .addLore("§7 ")
                .addAllLore(getLore())
                .addLoreRow(" ")
                .addLoreRow("§7" + getName() + " Level: §e LVL " + tier)
                .addLoreRow(" ")
                .addLoreRow("§eClick to view boss!")
                .build();
    }

    static ISlayer fromKey(String s) {
        for (var slayer : Slayers.values()) {
            if (slayer.key().equals(s))
                return slayer;
        }
        return null;
    }
}
