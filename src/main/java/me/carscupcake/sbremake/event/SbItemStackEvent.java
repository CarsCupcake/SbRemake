package me.carscupcake.sbremake.event;

import lombok.Getter;
import me.carscupcake.sbremake.item.SbItemStack;
import net.minestom.server.event.Event;

@Getter
public class SbItemStackEvent implements Event {
    private final SbItemStack itemStack;

    public SbItemStackEvent(SbItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
