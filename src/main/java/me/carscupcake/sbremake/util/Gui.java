package me.carscupcake.sbremake.util;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.ClickType;

import java.util.function.Function;

@Getter
@Setter
public class Gui {
    public static final EventNode<Event> LISTENER = EventNode.all("gui.listener").addListener(InventoryPreClickEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getGui() == null) return;
        Gui gui = player.getGui();
        event.setCancelled(gui.cancelled);
        for (Function<ClickType, Boolean> clickEvent : gui.clickEvents.get(event.getSlot()))
            if (clickEvent.apply(event.getClickType())) event.setCancelled(true);
        if (gui.generalClickEvent.apply(event.getClickType())) event.setCancelled(true);

    }).addListener(InventoryCloseEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getGui() == null) return;
        Gui gui = player.getGui();
        if (gui.closeEvent.get()) event.setNewInventory(gui.inventory);
        else player.setGui(null);
    });
    private Function<ClickType, Boolean> generalClickEvent = _ -> false;
    private Returnable<Boolean> closeEvent = () -> false;
    private final MapList<Integer, Function<ClickType, Boolean>> clickEvents = new MapList<>();
    private final Inventory inventory;
    private boolean cancelled = false;

    public Gui(Inventory inventory) {
        this.inventory = inventory;
    }

    public void showGui(SkyblockPlayer player) {
        player.setGui(this);
    }

}
