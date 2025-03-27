package me.carscupcake.sbremake.util.lootTable.rngMeter;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.player.SkyblockPlayer;

import java.util.Map;

@Getter
public class SlayerRngMeter {
    private final SkyblockPlayer player;
    private final ISlayer slayer;
    private final Map<RngMeterEntry, Double> lootTableGoals;
    private final Map<RngMeterEntry, Double> lootTableChances;
    @Setter
    private double rngMeterXp;
    @Setter
    private RngMeterEntry selected = null;

    public SlayerRngMeter(SkyblockPlayer player, ISlayer slayer, Map<RngMeterEntry, Double> lootTableGoal, Map<RngMeterEntry, Double> lootTableChances) {
        this.player = player;
        this.slayer = slayer;
        this.lootTableChances = lootTableChances;
        lootTableGoals = lootTableGoal;
        ConfigFile file = new ConfigFile("rngmeters", player);
        rngMeterXp = file.get(slayer.getId(), ConfigSection.DOUBLE, 0d);
        String selected = file.get((slayer.getId()) + "_SELECTED", ConfigSection.STRING);
        if (selected != null) {
            for (RngMeterEntry entry : lootTableGoals.keySet())
                if (entry.id().equals(selected)) {
                    this.selected = entry;
                    break;
                }
        }
    }

    public void save() {
        ConfigFile file = new ConfigFile("rngmeters", player);
        file.set(slayer.getId(), rngMeterXp, ConfigSection.DOUBLE);
        file.set((slayer.getId()) + "_SELECTED", selected == null ? null : selected.id(), ConfigSection.STRING);
        file.save();
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
}
