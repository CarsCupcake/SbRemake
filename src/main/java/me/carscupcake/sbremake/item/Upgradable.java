package me.carscupcake.sbremake.item;

import com.google.common.base.Strings;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Cost;

public interface Upgradable {
    int getMaxStars();

    Cost[] upgradeCost(SbItemStack item, int star);

    default String getStarDisplay(int stars) {
        if (stars == 0) return "";
        String s = "";
        if (!(stars <= 0)) {
            if (stars <= 5) {
                s = "§6" + (Strings.repeat("✪", stars));
            } else if (stars <= 10) {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < 5; i++)
                    str.append((i + 6 > stars) ? "§6" : "§d").append("✪");
                s = str.toString();
            } else if (stars <= 15) {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < 5; i++)
                    str.append((i + 11 > stars) ? "§d" : "§b").append("✪");
                s = str.toString();
            }
        }
        return s;
    }

    default double getBonus(SkyblockPlayer player, int stars) {
        return stars * 0.02d;
    }
}
