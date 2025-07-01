package me.carscupcake.sbremake.util.lootTable.rngMeter;

import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.lootTable.LootTable;

public interface RngMeterLoot extends LootTable.Loot<SbItemStack> {
    SbItemStack previewItem();

    SlayerLootTable.LootTableType type();

    boolean isGuaranteed(SkyblockPlayer player);

    double weight();

    ISlayer slayer();

}
