package me.carscupcake.sbremake.widgets;

import me.carscupcake.sbremake.util.Span;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;

public interface IWidget {
    void update();
    void render(Span<TabListItem> allocated);
    WidgetTypes type();
    int requiredSpace();
}
