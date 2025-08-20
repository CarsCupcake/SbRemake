package me.carscupcake.sbremake.widgets.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Span;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.widgets.IWidget;
import me.carscupcake.sbremake.widgets.TabListItem;
import me.carscupcake.sbremake.widgets.WidgetTypes;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.TreeMap;

public class StatsWidget implements IWidget {
    private final TreeMap<Stat, Double> cachedSkillValues = new TreeMap<>(Enum::compareTo);
    private final List<Stat> stats;
    private Span<TabListItem> allocated;
    private final SkyblockPlayer player;

    public StatsWidget(SkyblockPlayer player, List<Stat> stats) {
        this.player = player;
        this.stats = stats.stream().sorted(Enum::compareTo).toList();
    }

    @Override
    public void update() {
        int i = 0;
        for (var stat : stats) {
            var s = player.getStat(stat);
            if (s != cachedSkillValues.getOrDefault(stat, -1d)) {
                cachedSkillValues.put(stat, s);
                allocated.get(i + 1).updateName(Component.text(" " + stat.getName() + ": " + stat.getPrefix() + stat.getSymbol() + " " + StringUtils.toFormatedNumber(s)));
            }
            i++;
        }
    }

    @Override
    public void render(Span<TabListItem> allocated) {
        this.allocated = allocated;
        for (var stat : stats) {
            cachedSkillValues.put(stat, -1d);
        }
        allocated.getFirst().updateName(Component.text("§e§lStats"));
        update();
    }

    @Override
    public WidgetTypes type() {
        return null;
    }

    @Override
    public int requiredSpace() {
        return stats.size() + 1;
    }
}
