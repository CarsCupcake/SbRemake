package me.carscupcake.sbremake.widgets.impl;

import me.carscupcake.sbremake.util.Span;
import me.carscupcake.sbremake.widgets.IWidget;
import me.carscupcake.sbremake.widgets.PlayerListingColor;
import me.carscupcake.sbremake.widgets.TabListItem;
import me.carscupcake.sbremake.widgets.WidgetTypes;
import net.kyori.adventure.text.Component;

public record SingleTabAllocation(String name, PlayerListingColor color) implements IWidget {
    @Override
    public void update() {

    }

    @Override
    public void render(Span<TabListItem> allocated) {
        var item = allocated.getFirst();
        item.updateName(Component.text(name));
        item.updateColor(color);
    }

    @Override
    public WidgetTypes type() {
        return null;
    }

    @Override
    public int requiredSpace() {
        return 1;
    }
}
