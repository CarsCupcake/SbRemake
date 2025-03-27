package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;

import java.util.function.Consumer;

public class SwapSlotListener implements Consumer<PlayerChangeHeldSlotEvent> {
    @Override
    public void accept(PlayerChangeHeldSlotEvent playerSwapItemEvent) {
        SbItemStack stack = SbItemStack.from(playerSwapItemEvent.getPlayer().getInventory().getItemStack(playerSwapItemEvent.getSlot()));
        if (stack == null) return;
        playerSwapItemEvent.getPlayer().getInventory().setItemStack(playerSwapItemEvent.getSlot(), stack.update((SkyblockPlayer) playerSwapItemEvent.getPlayer()).item());
    }
}
