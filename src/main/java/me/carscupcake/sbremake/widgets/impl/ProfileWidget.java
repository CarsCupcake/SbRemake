package me.carscupcake.sbremake.widgets.impl;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Span;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.widgets.IWidget;
import me.carscupcake.sbremake.widgets.TabListItem;
import me.carscupcake.sbremake.widgets.WidgetTypes;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.kyori.adventure.text.Component;

public class ProfileWidget implements IWidget {
    private final SkyblockPlayer player;
    private long lastSbXp;
    private double lastBankBalance = -1;
    private Span<TabListItem> allocated;
    public ProfileWidget(SkyblockPlayer player) {
        this.player = player;
    }

    @Override
    public void update() {
        if (lastSbXp != player.getSkyblockXp() || lastBankBalance != player.getBankBalance()) {
            lastSbXp = player.getSkyblockXp();
            lastBankBalance = player.getBankBalance();
            allocated.getFirst().updateName(Component.text("§e§lProfile: §aAny"));
            allocated.get(1).updateName(Component.text(" SB Level: §8[§f" + player.getSkyblockLevel() + "§8] §b" + (player.getSkyblockXp() - (100L * (player.getSkyblockLevel()))) + "§3/§b100"));
            allocated.get(2).updateName(Component.text(" Bank: §6" + StringUtils.toShortNumber(player.getBankBalance())));
            allocated.get(3).updateName(Component.text(" Interest: §cNever!"));
        }
    }

    @Override
    public void render(Span<TabListItem> allocated) {
        this.allocated = allocated;
        lastBankBalance = -1;
        lastSbXp = -1;
        update();
    }

    @Override
    public WidgetTypes type() {
        return WidgetTypes.Profile;
    }

    @Override
    public int requiredSpace() {
        return 4;
    }
}
