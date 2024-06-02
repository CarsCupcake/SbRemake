package me.carscupcake.sbremake.player;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Iterator;
import java.util.List;

public class ActionBar {
    private static final List<ActionBarSection> actionbar = List.of(new Health(), new Defense(), new Mana());
    private final SkyblockPlayer player;
    public ActionBar(SkyblockPlayer player) {
        this.player = player;
    }
    public String build() {
        Iterator<ActionBarSection> it = actionbar.iterator();
        StringBuilder builder = new StringBuilder();
        while (it.hasNext()) {
            ActionBarSection section = it.next();
            if (!section.show(player)) continue;
            builder.append(section.get(player));
            if (it.hasNext()) builder.append("   ");
        }
        return builder.toString();
    }
    public interface ActionBarSection {
        boolean show(SkyblockPlayer player);
        String get(SkyblockPlayer player);
    }


    //Standart Impls (Health def etc)
    private static class Health implements ActionBarSection {

        @Override
        public boolean show(SkyblockPlayer player) {
            return true;
        }

        @Override
        public String get(SkyblockPlayer player) {
            return STR."\{Stat.Health.getPrefix()}\{StringUtils.cleanDouble(player.getSbHealth(), 1)}/\{StringUtils.cleanDouble(player.getMaxSbHealth(), 1)} \{Stat.Health.getSymbol()}";
        }
    }

    private static class Defense implements ActionBarSection {

        @Override
        public boolean show(SkyblockPlayer player) {
            return true;
        }

        @Override
        public String get(SkyblockPlayer player) {
            return STR."\{Stat.Defense.getPrefix()}\{StringUtils.cleanDouble(player.getStat(Stat.Defense))} \{Stat.Defense.getSymbol()}";
        }
    }

    private static class Mana implements ActionBarSection {

        @Override
        public boolean show(SkyblockPlayer player) {
            return true;
        }

        @Override
        public String get(SkyblockPlayer player) {
            return STR."\{Stat.Intelligence.getPrefix()}\{StringUtils.cleanDouble(player.getMana(), 1)}/\{StringUtils.cleanDouble(player.getManaPool(), 1)} \{Stat.Intelligence.getSymbol()}";
        }
    }
}
