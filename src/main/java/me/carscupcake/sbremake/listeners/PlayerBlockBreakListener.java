package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.blocks.Crop;
import me.carscupcake.sbremake.blocks.FarmingCrystal;
import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.event.LogBreakEvent;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.impl.Hub;
import me.carscupcake.sbremake.worlds.impl.Park;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class PlayerBlockBreakListener implements Consumer<PlayerBlockBreakEvent> {
    @Override
    public void accept(PlayerBlockBreakEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getWorldProvider().type() == SkyblockWorld.Hub) {
            if (player.getRegion() == Hub.Region.Forest) {
                Log log = null;
                for (Log l : Log.logs)
                    if (Objects.requireNonNull(l.block().registry().material()).equals(event.getBlock().registry().material())) {
                        log = l;
                        break;
                    }
                if (log != null) {
                    ((Hub) player.getWorldProvider()).brokenLogs.put(event.getBlockPosition(), new Log.LogInfo(log, event.getBlock().properties()));
                    SbItemStack item = log.drops(player);
                    LogBreakEvent logBreakEvent = new LogBreakEvent(player, event.getBlockPosition(), log, new ArrayList<>(List.of(item)));
                    MinecraftServer.getGlobalEventHandler().call(logBreakEvent);
                    for (SbItemStack i : logBreakEvent.drops()) {
                        ItemEntity entity = new ItemEntity(i.item());
                        entity.setInstance(player.getInstance(), event.getBlockPosition().add(0.5, 0, 0.5));
                        entity.addViewer(player);
                    }
                    player.getSkill(Skill.Foraging).addXp(log.xp());
                    return;
                }
            }
            if (player.getRegion() == Hub.Region.CoalMine) {
                MiningBlock block = null;
                for (MiningBlock miningBlock : player.getWorldProvider().ores(player.getPosition())) {
                    if (miningBlock.getBlock() == event.getBlock()) {
                        block = miningBlock;
                        break;
                    }
                }
                if (block != null && block.allowed(player.getWorldProvider().type())) {
                    block.breakBlock(Pos.fromPoint(event.getBlockPosition()), player, event.getBlockFace());
                    event.setCancelled(true);
                    return;
                }
            }
            for (Crop c : Crop.crops) {
                if (c.block().registry().id() == event.getBlock().registry().id()) {
                    for (SbItemStack item : c.drops(player)) {
                        item.drop(player.getInstance(), event.getBlockPosition().add(0.5, 0, 0.5));
                    }
                    FarmingCrystal closest = null;
                    double distance = Double.MAX_VALUE;
                    for (FarmingCrystal farmingCrystal : ((Hub) player.getWorldProvider()).getCrystals()) {
                        double d = farmingCrystal.location().distanceSquared(event.getBlockPosition());
                        if (d < distance) {
                            closest = farmingCrystal;
                            distance = d;
                        }
                    }
                    if (closest != null && distance < 30 * 30) {
                        closest.blocks().put(event.getBlockPosition(), event.getBlock());
                    } else {
                        MinecraftServer.getSchedulerManager().buildTask(() -> event.getInstance().setBlock(event.getBlockPosition(), event.getBlock())).delay(TaskSchedule.seconds(30)).schedule();
                        Main.LOGGER.info("Crop not in range!");
                    }
                    return;
                }
            }
        } else if (player.getWorldProvider().type() == SkyblockWorld.Park) {
            Log log = null;
            for (Log l : Log.logs)
                if (Objects.requireNonNull(l.block().registry().material()).equals(event.getBlock().registry().material())) {
                    log = l;
                    break;
                }
            if (log != null) {
                ((Park) player.getWorldProvider()).brokenLogs.put(event.getBlockPosition(), new Log.LogInfo(log, event.getBlock().properties()));
                SbItemStack item = log.drops(player);
                LogBreakEvent logBreakEvent = new LogBreakEvent(player, event.getBlockPosition(), log, new ArrayList<>(List.of(item)));
                MinecraftServer.getGlobalEventHandler().call(logBreakEvent);
                for (SbItemStack i : logBreakEvent.drops()) {
                    ItemEntity entity = new ItemEntity(i.item());
                    entity.setInstance(player.getInstance(), event.getBlockPosition().add(0.5, 0, 0.5));
                    entity.addViewer(player);
                }
                player.getSkill(Skill.Foraging).addXp(log.xp());
                return;
            }
        }
        event.setCancelled(true);
    }
}
