package me.carscupcake.sbremake.util.lootTable.rngMeter;

import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.kyori.adventure.nbt.CompoundBinaryTag;

import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public record RngMeterItemLoot(SbItemStack item, int min, int max, SlayerLootTable.LootTableType type, ISlayer slayer, double weight) implements RngMeterLoot {
    public RngMeterItemLoot(SbItemStack item, int amount, SlayerLootTable.LootTableType type, ISlayer slayer, double weight) {
        this(item, amount, amount, type, slayer, weight);
    }
    public RngMeterItemLoot(SbItemStack item, SlayerLootTable.LootTableType type, ISlayer slayer, double weight) {
        this(item, 1, 1, type, slayer, weight);
    }
    public RngMeterItemLoot(Class<? extends ISbItem> item, int amount, SlayerLootTable.LootTableType type, ISlayer slayer, double weight) {
        this(SbItemStack.from(item), amount, amount, type, slayer, weight);
    }
    public RngMeterItemLoot(Class<? extends ISbItem> item, SlayerLootTable.LootTableType type, ISlayer slayer, double weight) {
        this(SbItemStack.from(item), 1, 1, type, slayer, weight);
    }
    @Override
    public SbItemStack previewItem() {
        return item.update();
    }

    @Override
    public boolean isGuaranteed(SkyblockPlayer player) {
        SlayerRngMeter rngMeter = player.getSlayers().get(slayer).getMeter();
        if (rngMeter.getSelected() == null) return false;
        return rngMeter.getSelected().contains(this) && rngMeter.getRngMeterXp() >= rngMeter.getRequiredXp(this);
    }

    @Override
    public Set<SbItemStack> loot(SkyblockPlayer player) {
        if (player != null) {
            SlayerRngMeter meter = player.getSlayers().get(slayer).getMeter();
            if (meter.getSelected() != null && meter.getSelected().contains(this))
                meter.setRngMeterXp(0);
        }
        SbItemStack itemStack = (item.sbItem().isUnstackable()) ? SbItemStack.from(item.item().withTag(Modifier.EXTRA_ATTRIBUTES, ((CompoundBinaryTag) item.item().getTag(Modifier.EXTRA_ATTRIBUTES)).putString("uuid", UUID.randomUUID().toString()))) : item;
        return Set.of(Objects.requireNonNull(max - min == 0 ? itemStack.withAmount(max) : itemStack.withAmount(new Random().nextInt(max - min) + min)).update(player));
    }

    @Override
    public double chance(SkyblockPlayer player) {
        return weight;
    }
}
