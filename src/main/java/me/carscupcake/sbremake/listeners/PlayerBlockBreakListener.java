package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.impl.HubWorld;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

import java.util.Objects;
import java.util.function.Consumer;

public class PlayerBlockBreakListener implements Consumer<PlayerBlockBreakEvent> {
    @Override
    public void accept(PlayerBlockBreakEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getWorldProvider().type() == SkyblockWorld.Hub) {
            if (player.getRegion() == HubWorld.Region.Forest) {
                Log log = null;
                for (Log l : Log.logs)
                    if (Objects.requireNonNull(l.block().registry().material()).equals(event.getBlock().registry().material())) {
                        log = l;
                        break;
                    }
                if (log != null) {
                    ((HubWorld) player.getWorldProvider()).brokenLogs.put(event.getBlockPosition(), new Log.LogInfo(log, event.getBlock().properties()));
                    SbItemStack item = log.drops(player);
                    ItemEntity entity = new ItemEntity(item.item());
                    entity.setInstance(player.getInstance(), event.getBlockPosition().add(0.5, 0, 0.5));
                    entity.addViewer(player);
                    player.getSkill(Skill.Foraging).addXp(log.xp());
                    return;
                }
            }
            if (player.getRegion() == HubWorld.Region.CoalMine) {
                MiningBlock block = MiningBlock.BLOCKS.get(event.getBlock());
                if (block != null && block.allowed(player.getWorldProvider().type())) {
                    block.breakBlock(Pos.fromPoint(event.getBlockPosition()), player, event.getBlockFace());
                    event.setCancelled(true);
                    return;
                }
            }
        }
        event.setCancelled(true);
    }
}
