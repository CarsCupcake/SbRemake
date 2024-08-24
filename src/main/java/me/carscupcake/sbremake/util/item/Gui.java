package me.carscupcake.sbremake.util.item;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.MapList;
import me.carscupcake.sbremake.util.Returnable;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.ClickType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
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
        if (gui.generalClickEvent.apply(event)) event.setCancelled(true);

    }).addListener(InventoryCloseEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getGui() == null) return;
        Gui gui = player.getGui();
        if (gui.closeEvent.get()) event.setNewInventory(gui.inventory);
        else player.setGui(null);
    }).addListener(InventoryClickEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getGui() == null) return;
        Gui gui = player.getGui();
        gui.getPostClickEvent().accept(event);
    });
    private Function<InventoryPreClickEvent, Boolean> generalClickEvent = _ -> false;
    private Consumer<InventoryClickEvent> postClickEvent = (_) -> {};
    private Returnable<Boolean> closeEvent = () -> false;
    private final MapList<Integer, Function<ClickType, Boolean>> clickEvents = new MapList<>();
    private final Inventory inventory;
    private boolean cancelled = false;
    protected final Set<SkyblockPlayer> players = new HashSet<>();

    public Gui(Inventory inventory) {
        this.inventory = inventory;
    }

    public void showGui(SkyblockPlayer player) {
        player.setGui(this);
        players.add(player);
    }

}
