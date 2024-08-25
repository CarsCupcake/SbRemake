package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.util.item.InventoryBuilder;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StatBreakdownCommand extends Command {
    public StatBreakdownCommand() {
        super("statbreakdown");
        ArgumentEnum<Stat> statEnum = new ArgumentEnum<>("stat", Stat.class);
        addSyntax((commandSender, commandContext) -> {
            openStatBreakdown(commandContext.get(statEnum), (SkyblockPlayer) commandSender);
        }, statEnum);
    }

    public void openStatBreakdown(Stat stat, SkyblockPlayer player) {
        PlayerStatEvent event = player.getStatModifiers(stat, true);
        ItemBuilder flatBonuses = new ItemBuilder(Material.PAPER).setName(STR."\{stat.getPrefix()}\{stat.getSymbol()} Flat Bonuses");
        ItemBuilder additiveBonuses = new ItemBuilder(Material.BOOK).setName(STR."\{stat.getPrefix()}\{stat.getSymbol()} Additive Bonuses");
        ItemBuilder multiplicativeBonuses = new ItemBuilder(Material.PAPER).setName(STR."\{stat.getPrefix()}\{stat.getSymbol()} Multiplicative Bonuses");
        for (PlayerStatEvent.PlayerStatModifier modifier : event.modifiers()) {
            switch (modifier.type()) {
                case Value -> flatBonuses.addLoreRow(STR."\{stat.getPrefix()}+\{modifier.value()}\{stat.getSymbol()}§7 \{modifier.name()}");
                case AddativeMultiplier -> additiveBonuses.addLoreRow(STR."\{stat.getPrefix()}+\{modifier.value()}%§7 \{modifier.name()}");
                case MultiplicativeMultiplier -> multiplicativeBonuses.addLoreRow(STR."\{stat.getPrefix()}+\{modifier.value()}x§7 \{modifier.name()}");
            }
        }
        InventoryBuilder builder = new InventoryBuilder(6, stat.getName()).fill(TemplateItems.EmptySlot.getItem())
                .setItem(19, flatBonuses.build()).setItem(21, additiveBonuses.build()).setItem(23, multiplicativeBonuses.build());
        if (stat.getMaxValue() > 0) {
            builder.setItem(25, new ItemBuilder(Material.LEATHER_HELMET).setName(STR."\{stat.getPrefix()}Stat Cap").addLore(STR."§7\nValue: \{stat.getPrefix()}\{stat.getMaxValue()} \{stat.getSymbol()}").build());
        }
        Gui gui = new Gui(builder.build());
        gui.setCancelled(true);
        gui.showGui(player);
    }
}
