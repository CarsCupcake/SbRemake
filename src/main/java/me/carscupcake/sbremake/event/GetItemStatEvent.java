package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.SbItemStack;

@Getter
@Setter
public class GetItemStatEvent extends SbItemStackEvent{
    private final Stat stat;
    private double value;
    private double multiplier = 1;
    public GetItemStatEvent(SbItemStack stack, Stat stat, double value) {
        super(stack);
        this.value = value;
        this.stat = stat;
    }
}
