package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.item.SbItemStack;
import net.minestom.server.event.player.PlayerSwapItemEvent;

import java.util.function.Consumer;

public class SwapSlotListener implements Consumer<PlayerSwapItemEvent> {
    @Override
    public void accept(PlayerSwapItemEvent playerSwapItemEvent) {
        SbItemStack stack = SbItemStack.from(playerSwapItemEvent.getMainHandItem());
        stack.update();
    }
}
