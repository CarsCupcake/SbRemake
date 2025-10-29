package me.carscupcake.sbremake.item.impl.other.slayer;

import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.gui.Gui;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public class MaddoxBatphone implements ISbItem, HeadWithValue, ISoulbound {
    @Override
    public String getId() {
        return "MADDOX_BATPHONE";
    }

    @Override
    public String getName() {
        return "Maddox Batphone";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTU5NjQ5MTIwODg4NCwKICAicHJvZmlsZUlkIiA6ICJkNjBmMzQ3MzZhMTI0N2EyOWI4MmNjNzE1YjAwNDhkYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCSl9EYW5pZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTMzNmQ3Y2M5NWNiZjY2ODlmNWU4Yzk1NDI5NGVjOGQxZWZjNDk0YTQwMzEzMjViYjQyN2JjODFkNTZhNDg0ZCIKICAgIH0KICB9Cn0=";
    }

    private static final List<Ability> abilities = List.of(new ItemAbility<>("Whassup?", AbilityType.RIGHT_CLICK, playerInteractEvent -> {
        new TaskScheduler() {
            int i = 0;
            @Override
            public void run() {
                if (i == 3) {
                    playerInteractEvent.player().sendMessage("§e[NPC] §5Maddox§f: §b✆ §fYou hear the line pick up...");
                    cancel();
                    open(playerInteractEvent.player());
                    return;
                }

                new TaskScheduler() {
                    int ticks = 0;

                    @Override
                    public void run() {
                        if (ticks == 10) cancel();
                        playerInteractEvent.player().playSound(SoundType.BLOCK_NOTE_BLOCK_PLING.create(1, 1.67f));
                        ticks++;
                    }
                }.repeatTaskAsync(1, 1);
                playerInteractEvent.player().sendMessage("§e✆ Ring... ".repeat(i + 1));
                i++;
            }
        }.repeatTask(20);
    }, new Lore("§7Lets you call §5Maddox§7, when he's not busy.")));

    @Override
    public List<Ability> getDefaultAbilities() {
        return abilities;
    }
    private static final ItemStack NO_SLAYER = new ItemBuilder(Material.COAL_BLOCK)
            .setName("§cNot released yet!")
            .addLoreRow("§7This boss is still in development!")
            .build();
    public static void open(SkyblockPlayer player) {
        var gui = new Gui(new InventoryBuilder(4, "Slayer")
                .fill(TemplateItems.EmptySlot.getItem())
                .fill(NO_SLAYER, 10, 16)
                .setItem(Slayers.Zombie.getDisplayItem(player.getSlayers().get(Slayers.Zombie).getLevel()), 10)
                .setItem(Slayers.Enderman.getDisplayItem(player.getSlayers().get(Slayers.Enderman).getLevel()), 13)
                .setItem(TemplateItems.Close.getItem(), 31)
                .setItem(new ItemBuilder(player.isAutoSlayerEnabled() ? Material.LIME_DYE : Material.GRAY_DYE)
                        .setName("§bAuto-Slayer")
                        .addLore("""
                                §7Upon defeating a boss, §aautomatically§7 completes the quest and starts another of the same type if you have enough §6coins§7 in your purse or bank.
                                """)
                        .addLoreIf(player::isAutoSlayerEnabled, "§7Currently: §aEnabled")
                        .addLoreIf(() -> !player.isAutoSlayerEnabled(), "§7Currently: §cDisabled")
                        .addAllLore(" ", "§eClick to " + (player.isAutoSlayerEnabled() ? "disable!" : "enable!"))
                        .build(), 28)
                .build());
        gui.getClickEvents().add(31, _ -> {
            player.closeGui();
            return true;
        });
        gui.getClickEvents().add(28, _ -> {
            player.setAutoSlayerEnabled(!player.isAutoSlayerEnabled());
            open(player);
            return true;
        });
        gui.setCancelled(true);
        gui.showGui(player);
    }

    @Override
    public SoulboundType getSoulboundType() {
        return SoulboundType.Coop;
    }
}
