package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.EntityUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.block.BlockFace;

import java.time.Duration;
import java.util.ArrayList;

@DebugCommand
public class BeaconThrowCommand extends Command {
    public BeaconThrowCommand() {
        super("throwbeacon");
        setDefaultExecutor((sender, context) -> {
            var player = (SkyblockPlayer) sender;
            var possibles = new ArrayList<BlockVec>();
            for (int x = -16; x <= 16; x++) {
                for (int y = -5; y <= 5; y++) {
                    for (int z = -16; z <= 16; z++) {
                        if (x * x + z * z < 5 * 5) continue;
                        var root = player.getPosition().add(x, y, z);
                        if (player.getInstance().getBlock(root).isAir()) continue;
                        root = root.add(0, 1, 0);
                        if (!player.getInstance().getBlock(player.getPosition().add(x, y + 2, z)).isAir()) continue;
                        if (!player.getInstance().getBlock(player.getPosition().add(x, y + 3, z)).isAir()) continue;
                        if (!player.getInstance().getBlock(root.relative(BlockFace.NORTH)).isAir()) continue;
                        if (!player.getInstance().getBlock(root.relative(BlockFace.SOUTH)).isAir()) continue;
                        if (!player.getInstance().getBlock(root.relative(BlockFace.EAST)).isAir()) continue;
                        if (!player.getInstance().getBlock(root.relative(BlockFace.WEST)).isAir()) continue;
                        if (player.getInstance().getBlock(root).isAir())
                            possibles.add(new BlockVec(root));
                    }
                }
            }
            var items = possibles.parallelStream().filter(next -> !EntityUtils.blocksInSight(player.getInstance(), player.getPosition().add(0, 1, 0),
                                                                        Vec.fromPoint(next.middle().add(0, .5, 0).sub(player.getPosition().add(0,1, 0))),
                                                                        next.middle().add(0, .5, 0).distance(player.getPosition().add(0,1, 0)))).toList();
            if (items.isEmpty()) {
                player.sendMessage("No beacon pos!");
                return;
            }
            items.forEach(next -> {
                var shulker = new LivingEntity(EntityType.SHULKER);
                shulker.setInstance(player.getInstance(), next.middle());
                shulker.setGlowing(true);
                shulker.setGlowColor(NamedTextColor.GREEN);
                shulker.scheduleRemove(Duration.ofSeconds(10));
                shulker.setGlowing(true);
                shulker.setInvisible(true);
            });
        });
    }
}
