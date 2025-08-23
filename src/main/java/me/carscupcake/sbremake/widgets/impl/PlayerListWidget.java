package me.carscupcake.sbremake.widgets.impl;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.MultiSpan;
import me.carscupcake.sbremake.util.Span;
import me.carscupcake.sbremake.widgets.*;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerListWidget implements IWidget {
    private int lastRecordedPlayerSize = 0;
    private final int tabCount;
    private final SkyblockPlayer player;
    private MultiSpan<TabListItem> playerLists;
    private List<TabListItem> playerStatus;
    private List<Player> lastPlayerList = new ArrayList<>();

    public PlayerListWidget(SkyblockPlayer player, int tabCount) {
        this.tabCount = tabCount;
        this.player = player;
    }

    @Override
    public void update() {
        if (lastRecordedPlayerSize != player.getInstance().getPlayers().size()) {
            lastRecordedPlayerSize = player.getInstance().getPlayers().size();
            for (var item : playerStatus) {
                item.updateName(Component.text("     §a§lPlayers §f(" + player.getInstance().getPlayers().size() + ")     "));
            }
        }
        var players = player.getInstance().getPlayers().stream().sorted(Comparator.comparing(Player::getUsername)).toList();
        boolean changed = false;
        for (int i = 0; i < Math.min(players.size(), 38); i++) {
            var player = players.get(i);
            if (lastPlayerList.size() <= i || !player.equals(lastPlayerList.get(i))) {
                playerLists.get(i).updateName(Component.text(player.getUsername()));
                changed = true;
            }
        }
        if (playerLists.size() > players.size()) {
            for (int i = players.size(); i < playerLists.size(); i++) {
                playerLists.get(i).updateName(Component.text(""));
                changed = true;
            }
        }
        if (changed)
            lastPlayerList = new ArrayList<>(players);
    }

    @Override
    public void render(Span<TabListItem> allocated) {
        lastRecordedPlayerSize = -1;
        lastPlayerList.clear();
        playerStatus = new ArrayList<>(tabCount);
        playerStatus.add(allocated.getFirst());
        if (tabCount == 2) {
            playerStatus.add(allocated.get(20));
        }
        var playerLists = new ArrayList<Span<TabListItem>>(tabCount);
        playerLists.add(Span.slice(allocated, 1, 19));
        if (tabCount == 2) {
            playerLists.add(Span.slice(allocated, 21, 19));
        }
        this.playerLists = MultiSpan.from(playerLists);
        for (var item : playerStatus) {
            item.updateColor(PlayerListingColor.Green);
        }
        update();
    }

    @Override
    public WidgetTypes type() {
        return null;
    }

    @Override
    public int requiredSpace() {
        return tabCount * 20;
    }
}
