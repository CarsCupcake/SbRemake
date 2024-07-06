package me.carscupcake.sbremake.player;

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
            return new String[]{STR."§7\{formattedDate}§8 mega69", "§7 "};
        }
    },
    Time {
        @Override
        public String[] apply(SkyblockPlayer player) {
            return new String[]{"§fSeason", "§7Time"};
        }
    },
    Location {
        @Override
        public String[] apply(SkyblockPlayer player) {
            return new String[]{STR." §7⏣ \{(player.getRegion() == null) ? "None" : player.getRegion()}"};
        }
    },
    Purse {
        @Override
        public String[] apply(SkyblockPlayer player) {
            return new String[]{" ", STR."§fPurse§6 \{StringUtils.cleanDouble(player.getCoins())}", "§fBits:§b ∞"};
        }
    },
    Footer {
        @Override
        public String[] apply(SkyblockPlayer player) {
            return new String[]{" ", "§ePowered by", "§eCarsCupcake Engineering"};
        }
    }
}
