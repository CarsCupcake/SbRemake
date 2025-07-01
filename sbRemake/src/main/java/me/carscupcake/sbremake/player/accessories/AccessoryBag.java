package me.carscupcake.sbremake.player.accessories;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.AbstractAccessory;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public class AccessoryBag {
    public static final EventNode<Event> LISTENER = EventNode.all("player.accessorybag").addListener(PlayerStatEvent.class, event -> {
        if (event.player().getAccessoryBag() == null) return;
        for (var tuning : event.player().getAccessoryBag().getTunings().entrySet()) {
            if (tuning.getKey().getStat() != event.stat()) continue;
            event.modifiers().add(new PlayerStatEvent.BasicModifier(tuning.getKey().name() + " Tuning", tuning.getKey().getPerPoint() * tuning.getValue(), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.TuningPoint));
        }
    });

    private final List<SbItemStack> items;
    @Setter
    private int capacity;
    private int magicalPower;
    @Setter
    private Map<AccessoryStatTunings, Integer> tunings = new HashMap<>();

    public AccessoryBag(ConfigSection config, int capacity) {
        items = Arrays.stream(config.get("items", ConfigSection.ITEM_ARRAY, new SbItemStack[0])).parallel().collect(Collectors.toCollection(ArrayList::new));
        this.capacity = capacity;
        computeMagicalPower();
        for (var elementEntry : config.get("tunings", ConfigSection.SECTION, new ConfigSection(new JsonObject())).getRawElement().getAsJsonObject().entrySet()) {
            tunings.put(AccessoryStatTunings.valueOf(elementEntry.getKey()), elementEntry.getValue().getAsInt());
        }
    }

    private void computeMagicalPower() {
        AtomicInteger magicalPower = new AtomicInteger(0);
        items.parallelStream().collect(Collectors.groupingBy(stack -> ((AbstractAccessory) stack.sbItem()).getAccessoryFamily())).forEach((key, value) -> {
            if (value.size() == 1) {
                magicalPower.addAndGet(getPowerFromRarity(value.getFirst().getRarity()));
            } else {
                var mutable = new ArrayList<>(value);
                mutable.sort(Comparator.comparing(stack -> stack.getRarity().ordinal()));
                magicalPower.addAndGet(getPowerFromRarity(value.getLast().getRarity()));
            }
        });
        this.magicalPower = magicalPower.get();
    }

    public int getTuningPoints() {
        return magicalPower / 10;
    }

    private int getPowerFromRarity(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON, SPECIAL -> 3;
            case UNCOMMON, VERY_SPECIAL -> 5;
            case RARE -> 8;
            case EPIC -> 12;
            case LEGENDARY -> 16;
            default -> 22;
        };
    }
}
