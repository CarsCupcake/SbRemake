package me.carscupcake.sbremake.util.gui;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.MapList;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryItemChangeEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.Click;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
public class Gui {
    public static final EventNode<Event> LISTENER = EventNode.all("gui.listener").addListener(InventoryPreClickEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getGui() == null) return;
        Gui gui = player.getGui();
        event.setCancelled(gui.cancelled);
        if (gui.click(event.getSlot(), event.getClick(), event.getInventory()))
            event.setCancelled(true);
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
    }).addListener(InventoryItemChangeEvent.class, event -> {
        if (event.getInventory().getViewers().isEmpty()) return;
        if (event.getInventory() == event.getInventory().getViewers().stream().findFirst().orElseThrow().getInventory()) return;
        for (Player viewer : event.getInventory().getViewers()) {
            SkyblockPlayer player = (SkyblockPlayer) viewer;
            if (player.getGui() == null) return;
            Gui gui = player.getGui();
            gui.getItemChangeEvent().accept(event);

        }
    });
    private Function<InventoryPreClickEvent, Boolean> generalClickEvent = ignored -> false;
    private Consumer<InventoryClickEvent> postClickEvent = (ignored) -> {
    };
    private Consumer<InventoryItemChangeEvent> itemChangeEvent = (ignored) -> {
    };
    private Supplier<Boolean> closeEvent = () -> false;
    private final MapList<Integer, Function<Click, Boolean>> clickEvents = new MapList<>();
    private Inventory inventory;
    private boolean cancelled = false;
    protected final Set<SkyblockPlayer> players = new HashSet<>();

    public Gui(Inventory inventory) {
        this.inventory = inventory;
    }

    public void showGui(SkyblockPlayer player) {
        player.setGui(this);
        players.add(player);
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        players.forEach(player -> player.openInventory(inventory));
    }

    protected boolean click(int slot, Click click, AbstractInventory inventory) {
        boolean cancelled = false;
        for (var clickEvent : clickEvents.get(slot))
            if (clickEvent.apply(click)) cancelled = true;
        return cancelled;
    }

}
