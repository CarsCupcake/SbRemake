package me.carscupcake.sbremake.item.impl.rune;

import net.minestom.server.entity.Entity;

public interface RuneTicker<T extends Entity>  {
    IRune<T> getRune();
    void tick();
}
