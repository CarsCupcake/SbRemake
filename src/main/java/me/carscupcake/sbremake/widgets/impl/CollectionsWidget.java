package me.carscupcake.sbremake.widgets.impl;

import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.util.Span;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.widgets.IWidget;
import me.carscupcake.sbremake.widgets.TabListItem;
import me.carscupcake.sbremake.widgets.WidgetTypes;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class CollectionsWidget implements IWidget {
    private final List<Collection> collections;
    private final List<Long> collectionAmounts = new ArrayList<>();
    private Span<TabListItem> allocated;
    public CollectionsWidget(List<Collection> collections) {
        this.collections = collections;
    }

    @Override
    public void update() {
        for (int i = 0; i < collectionAmounts.size(); i++) {
            var item = collections.get(i);
            var amount = collectionAmounts.get(i);
            if (item.getProgress() != amount) {
                collectionAmounts.set(i, item.getProgress());
                amount = item.getProgress();
                allocated.get(i+1).updateName(Component.text(" " + item.getName() + " " + StringUtils.toRoman(item.getLevel()) + ": " +
                       (item.getLevel() == item.getMaxLevel() ? "§cMAX" : ("§e" + StringUtils.toFormatedNumber(amount) + "§6/§e" + StringUtils.toShortNumber(item.getLevelProgress()[item.getLevel()])))));
            }
        }
    }

    @Override
    public void render(Span<TabListItem> allocated) {
        this.allocated = allocated;
        collectionAmounts.clear();
        for (var collection : collections) {
            collectionAmounts.add(-1L);
        }
        allocated.getFirst().updateName(Component.text("§e§lCollections"));
        update();
    }

    @Override
    public WidgetTypes type() {
        return WidgetTypes.Collections;
    }

    @Override
    public int requiredSpace() {
        return collections.size() + 1;
    }
}
