package me.carscupcake.sbremake.util.lootTable.rngMeter;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.config.ConfigField;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.config.DefaultConfigItem;
import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.player.SkyblockPlayer;

import java.util.Map;

@Getter
public class SlayerRngMeter implements DefaultConfigItem {
    private final SkyblockPlayer player;
    private final ISlayer slayer;
    private final Map<RngMeterEntry, Double> lootTableGoals;
    private final Map<RngMeterEntry, Double> lootTableChances;
    @Setter
    @ConfigField
    private double rngMeterXp;
    private RngMeterEntry selected = null;
    @ConfigField
    private String selectedId = null;

    public SlayerRngMeter(SkyblockPlayer player, ISlayer slayer, Map<RngMeterEntry, Double> lootTableGoal, Map<RngMeterEntry, Double> lootTableChances, ConfigSection section) {
        this.player = player;
        this.slayer = slayer;
        this.lootTableChances = lootTableChances;
        lootTableGoals = lootTableGoal;
        load(section);
        if (selectedId != null) {
            for (RngMeterEntry entry : lootTableGoals.keySet())
                if (entry.id().equals(selectedId)) {
                    this.selected = entry;
                    break;
                }
        }
    }

    public double calculateWeight(RngMeterLoot loot, double weight) {
        for (Map.Entry<RngMeterEntry, Double> entry : lootTableGoals.entrySet()) {
            for (RngMeterLoot rngMeterLoot : entry.getKey().loots()) {
                if (rngMeterLoot == loot) {
                    return weight * (1 + Math.min(2, 2 * (rngMeterXp / entry.getValue())));
                }
            }
        }
        return weight;
    }

    public double getRequiredXp(RngMeterLoot loot) {
        for (Map.Entry<RngMeterEntry, Double> entry : lootTableGoals.entrySet())
            if (entry.getKey().contains(loot)) return entry.getValue();
        throw new IllegalArgumentException("Loot was not in table!");
    }

    public RngMeterEntry findById(String id) {
        for (RngMeterEntry entry : lootTableGoals.keySet())
            if (entry.id().equals(id)) return entry;
        return null;
    }

    public void setSelected(RngMeterEntry entry) {
        this.selected = entry;
        this.selectedId = entry.id();
    }
}
