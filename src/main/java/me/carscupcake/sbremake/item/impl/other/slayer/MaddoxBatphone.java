package me.carscupcake.sbremake.item.impl.other.slayer;

import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

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

    public static void open(SkyblockPlayer player) {

    }

    @Override
    public SoulboundType getSoulboundType() {
        return SoulboundType.Coop;
    }
}
