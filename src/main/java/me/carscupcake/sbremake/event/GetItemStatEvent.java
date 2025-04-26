package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;

@Getter
@Setter
public class GetItemStatEvent extends SbItemStackEvent {
    private final Stat stat;
    private double value;
    private double multiplier = 1;
    private final SkyblockPlayer player;

    public GetItemStatEvent(SbItemStack stack, Stat stat, double value, SkyblockPlayer player) {
        super(stack);
        this.value = value;
        this.stat = stat;
        this.player = player;
    }

    public void addValue(double value) {
        this.value += value;
    }
}
