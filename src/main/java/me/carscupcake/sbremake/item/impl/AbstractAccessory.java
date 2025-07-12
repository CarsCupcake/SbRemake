package me.carscupcake.sbremake.item.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;

import java.util.Map;

public abstract class AbstractAccessory implements ISbItem, ISbItem.StatProvider {
    private final Lore lore;
    private final Map<Stat, Number> stats;

    public AbstractAccessory(Lore lore, Map<Stat, Number> stats) {
        this.lore = lore;
        this.stats = stats;
    }

    public AbstractAccessory(Lore lore) {
        this(lore, Map.of());
    }

    public AbstractAccessory(Map<Stat, Number> stats) {
        this(Lore.EMPTY, stats);
    }

    public AbstractAccessory() {
        this(Lore.EMPTY);
    }

    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public ItemType getType() {
        return ItemType.Accessory;
    }

    @Override
    public Lore getLore() {
        return lore;
    }

    public abstract AccessoryFamily getAccessoryFamily();

    public enum AccessoryFamily {
        NetherrackLookingSunshade,
        PocketEspressoMachine,
        HandyBloodChalice
    }
}
