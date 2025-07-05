package me.carscupcake.sbremake.blocks;

import me.carscupcake.sbremake.item.ISbItem;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public record Log(Block block, ISbItem drop, double xp) {
    public Log(Block block, double xp) {
        this(block, ISbItem.get(Objects.requireNonNull(block.registry().material())), xp);
    }

    public static final Set<Log> logs = Set.of(new Log(Block.OAK_LOG, 6d), new Log(Block.OAK_WOOD, ISbItem.get(Material.OAK_LOG), 6d),
            new Log(Block.BIRCH_LOG, 6d), new Log(Block.BIRCH_WOOD, ISbItem.get(Material.BIRCH_LOG), 6d),
            new Log(Block.DARK_OAK_LOG, 6d), new Log(Block.DARK_OAK_WOOD, ISbItem.get(Material.DARK_OAK_LOG), 6d),
            new Log(Block.ACACIA_LOG, 6d), new Log(Block.ACACIA_WOOD, ISbItem.get(Material.ACACIA_LOG), 6d),
            new Log(Block.JUNGLE_LOG, 6d), new Log(Block.JUNGLE_WOOD, ISbItem.get(Material.JUNGLE_LOG), 6d));

    public record LogInfo(Log log, Map<String, String> properties) {
        public void regen(Instance instance, BlockVec pos) {
            instance.setBlock(pos, log.block.withProperties(properties));
        }
    }

    public static Log getLog(Block block) {
        Log log = null;
        for (Log l : Log.logs)
            if (Objects.requireNonNull(l.block().registry().material()).equals(block.registry().material())) {
                log = l;
                break;
            }
        return log;
    }
}
