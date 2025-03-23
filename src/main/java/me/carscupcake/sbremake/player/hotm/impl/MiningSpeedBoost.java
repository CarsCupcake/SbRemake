package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.PickaxeAbility;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import java.time.Duration;

public class MiningSpeedBoost extends PickaxeAbility {
    public MiningSpeedBoost(SkyblockPlayer player) {
        super(player, TitaniumInsanium.class, LuckOfTheCave.class);
    }

    @Override
    public int cooldown() {
        return 120;
    }

    @Override
    public void onInteract() {
        getPlayer().getTemporaryModifiers().add(Stat.MiningSpeed, new PlayerStatEvent.BasicModifier(getName(), 3, PlayerStatEvent.Type.AddativeMultiplier, PlayerStatEvent.StatsCategory.Hotm), Duration.ofSeconds(20));
        MinecraftServer.getSchedulerManager().buildTask(() -> getPlayer().sendMessage("§cYour Mining Speed Boost has expired!")).delay(TaskSchedule.seconds(20)).schedule();
    }

    @Override
    public String getName() {
        return "Mining Speed Boost";
    }

    @Override
    public String getId() {
        return "MINING_SPEED_BOOST_ABILITY";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Grants §a+300% " + (Stat.MiningSpeed) + " §7for §a20s§7.");
    }

    @Override
    public int levelRequirement() {
        return 2;
    }
}
