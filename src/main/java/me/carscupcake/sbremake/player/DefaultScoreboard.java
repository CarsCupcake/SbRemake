package me.carscupcake.sbremake.player;

import me.carscupcake.sbremake.util.NumberUtil;
import me.carscupcake.sbremake.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public enum DefaultScoreboard implements Function<SkyblockPlayer, String[]> {
    Server {
        @Override
        public String[] apply(SkyblockPlayer player) {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yy");
            String formattedDate = date.format(format);
            return new String[]{"§7" + (formattedDate) + "§8 " + (player.getWorldProvider().getId()), "§7 "};
        }
    },
    Time {
        @Override
        public String[] apply(SkyblockPlayer player) {
            return new String[]{"§f " + (me.carscupcake.sbremake.worlds.Time.season.getName()), "§7 " + (me.carscupcake.sbremake.worlds.Time.timeToString()) + " " + (NumberUtil.inRange(me.carscupcake.sbremake.worlds.Time.hour, 6, 18) ? "§e☀" : "§b☽")};
        }
    },
    Location {
        @Override
        public String[] apply(SkyblockPlayer player) {
            return new String[]{" §7⏣ " + ((player.getRegion() == null) ? "None" : player.getRegion())};
        }
    },
    Purse {
        @Override
        public String[] apply(SkyblockPlayer player) {
            String s = (player.getPowderString() != null) ? player.getPowderString() : "§fPurse§6 " + (StringUtils.toFormatedNumber(player.getCoins()));
            player.setPowderString(null);
            return new String[]{" ", s, "§fBits:§b ∞"};
        }
    },
    SlayerQuest {
        @Override
        public String[] apply(SkyblockPlayer player) {
            if (player.getSlayerQuest() == null)
                return new String[0];
            return switch (player.getSlayerQuest().getStage()) {
                case XpGathering ->
                        new String[]{" ", "§fSlayer Quest", "§5" + (player.getSlayerQuest().getSlayer().getSlayer().getName()),
                                player.getSlayerQuest().getLastXp() <= 0 ? "§7(§e" + (StringUtils.toShortNumber(player.getSlayerQuest().getXp())) + "§7/§c" + (StringUtils.toShortNumber(player.getSlayerQuest().getRequiredXp())) + "§7) Combat Xp"
                                        : "§7(§e" + ((int) (player.getSlayerQuest().getXp() / player.getSlayerQuest().getLastXp())) + "§7/§c" + ((int) (player.getSlayerQuest().getRequiredXp() / player.getSlayerQuest().getLastXp())) + "§7) Combat Xp"};
                case MobKilling ->
                        new String[]{" ", "§fSlayer Quest", "§5" + (player.getSlayerQuest().getSlayer().getSlayer().getName()), "§eSlay the boss!"};
                case Completed ->
                        new String[]{" ", "§fSlayer Quest", "§a" + (player.getSlayerQuest().getSlayer().getSlayer().getName()), "§eBoss Slain!"};
                case Failed ->
                        new String[]{" ", "§fSlayer Quest", "§c" + (player.getSlayerQuest().getSlayer().getSlayer().getName()), "§eQuest Failed!"};
            };
        }
    },
    Footer {
        @Override
        public String[] apply(SkyblockPlayer player) {
            return new String[]{" ", "§ePowered by", "§eCarsCupcake Engineering"};
        }
    }
}
