package me.carscupcake.sbremake.widgets.impl;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Span;
import me.carscupcake.sbremake.widgets.IWidget;
import me.carscupcake.sbremake.widgets.TabListItem;
import me.carscupcake.sbremake.widgets.WidgetTypes;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.kyori.adventure.text.Component;

public class GeneralInfoWidget implements IWidget {
    private final SkyblockPlayer player;
    private SkyblockWorld.WorldProvider lastProvider;
    private Span<TabListItem> allocated;
    public GeneralInfoWidget(SkyblockPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        if (lastProvider != player.getWorldProvider()) {
            lastProvider = player.getWorldProvider();
            allocated.getFirst().updateName(Component.text("§b§lArea: §7" + lastProvider.type().getName()));
            allocated.get(1).updateName(Component.text(" Server: §8" + lastProvider.getId()));
            allocated.get(2).updateName(Component.text(" Gems: §a0"));
            allocated.get(3).updateName(Component.text(" Fairy Souls: §dTODO!"));
        }
    }

    @Override
    public void render(Span<TabListItem> allocated) {
        this.allocated = allocated;
        lastProvider = null;
        update();
    }

    @Override
    public WidgetTypes type() {
        return WidgetTypes.GeneralInfo;
    }

    @Override
    public int requiredSpace() {
        return 4;
    }
}
