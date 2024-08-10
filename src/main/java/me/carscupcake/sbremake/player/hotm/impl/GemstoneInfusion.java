package me.carscupcake.sbremake.player.hotm.impl;

import kotlin.Pair;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.PickaxeAbility;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

public class GemstoneInfusion extends PickaxeAbility {
    public GemstoneInfusion(SkyblockPlayer player) {
        super(player, GiftsFromTheDeparted.class);
    }

    @Override
    public int cooldown() {
        return 140;
    }

    @Override
    public void onInteract() {

    }

    @Override
    public String getName() {
        return "Gemstone Infusion";
    }

    @Override
    public String getId() {
        return "GEMSTONE_INFUSION_ABILITY";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Increases the effectiveness of §6every Gemstone §7in your pick's Gemstone Slots by §a50% §7for §a16s§7.");
    }

    @Override
    public int levelRequirement() {
        return 10;
    }
}
