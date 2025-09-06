package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.GetItemStatEvent;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class DungeonItem implements ISbItem, Dungeonizable {
    public static final Modifier<Integer> DROPPED_FLOOR = new Modifier<>() {
        @Override
        public @NotNull Integer getFromNbt(SbItemStack item) {
            return item.item().get(DataComponents.CUSTOM_DATA, new CustomData(CompoundBinaryTag.empty())).nbt().getInt("item_tier", 0);
        }

        @Override
        public SbItemStack toNbt(Integer integer, SbItemStack itemStack) {
            return SbItemStack.from(itemStack.item().with(DataComponents.CUSTOM_DATA,
                    itemStack.item().get(DataComponents.CUSTOM_DATA, new CustomData(CompoundBinaryTag.empty())).withTag(Tag.Integer("item_tier"), integer)));
        }
    };
    public static final EventNode<Event> LISTENER = EventNode.all("dungeon_item")
            .addListener(GetItemStatEvent.class, event -> {
                if (event.getItemStack().sbItem() instanceof DungeonItem item) {
                    if (item.stats.containsKey(event.getStat())) {
                        var arr = item.stats.get(event.getStat());
                        if (arr.length == 0) return;
                        var index = Math.min(event.getItemStack().getModifier(DROPPED_FLOOR), arr.length - 1);
                        if (index > 0) {
                            var base = arr[0];
                            event.addValue(arr[index].doubleValue() - base.doubleValue());
                        }
                    }
                }
            });
    private final Map<Stat, Number[]> stats;

    protected DungeonItem(Map<Stat, Number[]> stats) {
        this.stats = stats;
    }

    @Override
    public boolean isDungeonItem() {
        return true;
    }

    @Override
    public double getStat(Stat stat) {
        if (stats.containsKey(stat)) return stats.get(stat)[0].doubleValue();
        return ISbItem.super.getStat(stat);
    }

    @Override
    public final int getMaxStars() {
        return 5;
    }
}
