package me.carscupcake.sbremake.widgets;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;

import java.util.List;

@Getter
public class TabListItem {
    private Component name;
    private PlayerInfoUpdatePacket.Entry entity;
    private final int index;
    private PlayerListingColor color = PlayerListingColor.Gray;
    public WidgetContainer container;
    
    public TabListItem(Component name, int index, PlayerInfoUpdatePacket.Entry entity, WidgetContainer container) {
        this.name = name;
        this.index = index;
        this.entity = entity;
        this.container = container;
    }

    public void updateName(Component name) {
        this.name = name;
        this.entity =  new PlayerInfoUpdatePacket.Entry(entity.uuid(), entity.username(), entity.properties(), entity.listed(), entity.latency(), entity.gameMode(), name, entity.chatSession(), entity.listOrder(), entity.displayHat());
        container.updateName(index, this);
    }

    public void updateColor(PlayerListingColor color) {
        this.color = color;
        this.entity =  new PlayerInfoUpdatePacket.Entry(entity.uuid(), entity.username(), List.of(color.getProperty()), entity.listed(), entity.latency(), entity.gameMode(), entity.displayName(), entity.chatSession(), entity.listOrder(), entity.displayHat());
        container.updateColor(index, this);
    }
}
