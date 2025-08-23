package me.carscupcake.sbremake.widgets;

import lombok.Getter;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Span;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.widgets.impl.*;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.GameMode;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Getter
public class WidgetContainer extends TaskScheduler {
    private final List<TabListItem> listPlayers = new ArrayList<>();

    private final SkyblockPlayer player;

    private int allocatedSpace = 0;

    private final List<IWidget> widgets = new ArrayList<>();

    public WidgetContainer(SkyblockPlayer player) {
        this.player = player;
        var config = new ConfigFile("widgets", player);
        for (int i = 0; i < 80; i++) {
            var entry = new PlayerInfoUpdatePacket.Entry(UUID.randomUUID(),
                    "listing" + i, List.of(i == 60 ? PlayerListingColor.DarkAqua.getProperty() : PlayerListingColor.Gray.getProperty()), true, 0, GameMode.SURVIVAL, Component.text( i == 60 ? "          §3§lINFO" : ""), null, 1000 - i, false);
            player.sendPacket(new PlayerInfoUpdatePacket(EnumSet.of(PlayerInfoUpdatePacket.Action.ADD_PLAYER, PlayerInfoUpdatePacket.Action.UPDATE_LATENCY, PlayerInfoUpdatePacket.Action.UPDATE_LISTED,
                    PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER,
                    PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                    PlayerInfoUpdatePacket.Action.UPDATE_HAT), List.of(entry, entry, entry, entry, entry, entry)));
            listPlayers.add(new TabListItem(entry.displayName(), i, entry, this));
        }
        var playerListTabCount = config.get("playLists", ConfigSection.INTEGER, 2);
        var playerList = new PlayerListWidget(player, playerListTabCount);
        registerWidget(playerList);
        var firstSingle = new SingleTabAllocation("          §3§lINFO", PlayerListingColor.DarkAqua);
        registerWidget(firstSingle);
        registerWidget(new GeneralInfoWidget(player));
        registerWidget(new SingleTabAllocation("", PlayerListingColor.Gray));
        registerWidget(new ProfileWidget(player));
        registerWidget(new SingleTabAllocation("", PlayerListingColor.Gray));
        registerWidget(new PetWidget(player));
        registerWidget(new SingleTabAllocation("", PlayerListingColor.Gray));
        registerWidget(WidgetTypes.Skills.create(player, config));
        registerWidget(new SingleTabAllocation("", PlayerListingColor.Gray));
        registerWidget(WidgetTypes.Stats.create(player, config));
        registerWidget(new SingleTabAllocation("", PlayerListingColor.Gray));
        registerWidget(WidgetTypes.Collections.create(player, config));
    }

    public void updateName(int i, TabListItem item) {
        listPlayers.set(i, item);
        player.sendPacket(new PlayerInfoUpdatePacket(EnumSet.of(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME), List.of(item.getEntity())));
    }

    public void updateColor(int i, TabListItem item) {
        listPlayers.set(i, item);
        var entry = item.getEntity();
        player.sendPacket(new PlayerInfoRemovePacket(item.getEntity().uuid()));
        player.sendPacket(new PlayerInfoUpdatePacket(EnumSet.of(PlayerInfoUpdatePacket.Action.ADD_PLAYER, PlayerInfoUpdatePacket.Action.UPDATE_LATENCY, PlayerInfoUpdatePacket.Action.UPDATE_LISTED,
                PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER,
                PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                PlayerInfoUpdatePacket.Action.UPDATE_HAT), List.of(entry, entry, entry, entry, entry, entry)));
    }

    public void registerWidget(IWidget widget) {
        var size = widget.requiredSpace();
        if (size + allocatedSpace >= 60 && allocatedSpace <= 60) {
            allocatedSpace = 61;
        }
        if (allocatedSpace == 61 && widget instanceof SingleTabAllocation) return;
        if (size + allocatedSpace > 80) throw new IllegalArgumentException("Widget is too big");
        var span = Span.slice(listPlayers, allocatedSpace, size);
        allocatedSpace += size;
        widget.render(span);
        widgets.add(widget);
    }

    @Override
    public void run() {
        for (var widget : widgets) {
            widget.update();
            if (rerender) {
                break;
            }
        }
        if (rerender) {
            rerender = false;
            rerender();
        }
    }

    public void rerender() {
        for (int i = 20; i < 80; i++) {
            if (i == 40 || i == 60) continue;
            listPlayers.get(i).updateName(Component.text(""));
        }
        allocatedSpace = 0;
        var widgets = new ArrayList<>(this.widgets);
        this.widgets.clear();
        for (var widget : widgets) {
            registerWidget(widget);
        }
    }
    private boolean rerender = false;
    public void sheduleRerender() {
        this.rerender = true;
    }
}
