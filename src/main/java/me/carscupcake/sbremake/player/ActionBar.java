package me.carscupcake.sbremake.player;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Iterator;
import java.util.List;

public class ActionBar {
    private static final List<ActionBarSection> actionbar = List.of(new Health(),  new DominusAbility(),new Defense(), new Mana());
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
            return  (player.getAbsorption() != 0 ? "§6" : Stat.Health.getPrefix()) +  (StringUtils.cleanDouble(player.getSbHealth() + player.getAbsorption(), 1)) + "/" + (StringUtils.cleanDouble(player.getMaxSbHealth(), 1)) + " " + (Stat.Health.getSymbol()) ;
        }
    }

    private static class DominusAbility implements ActionBarSection {

        @Override
        public boolean show(SkyblockPlayer player) {
            if (player.getFullSetBonusPieceAmount(me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.DominusAbility.INSTANCE) >= 2)
                return me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.DominusAbility.DOMINUS_STACKS.get(player) != 0;
            return false;
        }

        @Override
        public String get(SkyblockPlayer player) {
            return "§6" + (me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.DominusAbility.DOMINUS_STACKS.get(player) == 10 ? "§l" : "") + "ᝐ" + (String.valueOf(me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.DominusAbility.DOMINUS_STACKS.get(player))) ;
        }
    }

    private static class Defense implements ActionBarSection {

        @Override
        public boolean show(SkyblockPlayer player) {
            return true;
        }

        @Override
        public String get(SkyblockPlayer player) {
            return (player.getDefenseString() != null) ? player.getDefenseString() :  (Stat.Defense.getPrefix()) +  (StringUtils.cleanDouble(player.getStat(Stat.Defense))) + " " + (Stat.Defense.getSymbol()) + " Defense";
        }
    }

    private static class Mana implements ActionBarSection {

        @Override
        public boolean show(SkyblockPlayer player) {
            return true;
        }

        @Override
        public String get(SkyblockPlayer player) {
            if (player.isNotEnoughMana())
                return "§c§lNOT ENOUGH MANA";
            return  (Stat.Intelligence.getPrefix()) +  (StringUtils.cleanDouble(player.getMana(), 1)) + "/" + (StringUtils.cleanDouble(player.getManaPool(), 1)) + " " + (Stat.Intelligence.getSymbol()) + " Mana";
        }
    }
}
