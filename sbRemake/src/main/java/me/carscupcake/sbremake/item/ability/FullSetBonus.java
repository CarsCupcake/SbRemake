package me.carscupcake.sbremake.item.ability;

import lombok.Getter;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;

@Getter
public non-sealed abstract class FullSetBonus implements Ability {
    private final int maxPieces;
    private final int minPieces;
    private final boolean tiered;
    private final String name;

    public FullSetBonus(String name, int maxPieces, int minPieces, boolean tiered) {
        this.maxPieces = maxPieces;
        this.minPieces = minPieces;
        this.tiered = tiered;
        this.name = name;
    }

    public abstract void start(SkyblockPlayer player);

    public abstract void stop(SkyblockPlayer player);

    @Override
    public String headline(SbItemStack item, SkyblockPlayer player) {
        int pieces = (player == null) ? 0 : player.getFullSetBonusPieceAmount(this);
        if (isTiered()) {
            return "§6Tiered Bonus: " + (name) + " " + ((pieces == minPieces) ? "§6" : "§7") + "(" + (pieces) + "/" + (maxPieces) + ")";
        }
        if (maxPieces != minPieces) {
            return "§6" + (maxPieces) + "-Piece Set Bonus: " + (name);
        }
        if (maxPieces == 1)
            return "§6Ability: " + name;
        return "§6Full Set Bonus: " + (name) + " " + ((pieces == minPieces) ? "§6" : "§7") + "(" + (pieces) + "/" + (maxPieces) + ")";
    }
}
