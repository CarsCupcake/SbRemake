package me.carscupcake.sbremake.util;

import lombok.Getter;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

@Getter
@SuppressWarnings("unused")
public enum TemplateItems {

    EmptySlot(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").setHideTooltip(true).build()),
    Close(new ItemBuilder(Material.BARRIER).setName("§cClose").build()),
    BackArrow(new ItemBuilder(Material.ARROW).addLoreRow("§7Click to get back").setName("§aBack").build()),
    NextArrow(new ItemBuilder(Material.ARROW).addLoreRow("§7Click to get to the next page").setName("§aNext").build());
    private final ItemStack item;

    TemplateItems(ItemStack item) {
        this.item = item;
    }

}
