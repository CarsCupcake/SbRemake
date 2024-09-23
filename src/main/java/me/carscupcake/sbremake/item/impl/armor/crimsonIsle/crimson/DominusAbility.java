package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.CountMap;
import me.carscupcake.sbremake.util.TaskScheduler;

import java.util.HashMap;
import java.util.Map;

public class DominusAbility extends FullSetBonus {
    public static final DominusAbility INSTANCE = new DominusAbility();
    public static final CountMap<SkyblockPlayer> DOMINUS_STACKS = new CountMap<>();
    public DominusAbility() {
        super("Dominus", 4, 2, true);
    }
    public static final Map<SkyblockPlayer, DominusCounter> task = new HashMap<>();
    @Override
    public void start(SkyblockPlayer player) {
        DOMINUS_STACKS.put(player, 0);
        task.put(player, new DominusCounter(player, 20 * switch (player.getFullSetBonusPieceAmount(this)) {
            case 3 -> 7;
            case 4 -> 10;
            default -> 4;
        }));
    }

    @Override
    public void stop(SkyblockPlayer player) {
        task.remove(player).cancel();
        DOMINUS_STACKS.remove(player);
    }

    public static class DominusCounter extends TaskScheduler {
        private final SkyblockPlayer player;
        private final int resetTime;
        int i;
        public int cooldown = 10;
        public DominusCounter(SkyblockPlayer player, int resetTime) {
            this.resetTime = resetTime;
            i = resetTime;
            this.player = player;
            repeatTask(1, 1);
        }

        @Override
        public void run() {
            if (cooldown != 0) cooldown--;
            if (i == 0) {
                i = resetTime;
                if (DOMINUS_STACKS.get(player) > 0) DOMINUS_STACKS.subtract(player, 1);
            } else i--;
        }

        public void resetTime() {
            i = resetTime;
        }
    }

    @Override
    public Lore lore() {
        return new Lore(STR."§7For every melee kill gain §c1§7 stack of §6Dominus ᝐ§7.\n \nEach §6Dominus ᝐ stack grants §e+0.1 \{Stat.SwingRange}\n \n§7At §c10§7 stacks also §bswipe§7 in a random direction hitting every enemy in the path of the swipe.\n §8\n§7Lose 1 stack after §c%s%s§7 of not gaining a stack.",
                "%s%", (_, player) -> player == null ? "4" :
                switch (player.getFullSetBonusPieceAmount(this)) {
                    case 3 -> "7";
                    case 4 -> "10";
                    default -> "4";
                });
    }
}
